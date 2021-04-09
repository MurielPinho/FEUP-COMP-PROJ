package pt.up.fe.comp.jmm.ast.examples;

import java.util.List;
import java.util.stream.Collectors;

import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;
import pt.up.fe.comp.jmm.report.*;
import pt.up.fe.specs.util.utilities.StringLines;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.analysis.table.SymbolTableUtils;

/**
 * Counts the occurences of each node kind.
 * 
 * @author JBispo
 *
 */
public class AnalysisSemanticVisitor extends PreorderJmmVisitor<AnalysisSemanticInfo, Boolean> {

    private int line = -1;

    public AnalysisSemanticVisitor() {
        addVisit("MethodInvocation", this::methodVerification);
        addVisit("ArrayIndex", this::typeVerification);
    }

    // antes tem de ser: this, contrutor class ou identifier
    public Boolean methodVerification(JmmNode node, AnalysisSemanticInfo analysisSemanticInfo) {
        String methodName = node.get("val");
        this.line = Integer.parseInt(node.get("line"));
        
        JmmNode invoker = this.getMethodOwner(node);
        // we can only invoke a method if the owner of it is: a variable, this or a class constructor
        if(!(invoker.getKind().equals("This") || invoker.getKind().equals("Var") || invoker.getKind().equals("ConstructorClass"))) {
            analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Invalid invoker: " + (invoker.get("val") != null ? invoker.get("val") : invoker.getKind()) + " of method: " + node.get("val") +"!"));
            return false;
        }

        String className = analysisSemanticInfo.getClassName();

        // case of the owner of the method that is being invoked is a constructor class, Ex: new A()
        if(invoker.getKind().equals("ConstructorClass")){
            // the types of the constructor and the class are equal, Ex: Class: A, Constructor Class: new A().c()
            if (invoker.get("val").equals(className)) this.verifyIfMethodExistsInClass(node, analysisSemanticInfo);
            // the types of the constructor and the class are diferents, Ex: Class: A, Constructor Class: new B().c()
            else this.verifyIfClassIsImported(analysisSemanticInfo, invoker.get("val"), methodName);
        }
        // case of the owner of the method that is being invoked is "this", Ex: this.b()
        else if(invoker.getKind().equals("This")) this.verifyIfMethodExistsInClass(node, analysisSemanticInfo);
        else { // case of the owner of the method that is being invoked is a variable
            // tries to get the variable type
            Type type = this.getVariableTypeFromNodeInClass(invoker, analysisSemanticInfo);

            // variable does not belong to the class neither to its methods, Ex: io.println(), io nao e atributo nem variavel local do metodo
            if (type == null) this.verifyIfClassIsImported(analysisSemanticInfo, invoker.get("val"), methodName);
            else { // variable belong to the class or to some method
                // the type of the variable is the same as the class, Ex: a.b(), em que a é uma instancia de A
                if(type.getName().equals(className)) this.verifyIfMethodExistsInClass(node, analysisSemanticInfo);
                // the types of the variable and the class are diferents, Ex: d.b(), em que d nao é uma instancia de A
                else this.verifyIfClassIsImported(analysisSemanticInfo, type.getName(), methodName);
            }
        }

        return true;
    }

    /**
     * Verifies if the class was imported
     * @param analysisSemanticInfo
     * @param className
     * @return true if the class was imported, false otherwise
     */
    private boolean verifyIfClassIsImported(AnalysisSemanticInfo analysisSemanticInfo, String className, String methodName) {
        List<String> imports = analysisSemanticInfo.getImports();
        
        for(String importSt: imports) {
            int num = importSt.lastIndexOf(".");

            if(num == -1 && importSt.equals(className)) return true;
            else if(importSt.substring(num + 1).equals(className)) return true;
        }

        analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Method: " + methodName + "! Invoker class: " + className + " was not imported!"));

        return false;
    }

