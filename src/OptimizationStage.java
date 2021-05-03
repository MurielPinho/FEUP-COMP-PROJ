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
    ArrayList<String> fields = new ArrayList<>();
    @Override
    public OllirResult toOllir(JmmSemanticsResult semanticsResult) {


        JmmNode node = semanticsResult.getRootNode();

        SymbolTable symbolTable = semanticsResult.getSymbolTable();

        // Convert the AST to a String containing the equivalent OLLIR code
        String ollirCode = generateOllirCode(node, symbolTable); // Convert node ...

        System.out.println(ollirCode);

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
                result += class_name + " {";
                String constructor = "\n\t.construct " + class_name + "().V {\n\t\tinvokespecial(this, \"<init>\").V;\n\t}";
                result += "\n" + generateOllirClassCode(node, symbolTable, constructor);
            }
        }
        return result + "\n}";
    }

    String generateOllirClassCode(JmmNode node, SymbolTable symbolTable, String constructor)
    {

        String result = "";
        List<JmmNode> node_children = node.getChildren();

        boolean first = true;
        
        for(JmmNode child : node_children)
        {
            switch (child.getKind())
            {
                case "MethodDeclaration":
                    if(first)
                    {
                        result += constructor;
                        first = false;
                    }
                    result += generateOllirMethodCode(child, symbolTable);
                    break;
                case "VarDeclaration":
                    String new_vars = generateOllirVarDeclaration(child);

                    result += "\t.field private " + new_vars + ";";

                    if(!new_vars.equals("")) {
                        fields.add(new_vars);
                    }
                    break;

            }
        }
        return result;

    }

    String generateOllirMethodCode(JmmNode node, SymbolTable symbolTable)
    {
        String returnStatement="";
        String result = "";
        String body="";
        String methodDec="";
        List<JmmNode> methodType = node.getChildren();


        for(JmmNode method : methodType)
        {
            if(method.getKind().equals("RegularMethod") || method.getKind().equals("Main"))
            {
                if(method.getKind().equals("RegularMethod"))
                {

                    int length = method.get("val").length() - 1;

                    methodDec = method.get("val").substring(0,length);

                    result += "\n\t.method public " + methodDec + "(";


                }
                else if(method.getKind().equals("Main")){
                    result += "\n\t.method public static main(";
                }

                List<JmmNode> methodChildren = method.getChildren();
                String args = "";
                //ArrayList<String> args = new ArrayList<>();
                String returnType="";
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
                                String arg;
                                String paramTypeAux = param.getChildren().get(0).get("val");
                                String paramVarAux = param.getChildren().get(1).get("val");


                                if(paramTypeAux.equals("int")){
                                    arg = "$" + counter + "." + paramVarAux + ".i32";
                                }else if(paramTypeAux.equals("boolean")){
                                    arg = "$" + counter + "." + paramVarAux + ".bool";
                                }else if(paramTypeAux.equals("int[]")){
                                    arg = "$" + counter + "." + paramVarAux + ".array.i32";
                                } else {
                                    arg = "$" + counter + "." + paramVarAux + "." + paramTypeAux;
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
                            vars.remove("");
                            body = generateOllirBodyCode(child, vars, branch_counter, symbolTable);
                            break;

                        case "ReturnStatement":

                            String statement = child.getChildren().get(0).get("val");
                            String statementType = searchArgs(statement,vars,symbolTable);
                            String[] aux_split = statementType.split("\\.");
                            String type = aux_split[aux_split.length-1];
                            returnStatement += "\t\tret." + type + " " + statementType + ";";
                            break;

                        case "ArgName" :
                            result += child.get("val") + ".array.String).V {\n";
                            break;

                        default:
                            throw new IllegalStateException("Unexpected value: " + child.getKind());
                    }
                }
                //String formated_args = args.substring(1,args.toString().length()-1);
                String formated_args = args.replaceAll("\\$\\d+\\.", "");
                result += formated_args + returnType + "\n";
            }
        }

        return result + body + returnStatement + "\n\t}";
    }
    
    
    String generateOllirMethodInvocation(JmmNode methodInvocation, ArrayList<String> args, BranchCounter branch_counter, SymbolTable symbolTable, String var) {

        String result = "";

        List<String> methods = symbolTable.getMethods();
        String finalMethod = "";
        String returnType = "";
        String invoke = "";

        for (String method : methods){

            String substringMethod = method.substring(0,method.length()-1);
            if(methodInvocation.get("val").equals(substringMethod)){
                finalMethod =  "\"" + substringMethod + "\"";
                returnType = symbolTable.getReturnType(method).getName() + (symbolTable.getReturnType(method).isArray()?"[]":"");
                invoke = "invokevirtual";
          
            }
        }
        if(finalMethod.equals("")) {
            List<String> imports = symbolTable.getImports();
            if (imports.contains(var)) {
                finalMethod = "\"" + methodInvocation.get("val") + "\"";
                returnType=  "void";
                invoke = "invokestatic";
            }
        }

        result += invoke + "(" + var + ", " + finalMethod;
        result += genetrateOllirMethodArgs(methodInvocation, args, branch_counter, symbolTable) + ")";

        String aux_type = "";

        switch (returnType) {
            case "int":
                aux_type = ".i32";
                result += ".i32";
                break;
            case "boolean":
                aux_type = ".bool";
                result += ".bool";
                break;
            case "int[]":
                aux_type = ".array.i32";
                result += ".array.i32";
                break;
            case "void":
                aux_type = ".V";
                result += ".V" ;
                break;
        }

        if(!returnType.equals("void"))
        {
            branch_counter.incrementTemp();
            String aux_temp = "temp"+ branch_counter.getTemp_counter() + aux_type;

            String temp  = aux_temp + " :=" + aux_type + " ";
            temp += result;
            temp += ";\n";

            temps += "\t\t" + temp;
            return aux_temp;
        }

        result += ";" + "\n";

        return "\t\t" + result;
    }


    String generateOllirBodyCode (JmmNode body, ArrayList<String> args, BranchCounter branch_counter, SymbolTable symbolTable){

        List<JmmNode> methodBodyContents = body.getChildren();
        String method_body = "";

        String assignment_type = "";
        String var ="";

        for(int i = 0; i < methodBodyContents.size(); i++){
            JmmNode bodyContent = methodBodyContents.get(i);
            switch (bodyContent.getKind())
            {

                case "Var":
                    var = searchArgs(bodyContent.get("val"), args, symbolTable);
                    if(var.equals(""))
                    {
                        var = searchFields(bodyContent.get("val"));
                        if(!var.equals(""))
                        {
                            // getfield
                            if (bodyContent.equals(methodBodyContents.get(methodBodyContents.size()-1)) || (!methodBodyContents.get(i+1).getKind().equals("MethodInvocation") && !methodBodyContents.get(i+1).getKind().equals("Assignment")))
                            {
                                branch_counter.incrementTemp();
                                String aux_temp = "temp"+ branch_counter.getTemp_counter() + ".i32";

                                String temp  = aux_temp + "getfield(this, " + var + ").i32;\n";

                                temps += "\t\t" + temp;
                                method_body += var;
                            }
                            // putfield
                        }
                        else
                        {
                            method_body += "ERROR";
                        }
                    }
                    else
                    {
                        if (bodyContent.equals(methodBodyContents.get(methodBodyContents.size()-1)) || (!methodBodyContents.get(i+1).getKind().equals("MethodInvocation") && !methodBodyContents.get(i+1).getKind().equals("Assignment")))
                            method_body += var;
                        String[] aux_split = var.split("\\.");
                        assignment_type = aux_split[aux_split.length-1];
                    }



                    break;

                case "Assignment":

                    String aux= generateOllirExpressionCode(bodyContent, args,branch_counter, symbolTable);

                    method_body += temps;
                    if(searchFields(var).equals(""))
                    {
                        method_body += "\t\t" + var + " :=." + assignment_type + " " + aux + ";\n";
                    } else {
                        method_body += "\t\t" + "putfield(this," + var + ", " + aux + ";\n";
                    }
                    temps = "";

                    break;

                case "VarDeclaration":
                    String new_vars = generateOllirVarDeclaration(bodyContent);

                    if(!new_vars.equals("")) {
                        args.add(new_vars);
                    }
                    break;

                case "While":
                    branch_counter.incrementWhile();
                    method_body += generateOllirWhileCode(bodyContent, args, branch_counter, symbolTable);
                    break;

                case "IfAndElse":

                    branch_counter.incrementIfElse();
                    method_body += generateOllirIfAndElseCode(bodyContent, args, branch_counter, symbolTable);
                    break;

                case "MethodInvocation":

                    method_body += generateOllirMethodInvocation(bodyContent, args, branch_counter, symbolTable, var);
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


    String searchArgs(String var, ArrayList<String> args, SymbolTable symbolTable){
        String res ="";
        for(String arg : args){
            String[] allArgs = arg.split("\\.");

            String caracter = String.valueOf(allArgs[0].charAt(0));

            if(caracter.equals("$")){
                String[] newAllArgs = arg.split("\\.");

                if(newAllArgs[1].equals(var)){
                    res = arg;
                }
            }else{
                if(allArgs[0].equals(var)){
                    res = arg;
                }
            }
        }
        if(res.equals(""))
        {
            List<String> imports = symbolTable.getImports();
            if (imports.contains(var)) {
                res = var;
            }

        }
        return res;
    }

    String searchFields(String var)
    {
        String res= "";
        for(String field : fields) {
            String[] allArgs = field.split("\\.");
            if(allArgs[0].equals(var)){
                res = field;
            }
        }
        return res;
    }

    String genetrateOllirMethodArgs(JmmNode methodInvocation, ArrayList<String> args, BranchCounter branch_counter, SymbolTable symbolTable)
    {
        List<JmmNode> methodArgs = methodInvocation.getChildren().get(0).getChildren();
        String result = "";
        for(JmmNode methodArg : methodArgs)
        {
            result += ", " + generateOllirExpressionCode(methodArg, args, branch_counter, symbolTable);
        }
        return result;
    }


    String generateOllirExpressionCode(JmmNode node, ArrayList<String> args, BranchCounter branch_counter, SymbolTable symbolTable)
    {
        String result = "";
        List<JmmNode> contents = node.getChildren();

        String var = "";

        for(int i = 0; i < contents.size(); i++)
        {
            JmmNode content = contents.get(i);
            switch(content.getKind())
            {

                case "MethodInvocation":
                    result += generateOllirMethodInvocation(content, args, branch_counter, symbolTable, var);
                    break;

                case "Identifier":
                    result += searchArgs(content.get("val"), args, symbolTable);
                    break;

                case "Var":

                    var = searchArgs(content.get("val"), args, symbolTable);
                    if(var.equals(""))
                    {
                        var = searchFields(content.get("val"));
                        if(!var.equals(""))
                        {
                            // getfield
                            if (content.equals(contents.get(contents.size()-1)) || (!contents.get(i+1).getKind().equals("MethodInvocation") && !contents.get(i+1).getKind().equals("Assignment")))
                            {
                                branch_counter.incrementTemp();
                                String aux_temp = "temp"+ branch_counter.getTemp_counter() + ".i32";

                                String temp  = aux_temp + " :=.i32 getfield(this, " + var + ").i32;\n";

                                temps += "\t\t" + temp;
                                result += var;
                            }
                            // putfield
                        }
                        else
                        {
                            result += "ERROR";
                        }
                    }
                    else
                    {
                        if (content.equals(contents.get(contents.size()-1)) || (!contents.get(i+1).getKind().equals("MethodInvocation") && !contents.get(i+1).getKind().equals("Assignment")))
                            result += var;
                    }

                    break;

                case "IntegerLiteral":
                    result += content.get("val") + ".i32";
                    break;

                case "This":
                    var = "this";
                    //result += " invokevirtual(this, " + content.get("val") + ")"; // invokevirtual ??
                    break;

                case "And":
                    result += " &&.bool" + generateOllirExpressionCode(content, args, branch_counter, symbolTable);
                    break;

                case "Less":
                    if(content.getNumChildren() == 1){
                    result += " >=.i32 " + generateOllirExpressionCode(content, args, branch_counter, symbolTable);
                    }else{
                        result += " >=.i32 ";

                        branch_counter.incrementTemp();
                        String temp = "temp" + branch_counter.getTemp_counter() + ".bool" ;

                        result += temp;

                        temp += " :=.bool ";
                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);

                        temp += ";\n";

                        temps += "\t\t" + temp;

                    }
                    break;

                case "PlusExpression":

                    if(content.getNumChildren() == 1){
                        result += " +.i32 " + generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                    }else{
                        result += " +.i32 ";

                        branch_counter.incrementTemp();
                        String temp = "temp"+ branch_counter.getTemp_counter() +".i32";

                        result += temp;

                        temp += " :=.i32 ";
                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        temp += ";\n";

                        temps += "\t\t" + temp;
                    }

                    break;

                case "MinusExpression":
                    if(content.getNumChildren() == 1){
                    result += " -.i32 " + generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                    }else{
                        result += " -.i32 ";

                        branch_counter.incrementTemp();
                        String temp = "temp" + branch_counter.getTemp_counter() + ".i32";

                        result += temp;

                        temp += " :=.i32 ";
                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        temp += ";\n";

                        temps += "\t\t" + temp;
                    }

                    break;

                case "MultExpression":
                    if(content.getNumChildren() == 1 ){
                    result += " *.i32 " + generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                    }else{
                        result += " *.i32 ";

                        branch_counter.incrementTemp();
                        String temp = "temp" + branch_counter.getTemp_counter() + ".i32";

                        result += temp;

                        temp += " :=.i32 ";
                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        temp += ";\n";

                        temps += "\t\t" + temp;
                    }

                    break;

                case "DivExpression":
                    if(content.getNumChildren() == 1){
                    result += " /.i32 " + generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                    }else{
                        result += " /.i32 ";

                        branch_counter.incrementTemp();
                        String temp = "temp" + branch_counter.getTemp_counter() + ".i32";

                        result += temp;

                        temp += " :=.i32 ";
                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        temp += ";\n";

                        temps += "\t\t" + temp;
                    }

                    break;

                case "NotExpression":
                    result += " !.bool " + generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                    break;

                case "SubExpression":

                    branch_counter.incrementTemp();
                    String temp = "temp" + branch_counter.getTemp_counter() + ".i32" ;

                    result += temp;

                    temp += " :=.i32 ";
                    temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                    temp += ";\n";

                    temps += "\t\t" + temp;

                    break;

                case "Length":
                    result += ".length()";
                    break;

                case "ConstructorClass":
                    result += "new(" + content.get("val") + ")." + content.get("val");
                    break;

                /*case "ArrayIndex":

                    break;*/


                default:
                    throw new IllegalStateException("Unexpected value: " + content.getKind());
            }
        }
        return result;
    }


    String generateOllirVarDeclaration(JmmNode vars){

        String varDeclaration="";

        String varTypeAux = vars.getChildren().get(0).get("val");
        String varAux = vars.getChildren().get(1).get("val");

        if(varTypeAux.equals("int")){
            varDeclaration += varAux + ".i32";
        }else if(varTypeAux.equals("boolean")){
            varDeclaration += varAux + ".bool";
        }else if(varTypeAux.equals("int[]")){
            varDeclaration += varAux + ".array.i32";
        } else {
            varDeclaration += varAux + "." + varTypeAux;
        }

        //args.add(varDeclaration);

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



















