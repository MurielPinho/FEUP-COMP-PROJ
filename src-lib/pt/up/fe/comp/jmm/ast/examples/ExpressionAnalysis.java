package pt.up.fe.comp.jmm.ast.examples;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;

public class ExpressionAnalysis {
    private AnalysisSemanticVisitor analysisSemanticVisitor;
    private AnalysisSemanticInfo analysisSemanticInfo;

    private int line;

    public ExpressionAnalysis(AnalysisSemanticVisitor analysisSemanticVisitor, AnalysisSemanticInfo analysisSemanticInfo) {
        this.analysisSemanticVisitor = analysisSemanticVisitor;
        this.analysisSemanticInfo = analysisSemanticInfo;

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

    private String getTypeStringOfSimpleOperand(JmmNode node) {
        if (node.getKind().equals("True") || node.getKind().equals("False")) return "boolean";
        else if(node.getKind().equals("IntegerLiteral")) return "int";
        else if(node.getKind().equals("This")) return analysisSemanticInfo.getClassName();
        else if(node.getKind().equals("ConstructorIntArray")) return "int[]";
        else if(node.getKind().equals("ConstructorClass")) return node.get("val");
        else if(node.getKind().equals("Var")) {
            Type type = this.analysisSemanticVisitor.getVariableTypeFromNodeInClass(node, analysisSemanticInfo);

            if (type == null) {
                this.analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Undeclared variable " + node.get("val") + "!"));
                return null;
            }

            return this.fromTypeToString(type);
        }

        return null;
    }

    private String getTypeStringOfCompoundOperand(JmmNode node) {
        if (node.getKind().equals("Length") || node.getKind().equals("ArrayIndex")) return "int";
        else {
            Type retType = this.analysisSemanticVisitor.getReturnTypeOfMethod(node, analysisSemanticInfo);
            return (retType == null ? "null" : this.fromTypeToString(retType));
        }
    }

    private int getNumNodesToSkip(List<JmmNode> children, int start) {
        int i = 0;
        
        while(children.get(start - i).getKind().equals("MethodInvocation")
            ||children.get(start - i).getKind().equals("ArrayIndex")
            ||children.get(start - i).getKind().equals("Length")) i++;

        return i;
    }

    private String getOperatorSymbol(JmmNode node) {
        if (node.getKind().equals("And")) return "&&";
        else if (node.getKind().equals("Less")) return "<";
        else if (node.getKind().equals("PlusExpression")) return "+";
        else if (node.getKind().equals("MinusExpression")) return "-";
        else if (node.getKind().equals("MultExpression")) return "*";
        else if (node.getKind().equals("DivExpression")) return "/";
        else return "!";
    }

    private String buildStringExpressionFromList(List<String> parts, int ini, int fin) {
        String ret = "";
        
        for (int i = ini; i <= fin; i++)
            ret += parts.get(i) + " ";

        return ret;
    }

    private int[] findSubExpressionIndex(String expression) {
        int ini = -1, fin = -1, cont = 0;
        int[] ret = {ini, fin};

        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                if (ini == -1) ini = i; 
                cont++;
            }
            else if (expression.charAt(i) == ')') {
                cont--;
                if (cont == 0) {
                    fin = i;
                    ret[0] = ini; ret[1] = fin;
                    return ret;
                }
            }
        }

