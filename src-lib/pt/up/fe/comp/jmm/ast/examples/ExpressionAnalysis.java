package pt.up.fe.comp.jmm.ast.examples;

import java.util.List;

import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;

public class ExpressionAnalysis {
    private AnalysisSemanticVisitor analysisSemanticVisitor;
    private AnalysisSemanticInfo analysisSemanticInfo;

    private Type value;
    private JmmNode operator;

    private int line;

    public ExpressionAnalysis(AnalysisSemanticVisitor analysisSemanticVisitor, AnalysisSemanticInfo analysisSemanticInfo) {
        this.analysisSemanticVisitor = analysisSemanticVisitor;
        this.analysisSemanticInfo = analysisSemanticInfo;

        this.value = null;
        this.operator = null;

        this.line = -1;
    }

    private Boolean isOperand(JmmNode node) {
        return node.getKind().equals("True") || node.getKind().equals("False") || node.getKind().equals("IntegerLiteral") 
            || node.getKind().equals("This") || node.getKind().equals("ConstructorIntArray") || node.getKind().equals("Length")
            || node.getKind().equals("ConstructorClass") || node.getKind().equals("Var") || node.getKind().equals("ArrayIndex")
            || node.getKind().equals("MethodInvocation");
    }

    private Boolean isCompundOperand(JmmNode node) {
        return node.getKind().equals("MethodInvocation") || node.getKind().equals("ArrayIndex") || node.getKind().equals("Length");
    }

    private Type getTypeOfSimpleOperand(JmmNode node) {
        if (node.getKind().equals("True") || node.getKind().equals("False")) return new Type("boolean", false);
        else if(node.getKind().equals("IntegerLiteral")) return new Type("int", false);
        else if(node.getKind().equals("This")) return new Type(analysisSemanticInfo.getClassName(), false);
        else if(node.getKind().equals("ConstructorIntArray")) return new Type("int", true);
        else if(node.getKind().equals("ConstructorClass")) return new Type(node.get("val"), false);
        else if(node.getKind().equals("Var")) {
            Type type = this.analysisSemanticVisitor.getVariableTypeFromNodeInClass(node, analysisSemanticInfo);

            if (type == null) {
                this.analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Undeclared variable " + node.get("val") + "!"));
                return null;
            }

            return type;
        }

        return null;
    }

    private Type getTypeOfCompoundOperand(JmmNode node) {
        if (node.getKind().equals("Length") || node.getKind().equals("ArrayIndex")) return new Type("int", false);
        else return this.analysisSemanticVisitor.getReturnTypeOfMethod(node, analysisSemanticInfo);
    }

    private int getNumNodesToSkip(List<JmmNode> children, int start) {
        int i = 0;
        
        while(children.get(start - i).getKind().equals("MethodInvocation")
            ||children.get(start - i).getKind().equals("ArrayIndex")
            ||children.get(start - i).getKind().equals("Length")) i++;

        return i;
    }

    private Type getArgTypeForOperator(JmmNode operator) {
        if (operator.getKind().equals("And")) return new Type("boolean", false);
        else return new Type("int", false);
    }

    private Type getRetTypeOfOperator(JmmNode operator) {
        if (operator.getKind().equals("And") || operator.getKind().equals("Less")) return new Type("boolean", false);
        else return new Type("int", false);
    }

    private String getOperatorSymbol(JmmNode node) {
        if (node.getKind().equals("And")) return "&&";
        else if (node.getKind().equals("Less")) return "<";
        else if (node.getKind().equals("PlusExpression")) return "+";
        else if (node.getKind().equals("MinusExpression")) return "-";
        else if (node.getKind().equals("MultExpression")) return "*";
        else return "/";
    }

    private Boolean validOperandType(Type type, JmmNode node) {
        if (type != null && this.operator != null) {
            Type argType = this.getArgTypeForOperator(this.operator);
            if (!type.equals(argType)) {
                this.analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Operator " + this.getOperatorSymbol(this.operator) + " requires value of type " + argType.getName() + (argType.isArray() ? "[]" : "") + " but received: " + type.getName() + (type.isArray() ? "[]" : "") + " -> " + node));
                return false;
            }
        }

        if (this.operator == null) {
            this.value = type;
            this.operator = null;
        }
        else {
            this.value = this.getRetTypeOfOperator(this.operator);
            this.operator = null;
        }

        return true;
    }

    public Type analyse(JmmNode expression) {
        // this.line = 

        List<JmmNode> children = expression.getChildren();
        int ind = children.size() - 1;

        while(ind >= 0) {
            JmmNode child = children.get(ind--);

            // it's an operand
            if (this.isOperand(child)) {
                Type type = null;

                // it's an compund operand
                if (this.isCompundOperand(child)) {
                    ind = ind - this.getNumNodesToSkip(children, ind + 1);
                    type = this.getTypeOfCompoundOperand(child);
                }
                // it's a simple operand
                else {
                    type = this.getTypeOfSimpleOperand(child);

                    // an error has occured
                    if (type == null) return null;
                }

                if (!this.validOperandType(type, child)) return null;
            }
            // it's a subexpression so it's treated as an expression
            else if (child.getKind().equals("SubExpression")) {
                Type type = new ExpressionAnalysis(this.analysisSemanticVisitor, this.analysisSemanticInfo).analyse(child);

                if (!this.validOperandType(type, child)) return null;
            }
            // it's an operator
            else {
                if (this.analyse(child) == null) return null;

                if (child.getKind().equals("NotExpression")) {
                    // it can be the value that we want
                    if (this.value == null) {
                        this.value = new Type("boolean", false);
                        this.operator = null;
                    }
                    // the  value must be of type boolean
                    else if (!this.value.equals(new Type("boolean", false))) {
                        analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Operator ! requires value of type boolean but received: " + this.value.getName() + (this.value.isArray() ? "[]" : "")));
                        return null;
                    }
                }
                else {
                    Type argType = this.getArgTypeForOperator(child);

                    if (this.value != null && !this.value.equals(argType)) {
                        analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Operator " + this.getOperatorSymbol(child) + " requires value of type " + argType.getName() + (argType.isArray() ? "[]" : "") + " but received: " + this.value.getName() + (this.value.isArray() ? "[]" : "") + " -> " + child.getKind() + " -> " + argType));
                        return null;
                    }
                    else {
                        this.value = null;
                        this.operator = child;
                    }
                }
            }
        }

        return this.value;
    }


    
}
