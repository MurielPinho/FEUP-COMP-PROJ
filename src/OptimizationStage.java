import java.util.ArrayList;
import java.util.List;

import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.ast.examples.BranchCounter;
import pt.up.fe.comp.jmm.ollir.JmmOptimization;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.specs.util.SpecsIo;

import javax.xml.transform.Result;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */


public class OptimizationStage implements JmmOptimization {

    String temps = "";
    @Override
    public OllirResult toOllir(JmmSemanticsResult semanticsResult) {


        JmmNode node = semanticsResult.getRootNode();




        SymbolTable symbolTable = semanticsResult.getSymbolTable();

        // Convert the AST to a String containing the equivalent OLLIR code
        String ollirCode = generateOllirCode(node, symbolTable); // Convert node ...

        // More reports from this stage
        List<Report> reports = new ArrayList<>();

        return new OllirResult(semanticsResult, ollirCode, reports);
    }

    @Override
    public JmmSemanticsResult optimize(JmmSemanticsResult semanticsResult) {
        // THIS IS JUST FOR CHECKPOINT 3
        return semanticsResult;
    }

    @Override
    public OllirResult optimize(OllirResult ollirResult) {
        // THIS IS JUST FOR CHECKPOINT 3
        return ollirResult;
    }



/*
myClass {
    .construct myClass().V {
        invokespecial(this, "<init>").V;
    }

    .method public sum(A.array.i32).i32 {
        sum.i32 :=.i32 0.i32;
        i.i32 :=.i32 0.i32;

        Loop:
            t1.i32 :=.i32 arraylength($1.A.array.i32).i32;
            if (i.i32 >=.i32 t1.i32) goto End;
            t2.i32 :=.i32 $1.A[i.i32].i32;
            sum.i32 :=.i32 sum.i32 +.i32 t2.i32;
            i.i32 :=.i32 i.i32 +.i32 1.i32;
            goto Loop;
        End:
            ret.i32 sum.i32;


    }
}
 */


    String generateOllirCode(JmmNode root, SymbolTable symbolTable)
    {
        String result = "";

        List<JmmNode> nodes = root.getChildren();
        for(JmmNode node : nodes)
        {
            if(node.getKind().equals("Class"))
            {
                String class_name = node.get("val");
                result = class_name + " {\n\t.construct " + class_name + "().V {\n\t\tinvokespecial(this, \"<init>\").V;\n\t}";
                List<JmmNode> methods = node.getChildren();
                for(JmmNode method : methods)
                {
                    result += generateOllirMethodCode(method, symbolTable);
                }
                result += "}";
            }
        }

        return result;
    }

    String generateOllirMethodCode(JmmNode node, SymbolTable symbolTable)
    {

        String result = "";

        List<JmmNode> methodType = node.getChildren();

        for(JmmNode method : methodType)
        {
            if(method.getKind().equals("RegularMethod") || method.getKind().equals("Main"))
            {
                if(method.getKind().equals("RegularMethod"))
                {
                    result += ".method public " + method.get("val") + "(";
                }
                else if(method.getKind().equals("Main")){
                    result += ".method public static main(";
                }

                /*
                .method public static main(args.array.String).V {

                 */

                List<JmmNode> methodChildren = method.getChildren();

                String returnType="";
                String args = "";
                String body="";

                ArrayList<String> vars = null;

                for(JmmNode child : methodChildren)
                {
                    switch(child.getKind())
                    {
                        case "ReturnType":
                            String typeAux = child.getChildren().get(0).get("val");

                            if(typeAux.equals("int")){
                                returnType = ").i32 {";
                            }else if(typeAux.equals("boolean")){
                                returnType = ").bool {";
                            }
                            break;

                        case "MethodParams":
                            List<JmmNode> methodParams = child.getChildren();

                            int counter = 1;

                            for(JmmNode param : methodParams)
                            {
                                String arg = "";

                                String paramTypeAux = param.getChildren().get(0).get("val");
                                String paramVarAux = param.getChildren().get(1).get("val");

                                if(paramTypeAux.equals("int")){
                                    arg = "$" + counter + "." + paramVarAux + ".i32";
                                }else if(paramTypeAux.equals("boolean")){
                                    arg = "$" + counter + "." + paramVarAux + ".bool";
                                }else if(paramTypeAux.equals("int[]")){
                                    arg = "$" + counter + "." + paramVarAux + ".array.i32";
                                }

                                counter++;

                                if(counter <= methodParams.size()){
                                    arg += ",";
                                }

                                args += arg;
                            }
                            break;


                        case "MethodBody":
                            BranchCounter branch_counter = new BranchCounter();
                            vars = new ArrayList(Arrays.asList(args.split(",")));
                            body = generateOllirBodyCode(child, vars, branch_counter, symbolTable);
                            break;

                        case "ReturnStatement":

                            String statement = child.getChildren().get(0).get("val");
                            String statementType = searchArgs(statement,vars);
                            String[] aux_split = statementType.split("\\.");
                            String type = aux_split[aux_split.length-1];
                            result += "End:\n\tret." + type + " " + statementType + ";";
                            break;

                        case "ArgName" :
                            result += child.get("val") + ".array.String).V";
                            break;

                        default:
                            throw new IllegalStateException("Unexpected value: " + child.getKind());
                    }
                }

                result += args + returnType;
            }
        }

        return result + "\n\t}";
    }