        return ret;
    }

    private List<String> mySplit(String expression) {
        List<String> ret = new ArrayList<>();
        String word = "";

        for(int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) != ' ') word += String.valueOf(expression.charAt(i));
            else {
                if (!word.equals("")) ret.add(word);
                word = "";
            }
        }

        if (!word.equals("")) ret.add(word);

        return ret;
    }

    private String fromTypeToString(Type type) {
        return type.getName() + (type.isArray() ? "[]" : "");
    }

    // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    // $$$$$$$$$$$$$$$$$$ - MAIN PROCEDURES - $$$$$$$$$$$$$$$$$$$$$$
    // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    /**
     * Goes through an expression and tries to find a subexpression "(...)" and if finds one it will process it
     * @param expression
     * @return result of processing the expression
     */
    private String processSubExpression(String expression) {
        String curr = expression, aux = "";
        int[] ind  = {};

        ind = this.findSubExpressionIndex(curr);
        while(ind[0] != -1 && ind[1] != -1) {
            aux = this.processStringExpression(curr.substring(ind[0] + 1, ind[1]));
            curr = curr.substring(0, ind[0]) + aux + curr.substring(ind[1] + 1, curr.length());
            ind = this.findSubExpressionIndex(curr);
        }

        return curr;
    }

    /**
     * Goes through an expression and tries to find a notexpression "!..." and if finds one it will process it
     * @param expression
     * @return result of processing the expression
     */
    private String processNotExpression(String expression) {
        String curr = expression;
        List<String> parts = this.mySplit(curr);
        int ind  = -1;

        parts.indexOf("!");

        ind = parts.indexOf("!");
        while(ind != -1) {
            if (!parts.get(ind+1).equals("boolean") && !parts.get(ind+1).equals("null")) {
                this.analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Invalid operand: '" + parts.get(ind+1) + "' for operator '!'"));
                return null;
            }

            curr = this.buildStringExpressionFromList(parts, 0, ind - 1) + " boolean " + this.buildStringExpressionFromList(parts, ind + 2, parts.size() - 1);
            parts = this.mySplit(curr);
            ind = parts.indexOf("!");
        }

        return curr;
    }

    /**
     * Goes through an expression and tries to find a mult or a divexpression "...*..." ou ".../..." and if finds one it will process it
     * @param expression
     * @return result of processing the expression
     */
    private String processMultDivExpression(String expression) {
        String curr = expression;
        String op = "";
        List<String> parts = this.mySplit(curr);
        int ind  = -1;

        int multInd = curr.indexOf("*");
        int divInd = curr.indexOf("/");

        if (multInd != -1 && divInd != -1) {
            if (multInd < divInd) op = "*";
            else op = "/";
        }
        else if(multInd != -1) op = "*";
        else if(divInd != -1) op = "/";

        while(!op.equals("")) {
            ind = parts.indexOf(op);

            if (!parts.get(ind+1).equals("int") && !parts.get(ind+1).equals("null")) {
                this.analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Invalid operand: '" + parts.get(ind+1) + "' for operator '" + op + "'"));
                return null;
            }

            if (!parts.get(ind-1).equals("int") && !parts.get(ind-1).equals("null")) {
                this.analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Invalid operand: '" + parts.get(ind-1) + "' for operator '" + op + "'"));
                return null;
            }

            curr = this.buildStringExpressionFromList(parts, 0, ind - 2) + " int " + this.buildStringExpressionFromList(parts, ind + 2, parts.size() - 1);
            parts = this.mySplit(curr);

            op = "";

            multInd = curr.indexOf("*");
            divInd = curr.indexOf("/");

            if (multInd != -1 && divInd != -1) {
                if (multInd < divInd) op = "*";
                else op = "/";
            }
            else if(multInd != -1) op = "*";
            else if(divInd != -1) op = "/";
        }

        return curr;
    }

    /**
     * Goes through an expression and tries to find a plus or a minusexpression "...+..." ou "...-..." and if finds one it will process it
     * @param expression
     * @return result of processing the expression
     */
    private String processPlusMinusExpression(String expression) {
        String curr = expression;
        String op = "";
        List<String> parts = this.mySplit(curr);
        int ind  = -1;

        int plusInd = curr.indexOf("+");
        int minusInd = curr.indexOf("-");

        if (plusInd != -1 && minusInd != -1) {
            if (plusInd < minusInd) op = "+";
            else op = "-";
        }
        else if(plusInd != -1) op = "+";
        else if(minusInd != -1) op = "-";

        while(!op.equals("")) {
            ind = parts.indexOf(op);

            if (!parts.get(ind+1).equals("int") && !parts.get(ind+1).equals("null")) {
                this.analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Invalid operand: '" + parts.get(ind+1) + "' for operator '" + op + "'"));
                return null;
            }

            if (!parts.get(ind-1).equals("int") && !parts.get(ind-1).equals("null")) {
                this.analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Invalid operand: '" + parts.get(ind-1) + "' for operator '" + op + "'"));
                return null;
            }

            curr = this.buildStringExpressionFromList(parts, 0, ind - 2) + " int " + this.buildStringExpressionFromList(parts, ind + 2, parts.size() - 1);
            parts = this.mySplit(curr);

            op = "";

            plusInd = curr.indexOf("+");
            minusInd = curr.indexOf("-");

            if (plusInd != -1 && minusInd != -1) {
                if (plusInd < minusInd) op = "+";
                else op = "-";
            }
            else if(plusInd != -1) op = "+";
            else if(minusInd != -1) op = "-";
        }

        return curr;
    }

    /**
     * Goes through an expression and tries to find a lessexpression "...<..." and if finds one it will process it
     * @param expression
     * @return result of processing the expression
     */
    private String processLessExpression(String expression) {
        String curr = expression;
        List<String> parts = this.mySplit(curr);

        int ind = parts.indexOf("<");

        while(ind != -1) {
            if (!parts.get(ind+1).equals("int") && !parts.get(ind+1).equals("null")) {
                this.analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Invalid operand: '" + parts.get(ind+1) + "' for operator '<'"));
                return null;
            }

            if (!parts.get(ind-1).equals("int") && !parts.get(ind-1).equals("null")) {
                this.analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Invalid operand: '" + parts.get(ind-1) + "' for operator '<'"));
                return null;
            }

            curr = this.buildStringExpressionFromList(parts, 0, ind - 2) + " boolean " + this.buildStringExpressionFromList(parts, ind + 2, parts.size() - 1);
            parts = this.mySplit(curr);
            ind = parts.indexOf("<");
        }

        return curr;
    }

    /**
     * Goes through an expression and tries to find a andexpression "...&&..." and if finds one it will process it
     * @param expression
     * @return result of processing the expression
     */
    private String processAndExpression(String expression) {
        String curr = expression;
        List<String> parts = this.mySplit(curr);

        int ind = parts.indexOf("&&");

        while(ind != -1) {
            if (!parts.get(ind+1).equals("boolean") && !parts.get(ind+1).equals("null")) {
                this.analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Invalid operand: '" + parts.get(ind+1) + "' for operator '&&'"));
                return null;
            }

            if (!parts.get(ind-1).equals("boolean") && !parts.get(ind-1).equals("null")) {
                this.analysisSemanticInfo.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC, this.line, 0, "Invalid operand: '" + parts.get(ind-1) + "' for operator '&&'"));
                return null;
            }

            curr = this.buildStringExpressionFromList(parts, 0, ind - 2) + " boolean " + this.buildStringExpressionFromList(parts, ind + 2, parts.size() - 1);
            parts = this.mySplit(curr);
            ind = parts.indexOf("&&");
        }

        return curr;
    }

    /**
     * Goes through an expression and processes it respecting the operator precedence
     * @param expression
     * @return result of processing the expression
     */
    private String processStringExpression(String expression) {
        if (expression == null) return null;
        expression = this.processSubExpression(expression);
        if (expression == null) return null;
        expression = this.processNotExpression(expression);
        if (expression == null) return null;
        expression = this.processMultDivExpression(expression);
        if (expression == null) return null;
        expression = this.processPlusMinusExpression(expression);
        if (expression == null) return null;
        expression = this.processLessExpression(expression);
        if (expression == null) return null;
        expression = this.processAndExpression(expression);

        return expression;
    }

    /**
     * Builds a string that represents the expression
     * Ex: int + int < int && boolean && !boolean 
     * @param expression
     * @return the string expression
     */
    private String buildExpressionString(JmmNode expression) {
        String ret = "";

        List<JmmNode> children = expression.getChildren();
        int ind = children.size() - 1;

        while(ind >= 0) {
            JmmNode child = children.get(ind--);
        
            if (this.isOperand(child)) {
                String type = null;

                // it's an compund operand
                if (this.isCompundOperand(child)) {
                    ind = ind - this.getNumNodesToSkip(children, ind + 1);
                    type = this.getTypeStringOfCompoundOperand(child);
                }
                // it's a simple operand
                else {
                    type = this.getTypeStringOfSimpleOperand(child);
                    if (type == null) return null;
                }

                ret = type + " " + ret;
            }   
            // it's a subexpression so it's treated as an expression
            else if (child.getKind().equals("SubExpression")) {
                String type = this.buildExpressionString(child);
                if (type == null) return null;

                ret = " ( " + type + " ) " + ret;
            } 
            else {
                String type = this.buildExpressionString(child);
                if (type == null) return null;

                ret = this.getOperatorSymbol(child) + " " + type + " " + ret;
            }
            
        }

        return ret;
    }
    
    /**
     * Goes through the nodes of expression and builds a string containing the type and operator and then processes it
     * Ex: int + int < int && boolean && !boolean 
     * @param expression
     * @return result of the expression processed
     */
    public String analyse(JmmNode expression) {
        this.line = Integer.parseInt(expression.get("line"));
        String exp = this.buildExpressionString(expression);
        String ret = this.processStringExpression(exp);
        return (ret != null ? ret.trim() : null);
    }

}