    /**
     * Verifies if the method exists in the class
     * @param method
     * @param analysisSemanticInfo
     * @return true if the method exists, false otherwise
     */
    private boolean verifyIfMethodExistsInClass(JmmNode method, AnalysisSemanticInfo analysisSemanticInfo) {
        String methodName = method.get("val");
        List<String> methods = analysisSemanticInfo.getMethods();
        Report report = null;

        // it goes through all methods that have the same name
        for(String aux: methods) {  
            if(aux.substring(0, aux.length() - 1).equals(methodName)) {
                List<Symbol> params = analysisSemanticInfo.getMethodParameters(aux);

                // the method declaration and the method invocation have diferent number of args
                if(params.size() != this.getNumArgs(method)) {
                    report = new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Method: " + methodName + "! Invalid number of arguments! Given: " + this.getNumArgs(method) + "; Required: " + params.size() +"!");
                    continue;
                }

                if (params.size() == 0) return true;

                // verifies if all the elements are of the correct type, if so it returns null
                Report rep1 = validArgs(method, params, analysisSemanticInfo);
                if(rep1 == null) return true;
                else if(report == null) report = rep1;
            }
        }

        // the method does not exist in the current class, and the class has no super
        if(!analysisSemanticInfo.getSuper().equals("")) return true;

        if(report == null) analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0,
        "Method: " + methodName + "! This method was not defined in this class!"));
        else analysisSemanticInfo.addReport(report);

        return false;
    }

    /**
     * Returns the owner of the invocation, Ex: a.b(), returns a
     * @param node
     * @return
     */
    private JmmNode getMethodOwner(JmmNode node) {
        List<JmmNode> children = node.getParent().getChildren();
        
        // get previous sibling
        for(int i = 1; i < children.size(); i++){
            if (children.get(i) == node) return children.get(i - 1);
        }

        return null;
    }

    /**
     * Finds the method that owns a variable
     * @param node
     * @return
     */
    private JmmNode findMethodOwnerFromVariable(JmmNode node) {
        JmmNode currNode = node;
        JmmNode parent = currNode.getParent();
        
        while(!parent.getKind().equals("MethodDeclaration")) {
            currNode = parent;
            parent = currNode.getParent();
        }

        return parent.getChildren().get(0);
    }

    /**
     * Retruns the number of arguments of a method
     * @param node
     * @return
     */
    private int getNumArgs(JmmNode node) {
        return (node.getChildren().get(0).getNumChildren() == 0 || node.getChildren().get(0).getChildren().get(0).getNumChildren() == 0) ? 0 : node.getChildren().get(0).getNumChildren();
    }

    /**
     * Verifies if all arguements correspond to all parameters of a method
     * @param node
     * @param params
     * @param analysisSemanticInfo
     * @return
     */
    private Report validArgs(JmmNode node, List<Symbol> params, AnalysisSemanticInfo analysisSemanticInfo) {
        List<JmmNode> args = node.getChildren().get(0).getChildren();

        for(int i = 0; i < args.size(); i++) {
            JmmNode lastNode = args.get(i).getChildren().get(args.get(i).getChildren().size() - 1);
            Type type = this.getTypeFromKind(lastNode, params.get(i), analysisSemanticInfo);

            if (type == null) return new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0,
                                            "Method: " + node.get("val") + "! Imcompatible Operation in argument " + (i + 1));
            
            if (!type.equals(params.get(i).getType())) {
                return new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0,
                                            "Method: " + node.get("val") + "! Imcompatible types " + params.get(i).getType().getName() + (params.get(i).getType().isArray() ? "[]" : "")
                                            + " and " + type.getName() + (type.isArray() ? "[]" : "") + ", in argument " + (i+1) +"!");
            }
        }

        return null;
    }

    /**
     * Given a kind of a node it gives us the type that it represents
     * @param node
     * @param expected
     * @param analysisSemanticInfo
     * @return
     */
    private Type getTypeFromKind(JmmNode node, Symbol expected, AnalysisSemanticInfo analysisSemanticInfo) {
        if(node.getKind().equals("IntegerLiteral")) return new Type("int", false);
        else if(node.getKind().equals("True")) return new Type("boolean", false);
        else if(node.getKind().equals("False")) return new Type("boolean", false);
        else if(node.getKind().equals("Var")) {
            Type type = this.getVariableTypeFromNodeInClass(node, analysisSemanticInfo);
            return (type == null) ? expected.getType() : type;
        }
        else if(node.getKind().equals("This")) return new Type(analysisSemanticInfo.getClassName(), false);
        else if(node.getKind().equals("ConstructorIntArray")) return new Type("int", true);
        else if(node.getKind().equals("ConstructorClass")) return new Type(node.get("val"), false);
        else if(node.getKind().equals("And")) return new Type("boolean", false);
        else if(node.getKind().equals("Less")) return new Type("boolean", false);
        else if(node.getKind().equals("PlusExpression")) return new Type("int", false);
        else if(node.getKind().equals("MinusExpression")) return new Type("int", false);
        else if(node.getKind().equals("MultExpression")) return new Type("int", false);
        else if(node.getKind().equals("DivExpression")) return new Type("int", false);
        else if(node.getKind().equals("NotExpression")) return new Type("boolean", false);
        else if(node.getKind().equals("Length")) return new Type("int", false);
        else if(node.getKind().equals("ArrayIndex")) return new Type("int", false);
        else if(node.getKind().equals("MethodInvocation")) {
            Type type = analysisSemanticInfo.getReturnType(node.get("val"));
            return (type == null) ? expected.getType() : type;
        }
        else if(node.getKind().equals("SubExpression")) return this.getTypeFromKind(node.getChildren().get(0), expected, analysisSemanticInfo);
        else return null;
    }

    /**
     * Returns the type of the variable in the class if exists
     * @param variable
     * @param analysisSemanticInfo
     * @return
     */
    private Type getVariableTypeFromNodeInClass(JmmNode variable, AnalysisSemanticInfo analysisSemanticInfo) {
        JmmNode methodOwner = this.findMethodOwnerFromVariable(variable);
        String methodName = (methodOwner.getKind().equals("Main")) ? "main" : methodOwner.get("val");
        
        // tries to get the variable type from the local variables
        Type type = this.getVariableTypeFromSymbols(variable, analysisSemanticInfo.getLocalVariables(methodName));

        // tries to get the variable type from the fields of the class
        if (type == null) type = this.getVariableTypeFromSymbols(variable, analysisSemanticInfo.getFields());

        return type;
    }

    /**
     * Returns the type of a node in a set of symbols
     * @param variable
     * @param symbols
     * @return
     */
    private Type getVariableTypeFromSymbols(JmmNode variable, List<Symbol> symbols) {
        Type type = null;

        if(symbols == null) return null;
        
        for(Symbol symbol: symbols) {
            if (symbol.getName().equals(variable.get("val"))) {
                type = symbol.getType();
                break;
            }
        }

        return type;
    }
    
    //     $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    //                  +++++++++++++++++++++++++             
    //     $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    public Boolean typeVerification(JmmNode node, AnalysisSemanticInfo analysisSemanticInfo) {
        // System.out.println("TO DO! TYPE VERIFICATION");
        
        return true;
    }
}