    String generateOllirBodyCode (JmmNode body, ArrayList<String> args, BranchCounter branch_counter, SymbolTable symbolTable){

        System.out.println(body.toTree());
        List<JmmNode> methodBodyContents = body.getChildren();
        String method_body = "";

        String assignment_type = "";
        String var ="";

        for(JmmNode bodyContent : methodBodyContents){

            switch (bodyContent.getKind())
            {

                case "Var":
                    var = searchArgs(bodyContent.get("val"), args);
                    method_body += var;
                    String[] aux_split = var.split("\\.");
                    assignment_type = aux_split[aux_split.length-1];

                    break;

                case "Assignment":

                    String aux= generateOllirExpressionCode(bodyContent, args,branch_counter, symbolTable);

                    method_body += temps;
                    method_body += " :=." + assignment_type + " " + aux + ";";

                    temps = "";
                    break;

                case "VarDeclaration":
                    String new_vars = generateOllirVarDeclaration(bodyContent, args);
                    args.add(new_vars);
                    method_body += args;
                    break;

                case "While":
                    branch_counter.incrementWhile();
                    method_body += generateOllirWhileCode(bodyContent, args, branch_counter, symbolTable);
                    break;

                case "IfAndElse":

                    branch_counter.incrementIfElse();
                    method_body += generateOllirIfAndElseCode(bodyContent, args, branch_counter, symbolTable);
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + bodyContent.getKind());
            }



        }
        return method_body;
    }

    String generateOllirIfAndElseCode(JmmNode ifElse, ArrayList<String> args, BranchCounter branch_counter, SymbolTable symbolTable)
    {
        /*
        IfAndElse (val: null)
          IfExpression (val: null, col: 12, line: 44)
           Var (val: num, col: 13, line: 44)
           Less (val: null)
            IntegerLiteral (val: 1, col: 19, line: 44)
          IfBody (val: null)
           Var (val: num_aux, col: 13, line: 45)
           Assignment (val: null, col: 21, line: 45)
            IntegerLiteral (val: 1, col: 23, line: 45)
          ElseBody (val: null)
         */
        String result = "";
        List<JmmNode> ifElseContents = ifElse.getChildren();

        boolean else_exists = false;

        for(JmmNode content : ifElseContents)
        {
            else_exists = content.getKind().equals("ElseBody");
        }

        for(JmmNode content : ifElseContents)
        {
            switch(content.getKind())
            {
                case "IfExpression":

                    String aux = generateOllirExpressionCode(content, args,branch_counter, symbolTable);

                    result+= temps;
                    result = "";
                    result += "if (";

                    result += aux + ")";
                    if(else_exists)
                        result += " goto else";
                    else
                        result += " goto endif";

                    result += branch_counter + ";\n\t";
                    branch_counter.incrementIfElse();
                    break;

                case "IfBody":
                    result += generateOllirBodyCode(content, args, branch_counter, symbolTable) ;
                    break;

                case "ElseBody":
                    result += "goto endif"+branch_counter+";\nelse"+branch_counter + ":" + generateOllirBodyCode(content, args, branch_counter, symbolTable);
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + content.getKind());
            }
        }

        return result + "\nendif" + branch_counter +":";
    }


    String searchArgs(String var, ArrayList<String> args){
        String res ="";
        int length = var.length();

        for(String arg : args){
            String aux = arg.substring(0, length - 1);

            if(aux.equals(var)){
                res = arg;
            }
        }

        return res;
    }

    String genetrateOllirMethodArgs(JmmNode methodInvocation, ArrayList<String> args, BranchCounter branch_counter, SymbolTable symbolTable)
    {
        List<JmmNode> methodArgs = methodInvocation.getChildren();
        String result = "";
        Boolean aux = false;
        for(JmmNode methodArg : methodArgs)
        {
            if(aux)
                result += ", ";
            aux = true;
            result += generateOllirExpressionCode(methodArg, args, branch_counter, symbolTable);
        }
        return result;
    }


    String generateOllirExpressionCode(JmmNode node, ArrayList<String> args, BranchCounter branch_counter, SymbolTable symbolTable)
    {
        String result = "";
        List<JmmNode> contents = node.getChildren();

        String var = "";

        for(JmmNode content : contents)
        {
            switch(content.getKind())
            {

                case "MethodInvocation":
                    result += "invokevirtual(" + var + ", " + content.get("val");
                    result += genetrateOllirMethodArgs(content, args, branch_counter, symbolTable) + ")";

                    //System.out.println(content.get("val"));

                    List<String> methods = symbolTable.getMethods();

                    String finalMethod = "";

                    for (String method : methods){

                        String[] aux=method.split("\\d+",2);

                        String substringMethod = aux[0].trim();
                        if(content.get("val").equals(substringMethod)){
                            finalMethod = method;
                        }
                    }

                    String returnType = symbolTable.getReturnType(finalMethod).getName() + (symbolTable.getReturnType(finalMethod).isArray()?"[]":"");

                    if(returnType.equals("int")){
                        result += ".i32";
                    }else if(returnType.equals("boolean")){
                        result += ".bool";
                    }else if(returnType.equals("int[]")){
                        result += ".array.i32";
                    }
                    else if(returnType.equals("void")) {
                        result += ".V";
                    }
                    break;

                case "Identifier":
                    result += searchArgs(content.get("val"), args);
                    break;

                case "Var":
                    var = searchArgs(content.get("val"), args);
                    result += var;
                    break;

                case "IntegerLiteral":
                    result += searchArgs(content.get("val"), args);
                    break;

                case "This":
                    result += " invokevirtual(this, " + content.get("val") + ")"; // invokevirtual ??
                    break;

                case "And":
                    result += " &&.bool" + generateOllirExpressionCode(content, args, branch_counter, symbolTable);
                    break;

                case "Less":
                    if(content.getNumChildren() == 1){
                    result += " >=.i32" + generateOllirExpressionCode(content, args, branch_counter, symbolTable);
                    }else{
                        result += " >=.i32";

                        branch_counter.incrementTemp();
                        String temp = "temp" + branch_counter;

                        result += temp;

                        temp += " = ";
                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        temp += ";\n";

                        temps += temp;
                    }
                    break;

                case "PlusExpression":

                    if(content.getNumChildren() == 1){
                        result += " +.i32" + generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                    }else{
                        result += " +.i32";

                        branch_counter.incrementTemp();
                        String temp = "temp" + branch_counter;

                        result += temp;

                        temp += " = ";
                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        temp += ";\n";

                        temps += temp;
                    }

                    break;

                case "MinusExpression":
                    if(content.getNumChildren() == 1){
                    result += " -.i32" + generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                    }else{
                        result += " -.i32";

                        branch_counter.incrementTemp();
                        String temp = "temp" + branch_counter;

                        result += temp;

                        temp += " = ";
                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        temp += ";\n";

                        temps += temp;
                    }

                    break;

                case "MultExpression":
                    if(content.getNumChildren() == 1){
                    result += " *.i32" + generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                    }else{
                        result += " *.i32";

                        branch_counter.incrementTemp();
                        String temp = "temp" + branch_counter;

                        result += temp;

                        temp += " = ";
                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        temp += ";\n";

                        temps += temp;
                    }

                    break;

                case "DivExpression":
                    if(content.getNumChildren() == 1){
                    result += " /.i32" + generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                    }else{
                        result += " /.i32";

                        branch_counter.incrementTemp();
                        String temp = "temp" + branch_counter;

                        result += temp;

                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        temp += ";\n";

                        temps += temp;
                    }

                    break;

                case "NotExpression":
                    result += " !.bool" + generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                    break;

                case "SubExpression":

                    branch_counter.incrementTemp();
                    String temp = "temp" + branch_counter;

                    result += temp;

                    temp += " = ";
                    temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                    temp += ";\n";

                    temps += temp;

                    break;

                case "Length":
                    result += ".length()";
                    break;

                /*case "ArrayIndex":

                    break;*/


                default:
                    throw new IllegalStateException("Unexpected value: " + content.getKind());
            }
        }
        return result;
    }


    String generateOllirVarDeclaration(JmmNode vars, ArrayList<String> args){

        String varDeclaration="";

        String varTypeAux = vars.getChildren().get(0).get("val");
        String varAux = vars.getChildren().get(1).get("val");

        if(varTypeAux == "int"){
            varDeclaration += varAux + ".i32;";
        }else if(varTypeAux == "boolean"){
            varDeclaration += varAux + ".bool;";
        }else if(varTypeAux == "int[]"){
            varDeclaration += varAux + ".array.i32;";
        }

        args.add(varDeclaration);

        return varDeclaration;
    }

    String generateOllirWhileCode(JmmNode vars, ArrayList<String> args, BranchCounter branch_counter, SymbolTable symbolTable){

        List<JmmNode> whileContent = vars.getChildren();
        String ollirWhile="";

        for(JmmNode content : whileContent){
            if(content.equals("WhileExpression")){

                String condition = generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                ollirWhile += temps;
                temps = "";
                ollirWhile += "Loop " + branch_counter + ":\n\t if (" + condition + ") goto Body" + branch_counter + ";\ngoto EndLoop " + branch_counter + "; \nBody"+branch_counter+":\n\t";

            }else if(content.equals("WhileBody")){
                List<JmmNode> bodyContent = content.getChildren();

                for(JmmNode insideContent : bodyContent) {

                    if (insideContent.equals("Scope")) {
                        ollirWhile += generateOllirBodyCode(insideContent,args, branch_counter, symbolTable);
                    }
                }
            }
        }

        return ollirWhile + "EndLoop" + branch_counter+ ":\n\t";
    }


}




