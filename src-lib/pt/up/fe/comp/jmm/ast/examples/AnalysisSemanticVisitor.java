package pt.up.fe.comp.jmm.ast.examples;

import java.lang.Thread.State;
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
    // private Type argType = null;
    // private Type opRetType = null;
    // private JmmNode operator = null;
    private JmmNode operator = null;
    private Type value = null;

    public AnalysisSemanticVisitor() {
        addVisit("MethodInvocation", this::methodVerification);
        addVisit("Assignment", this::assignmentVerification);
        addVisit("ArrayIndex", this::arrayVerification);
        addVisit("ConstructorIntArray", this::arrayConstructorVerification);
        addVisit("IfExpression", this::ifExpressionVerification);
        addVisit("WhileExpression", this::whileExpressionVerification);
        addVisit("Length", this::lengthVerification);
    }

    // antes tem de ser: this, contrutor class ou identifier
    public Boolean methodVerification(JmmNode node, AnalysisSemanticInfo analysisSemanticInfo) {
        String methodName = node.get("val");
        this.line = Integer.parseInt(node.get("line"));
        
        JmmNode invoker = this.getMethodOwner(node);
        // we can only invoke a method if the owner of it is: a variable, this or a class constructor
        if(!(invoker.getKind().equals("This") || invoker.getKind().equals("Var") || invoker.getKind().equals("ConstructorClass") || invoker.getKind().equals("MethodInvocation"))) {
            analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Invalid invoker: " + (!invoker.get("val").equals("null") ? invoker.get("val") : invoker.getKind()) + " of method: " + node.get("val") +"!"));
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
        // case of the owner of the method that is being invoker is a method(b), Ex: a.b().c()
        else if(invoker.getKind().equals("MethodInvocation")) {
            // if type is null it means that the invoker method was not declared in this class, so it can return anything
            Type type = this.getReturnTypeOfMethod(invoker, analysisSemanticInfo);

            // it's a deifned type
            if (type != null) this.verifyIfTypeHasMethod(type, node, analysisSemanticInfo);
        }
        else { // case of the owner of the method that is being invoked is a variable
            // tries to get the variable type
            Type type = this.getVariableTypeFromNodeInClass(invoker, analysisSemanticInfo);

            // variable does not belong to the class neither to its methods, Ex: io.println(), io nao e atributo nem variavel local do metodo
            if (type == null) this.verifyIfClassIsImported(analysisSemanticInfo, invoker.get("val"), methodName);
            // it's a defined type
            else this.verifyIfTypeHasMethod(type, node, analysisSemanticInfo);
        }

        return true;
    }

    private boolean verifyIfTypeHasMethod(Type type, JmmNode method, AnalysisSemanticInfo analysisSemanticInfo) {
        // variable is type boolean or int
        if (type.getName().equals("int") || type.getName().equals("boolean")) {
            analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Method: " + method.get("val") + "! Type: " + type.getName() + (type.isArray() ? "[]" : "") + " has not methods!"));
            return false;   
        }
        // the type of the variable is the same as the class, Ex: a.b(), em que a é uma instancia de A
        else if(type.getName().equals(analysisSemanticInfo.getClassName())) return this.verifyIfMethodExistsInClass(method, analysisSemanticInfo);
        // the types of the variable and the class are diferents, Ex: d.b(), em que d nao é uma instancia de A
        else return this.verifyIfClassIsImported(analysisSemanticInfo, type.getName(), method.get("val"));
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

        // the method does not exist in the current class but exists on super
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
            Type type = this.getReturnTypeOfMethod(node, analysisSemanticInfo);
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
    public Type getVariableTypeFromNodeInClass(JmmNode variable, AnalysisSemanticInfo analysisSemanticInfo) {
        JmmNode methodOwner = this.findMethodOwnerFromVariable(variable);
        String methodName = (methodOwner.getKind().equals("Main")) ? "main" : methodOwner.get("val");

        // tries to get the variable type from the local variables
        Type type = this.getVariableTypeFromSymbols(variable, analysisSemanticInfo.getLocalVariables(methodName));

        // tries to get the variable from the parameters
        if (type == null) type = this.getVariableTypeFromSymbols(variable, analysisSemanticInfo.getMethodParameters(methodName));

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
    
    /**
     * Looks for the method and returns the type that it's returning
     * @param method
     * @param analysisSemanticInfo
     * @return type of returning or null if it does not exist
     */
    public Type getReturnTypeOfMethod(JmmNode method, AnalysisSemanticInfo analysisSemanticInfo) {
        String methodName = method.get("val");
        List<String> methods = analysisSemanticInfo.getMethods();

        // it goes through all methods that have the same name
        for(String aux: methods) {  
            if(aux.substring(0, aux.length() - 1).equals(methodName)) {
                List<Symbol> params = analysisSemanticInfo.getMethodParameters(aux);

                // the method declaration and the method invocation have diferent number of args
                if(params.size() != this.getNumArgs(method)) continue;

                if (params.size() == 0) return analysisSemanticInfo.getReturnType(aux);

                // verifies if all the elements are of the correct type, if so it returns null
                if(validArgs(method, params, analysisSemanticInfo) == null) return analysisSemanticInfo.getReturnType(aux);
            }
        }

        return null;
    }

    //     $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    //                  +++++++++++++++++++++++++             
    //     $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    private Type getTypeFromString(String type) {
        int num = type.indexOf("[");
        if (num != -1) return new Type(type.substring(0, num), true);
        else return new Type(type, false);
    }

    public Boolean assignmentVerification(JmmNode node, AnalysisSemanticInfo analysisSemanticInfo) {
        this.line = Integer.parseInt(node.get("line"));
        Type assignedType = null;
        Type assigneeType = null;
        
        List<JmmNode> children = node.getParent().getChildren();
        for(int i = children.size() - 1; i > -1; i--) {
            if (children.get(i) == node) {
                if (children.get(i-1).getKind().equals("ArrayIndex")) {
                    assignedType = this.getVariableTypeFromNodeInClass(children.get(i-2), analysisSemanticInfo);
                    assignedType = new Type(assignedType.getName(), false);
                }
                else assignedType = this.getVariableTypeFromNodeInClass(children.get(i-1), analysisSemanticInfo);

                if (assignedType == null) {
                    analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Variable: " + (children.get(i-1).getKind().equals("ArrayIndex") ? children.get(i-2).get("val") : children.get(i-1).get("val")) + " was not declared!"));
                    return false;
                }

                break;
            }
        } 

        String type = new ExpressionAnalysis(this, analysisSemanticInfo).analyse(node);

        if (type != null && !type.equals("null")) {
            assigneeType = this.getTypeFromString(type);
            if (!assigneeType.equals(assignedType)) {
                analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Invalid assignment! Required: " + assignedType.getName() + (assignedType.isArray() ? "[]" : "") +  ", given: " + assigneeType.getName() + (assigneeType.isArray() ? "[]" : "")));
                return false;
            }
        }

        return true;
    }

    public Boolean arrayVerification(JmmNode node, AnalysisSemanticInfo analysisSemanticInfo) {
        this.line = Integer.parseInt(node.get("line"));
        String type = new ExpressionAnalysis(this, analysisSemanticInfo).analyse(node);

        if (type != null && !type.equals("null") && !type.equals("int")) {
            analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Invalid Array Access! Required: int, Given: " + type));
            return false;
        }

        List<JmmNode> children = node.getParent().getChildren();
        Boolean find = false;
        for(int i = children.size() - 1; i > -1; i--) {
            if (children.get(i) == node) find = true;
            if (find) {
                if (children.get(i).getKind().equals("ArrayIndex")) continue;
                else if (children.get(i).getKind().equals("Var") || children.get(i).getKind().equals("MethodInvocation")) {
                    if (children.get(i).getKind().equals("Var")) {
                        Type type1 = this.getVariableTypeFromNodeInClass(children.get(i), analysisSemanticInfo);

                        if (type1 != null) {
                            if (!type1.isArray() || !type1.getName().equals("int")) {
                                analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Must be array type! Found variable: " + children.get(i).get("val") + " of type: " + type1.getName()));
                                return false;
                            }
                            // it's array Type
                            else return true;
                        }
                        else {
                            analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Must be array type! Found undeclared variable: " + children.get(i).get("val")));
                            return false;
                        }
                    }
                    else {
                        Type type1 = this.getReturnTypeOfMethod(children.get(i), analysisSemanticInfo);

                        if (type1 != null) {
                            if (!type1.isArray()) {
                                analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Must be array type! Found: " + type1.getName()));
                                return false;
                            }
                            // it's array Type
                            else return true;
                        }
                        // undefined method
                        else return true;
                    }
                }
                else {
                    analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Must be array type! Found: " + (children.get(i).get("val") != null ? children.get(i).get("val") : children.get(i).getKind())));
                    return false;
                }
            }
        }

        return true;
    }

    public Boolean arrayConstructorVerification(JmmNode node, AnalysisSemanticInfo analysisSemanticInfo) {
        this.line = Integer.parseInt(node.get("line"));
        String initializer = new ExpressionAnalysis(this, analysisSemanticInfo).analyse(node);
        
        if (initializer != null && !initializer.equals("null") && !initializer.equals("int")) {
            analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Array size must be of type int but " + initializer + " was given!"));
            return false;
        }
        
        return true;
    }

    public Boolean ifExpressionVerification(JmmNode node, AnalysisSemanticInfo analysisSemanticInfo) {
        this.line = Integer.parseInt(node.get("line"));
        String initializer = new ExpressionAnalysis(this, analysisSemanticInfo).analyse(node);
        
        if (initializer != null && !initializer.equals("null") && !initializer.equals("boolean")) {
            analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Array size must be of type int but " + initializer + " was given!"));
            return false;
        }
        
        return true;
    }

    public Boolean whileExpressionVerification(JmmNode node, AnalysisSemanticInfo analysisSemanticInfo) {
        this.line = Integer.parseInt(node.get("line"));
        String initializer = new ExpressionAnalysis(this, analysisSemanticInfo).analyse(node);
        
        if (initializer != null && !initializer.equals("null") && !initializer.equals("boolean")) {
            analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Array size must be of type int but " + initializer + " was given!"));
            return false;
        }
        
        return true;
    }

    public Boolean lengthVerification(JmmNode node, AnalysisSemanticInfo analysisSemanticInfo) {
        this.line = Integer.parseInt(node.get("line"));
        List<JmmNode> children = node.getParent().getChildren();

        for(int i = children.size() - 1; i > -1; i--) {
            if (children.get(i) == node) {
                if (!children.get(i-1).getKind().equals("Var") && !children.get(i-1).getKind().equals("MethodInvocation")) {
                    analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Invoker of length must be a variable of type array!"));
                    return false;
                }
                else {
                    Type type = null;

                    if (children.get(i-1).getKind().equals("Var")) type = this.getVariableTypeFromNodeInClass(children.get(i-1), analysisSemanticInfo);
                    else type = this.getReturnTypeOfMethod(children.get(i-1), analysisSemanticInfo);

                    if (type != null) {
                        if (!type.isArray()) analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Invoker of length must be a variable of type array"));
                        return false;
                    }
                    else {
                        if (children.get(i-1).getKind().equals("Var")) {
                            analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Variable: " + children.get(i-1).get("val") + " was not declared!"));
                            return false;
                        }
                    }

                    return true;
                }
            }
        }
        
        return true;
    }
}