/*
Program (val: null)
 Imports (val: null)
  ImportDeclaration (val: io.jgh.op.i, col: 8, line: 1)
  ImportDeclaration (val: a, col: 8, line: 2)
  ImportDeclaration (val: a.Fac1.b, col: 8, line: 3)

 Class (val: Fac B, col: 7, line: 4)

  MethodDeclaration (val: null)
   RegularMethod (val: ComputeFac1, col: 16, line: 5)
    ReturnType (val: null)
     Type (val: int, col: 12, line: 5)
    MethodParams (val: null)
     MethodParam (val: null)
      Type (val: int, col: 27, line: 5)
      VarId (val: num, col: 31, line: 5)
     MethodParam (val: null)
      Type (val: boolean, col: 36, line: 5)
      VarId (val: a, col: 44, line: 5)
     MethodParam (val: null)
      Type (val: int[], col: 47, line: 5)
      VarId (val: c34d, col: 53, line: 5)
    MethodBody (val: null)
     VarDeclaration (val: null)
      Type (val: int, col: 9, line: 6)
      VarId (val: num_aux, col: 13, line: 6)
     VarDeclaration (val: null)
      Type (val: ab, col: 9, line: 7)
      VarId (val: a, col: 12, line: 7)
     While (val: null)
      WhileBody (val: null)
       Scope (val: null)
        Var (val: a, col: 13, line: 9)
        Assignment (val: null, col: 14, line: 9)
         IntegerLiteral (val: 1, col: 15, line: 9)
     While (val: null)
      WhileExpression (val: null, col: 14, line: 11)
       Var (val: a, col: 15, line: 11)
       Less (val: null)
        IntegerLiteral (val: 1, col: 19, line: 11)
      WhileBody (val: null)
       Scope (val: null)
        Var (val: a, col: 13, line: 12)
        Assignment (val: null, col: 15, line: 12)
         Var (val: a, col: 17, line: 12)
         PlusExpression (val: null)
          IntegerLiteral (val: 1, col: 21, line: 12)
     IfAndElse (val: null)
      IfExpression (val: null, col: 12, line: 44)
       Var (val: num, col: 13, line: 44)
       Less (val: null)
        IntegerLiteral (val: 1, col: 19, line: 44)
      IfBody (val: null)
       Var (val: num_aux, col: 13, line: 45)
       Assignment (val: null, col: 21, line: 45)
        IntegerLiteral (val: 1, col: 23, line: 45)
      ElseBody (val: null)
       Var (val: num_aux, col: 13, line: 47)
       Assignment (val: null, col: 21, line: 47)
        IntegerLiteral (val: 1, col: 23, line: 47)
        MultExpression (val: null)
         SubExpression (val: null)
          This (val: null)
          MethodInvocation (val: ComputeFac, col: 33, line: 47)
           MethodArgs (val: null)
            MethodArg (val: null)
             Var (val: num, col: 44, line: 47)
             MinusExpression (val: null)
              IntegerLiteral (val: 1, col: 50, line: 47)
            MethodArg (val: null)
             False (val: null)
            MethodArg (val: null)
             ConstructorClass (val: Fac, col: 64, line: 47)
             MethodInvocation (val: b, col: 70, line: 47)
              MethodArgs (val: null)
             MethodInvocation (val: f, col: 74, line: 47)
              MethodArgs (val: null)
          PlusExpression (val: null)
           SubExpression (val: null)
            SubExpression (val: null)
             SubExpression (val: null)
              IntegerLiteral (val: 7, col: 84, line: 47)
        PlusExpression (val: null)
         IntegerLiteral (val: 5, col: 92, line: 47)
        Less (val: null)
         IntegerLiteral (val: 1, col: 96, line: 47)
    ReturnStatement (val: null)
     Var (val: num_aux, col: 16, line: 52)

  MethodDeclaration (val: null)
   RegularMethod (val: b1, col: 16, line: 55)
    ReturnType (val: null)
     Type (val: int, col: 12, line: 55)
    MethodBody (val: null)
    ReturnStatement (val: null)
     IntegerLiteral (val: 1, col: 16, line: 56)

  MethodDeclaration (val: null)
   Main (val: null)
    ArgName (val: args, col: 38, line: 59)
    MethodBody (val: null)
     VarDeclaration (val: null)
      Type (val: A, col: 9, line: 60)
      VarId (val: b, col: 11, line: 60)
     Var (val: io, col: 9, line: 61)
     MethodInvocation (val: println, col: 12, line: 61)
      MethodArgs (val: null)
       MethodArg (val: null)
        ConstructorClass (val: Fac, col: 24, line: 61)
        MethodInvocation (val: ComputeFac, col: 30, line: 61)
         MethodArgs (val: null)
          MethodArg (val: null)
           IntegerLiteral (val: 10, col: 41, line: 61)
          MethodArg (val: null)
           Var (val: b, col: 45, line: 61)
          MethodArg (val: null)
           Var (val: a, col: 48, line: 61)
           ArrayIndex (val: null, col: 49, line: 61)
            IntegerLiteral (val: 4, col: 50, line: 61)
           PlusExpression (val: null)
            IntegerLiteral (val: 7, col: 55, line: 61)
     ConstructorClass (val: Fac1, col: 13, line: 62)
     MethodInvocation (val: ab, col: 20, line: 62)
      MethodArgs (val: null)
       MethodArg (val: null)
        Var (val: b, col: 23, line: 62)

 */



















