import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.ast.examples.BranchCounter;
import pt.up.fe.comp.jmm.ollir.JmmOptimization;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.report.Report;


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

        BranchCounter finalBranchCounter = new BranchCounter();

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
                            finalBranchCounter = branch_counter;
                            break;

                        case "ReturnStatement":

                            String statement = generateOllirExpressionCode(child, vars, finalBranchCounter, symbolTable);
                            String[] elements = statement.split(" ");
                            String[] aux_split = elements[0].split("\\.");
                            String type = aux_split[aux_split.length-1];
                            if(elements.length != 1)
                            {
                                finalBranchCounter.incrementTemp();
                                String returnTemp = "temp"+finalBranchCounter.getTemp_counter() +"." + type;
                                temps += finalBranchCounter.getident() + returnTemp + " :=."+type + " " + statement +";\n";
                                statement = returnTemp;
                            }

                            returnStatement += temps + finalBranchCounter.getident() + "ret." + type + " " + statement + ";";
                            temps = "";
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

            temps += branch_counter.getident() + temp;
            return aux_temp;
        }

        result += ";" + "\n";

        return branch_counter.getident() + result;
    }


    String generateOllirBodyCode(JmmNode body, ArrayList<String> args, BranchCounter branch_counter, SymbolTable symbolTable){

        List<JmmNode> methodBodyContents = body.getChildren();
        String method_body = "";

        String assignment_type = "";
        String var ="";

        for(int i = 0; i < methodBodyContents.size(); i++){
            JmmNode bodyContent = methodBodyContents.get(i);
            switch (bodyContent.getKind())
            {

                /*

                Var -> d
                ArrayIndex -> null
                 Var -> c
                Assignment -> null
                 Var -> a
                 MinusExpression -> null
                  Var -> b
                 */
                case "Var":
                    var = searchArgs(bodyContent.get("val"), args, symbolTable);
                    if(var.equals(""))
                    {
                        var = searchFields(bodyContent.get("val"));
                        if(!var.equals("")) {
                            // getfield
                            if (bodyContent.equals(methodBodyContents.get(methodBodyContents.size() - 1)) || (!methodBodyContents.get(i + 1).getKind().equals("MethodInvocation") && !methodBodyContents.get(i + 1).getKind().equals("Assignment") && !methodBodyContents.get(i + 1).getKind().equals("ArrayIndex"))) {
                                branch_counter.incrementTemp();
                                String aux_temp = "temp" + branch_counter.getTemp_counter() + ".i32";

                                String temp = aux_temp + "getfield(this, " + var + ").i32;\n";

                                temps += branch_counter.getident() + temp;
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
                        String[] var_split = var.split("\\.");
                        if (bodyContent.equals(methodBodyContents.get(methodBodyContents.size()-1)) || !methodBodyContents.get(i+1).getKind().equals("Assignment"))
                        {
                            if(i != methodBodyContents.size() - 1 && methodBodyContents.get(i+1).getKind().equals("ArrayIndex"))
                            {
                                if(var_split[0].charAt(0) == '$')
                                    var = var_split[0] + "." + var_split[1]; // {$1.var}.whaetever
                                else
                                    var = var_split[0]; // {var}.whaetever
                            }
                            else if(i != methodBodyContents.size() - 1 && methodBodyContents.get(i+1).getKind().equals("MethodInvocation"))
                            {
                                if(var_split[0].charAt(0) == '$')
                                    var = var_split[1]; // $1.{var}.whaetever
                                else
                                    var = var_split[0]; // {var}.whaetever
                            }
                            else
                                method_body += var;
                        }

                        String[] aux_split = var.split("\\.");
                        assignment_type = aux_split[aux_split.length-1];
                    }



                    break;

                case "Assignment":

                    String aux= generateOllirExpressionCode(bodyContent, args,branch_counter, symbolTable);

                    method_body += temps;
                    if(searchFields(var).equals(""))
                    {
                        method_body += branch_counter.getident() + var + " :=." + assignment_type + " " + aux + ";\n";
                    } else {
                        method_body += branch_counter.getident() + "putfield(this," + var + ", " + aux + ";\n";
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
                    branch_counter.incrementIdent();
                    break;

                case "IfAndElse":

                    branch_counter.incrementIfElse();
                    method_body += generateOllirIfAndElseCode(bodyContent, args, branch_counter, symbolTable);
                    if(i + 1 != methodBodyContents.size()) // if it isnt last one
                        branch_counter.incrementIdent();
                    break;

                case "MethodInvocation":

                    method_body += generateOllirMethodInvocation(bodyContent, args, branch_counter, symbolTable, var);
                    break;

                case "Scope":
                    method_body += generateOllirBodyCode(bodyContent, args, branch_counter, symbolTable);
                    break;

                case "ArrayIndex":
                    String arrayIndex = "["+generateOllirExpressionCode(bodyContent, args,branch_counter, symbolTable)+"].i32";
                    if(i == methodBodyContents.size() - 1 || (!methodBodyContents.get(i+1).getKind().equals("Assignment")))
                         method_body += var + arrayIndex;
                    else
                        var += arrayIndex;
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + bodyContent.getKind());
            }



        }
        return method_body;
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

    String handleSiblings(ArrayList<String> siblings, String content, int sibling_index,BranchCounter branch_counter)
    {
        if(sibling_index == 0)
            return content;
        else if (sibling_index == 1){
            return siblings.get(0) + content;
        }else{
            branch_counter.incrementTemp();
            String previous = siblings.get(sibling_index-1);
            String[] elements = previous.split(" ");
            String[] aux_split = elements[0].split("\\.");
            String type = aux_split[aux_split.length-1];


            String temp="temp" + branch_counter.getTemp_counter() + "." + type;
            temps += branch_counter.getident() + temp + " :=."+ type + " " + previous + ";\n";

            return temp + content;
        }
    }
    String generateOllirExpressionCode(JmmNode node, ArrayList<String> args, BranchCounter branch_counter, SymbolTable symbolTable)
    {
        String result = "";
        List<JmmNode> contents = node.getChildren();

        String var = "";

        ArrayList<String> siblings = new ArrayList<>();
        boolean moreThanTwo = false;
        if(contents.size() > 2)
        {
            moreThanTwo = true;
        }

        for(int i = 0; i < contents.size(); i++)
        {
            JmmNode content = contents.get(i);
            switch(content.getKind())
            {

                case "MethodInvocation":
                    String content_string = generateOllirMethodInvocation(content, args, branch_counter, symbolTable, var);
                    if(moreThanTwo)
                        siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                    else
                        result += content_string;
                    break;

                case "Identifier":
                    content_string = searchArgs(content.get("val"), args, symbolTable);
                    if(moreThanTwo)
                        siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                    else
                        result += content_string;
                    break;

                case "Var":

                    var = searchArgs(content.get("val"), args, symbolTable);

                    if(var.equals(""))
                    {
                        var = searchFields(content.get("val"));
                        if(!var.equals(""))
                        {
                            // getfield
                            if (content.equals(contents.get(contents.size()-1)) || (!contents.get(i+1).getKind().equals("MethodInvocation") && !contents.get(i+1).getKind().equals("Assignment") && !contents.get(i+1).getKind().equals("ArrayIndex")))
                            {
                                branch_counter.incrementTemp();
                                String aux_temp = "temp"+ branch_counter.getTemp_counter() + ".i32";

                                String temp  = aux_temp + " :=.i32 getfield(this, " + var + ").i32;\n";

                                temps += branch_counter.getident() + temp;
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
                        String[] var_split = var.split("\\.");
                        if (content.equals(contents.get(contents.size()-1)) || !contents.get(i+1).getKind().equals("Assignment"))
                            if(i != contents.size() - 1 && contents.get(i+1).getKind().equals("ArrayIndex"))
                            {
                                if(var_split[0].charAt(0) == '$')
                                    var = var_split[0] + "." + var_split[1]; // $1.var.whaetever
                                else
                                    var = var_split[0]; // var.whaetever
                            }
                            else if(i != contents.size() - 1 && contents.get(i+1).getKind().equals("MethodInvocation"))
                            {
                                if(var_split[0].charAt(0) == '$')
                                    var = var_split[1]; // $1.{var}.whaetever
                                else
                                    var = var_split[0]; // {var}.whaetever
                            }
                            else if(!moreThanTwo)
                                result += var;
                        if(moreThanTwo)
                            siblings.add(handleSiblings(siblings, var, i, branch_counter));
                    }

                    break;

                case "IntegerLiteral":
                    content_string = content.get("val") + ".i32";
                    if(moreThanTwo)
                        siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                    else
                        result += content_string;
                    break;

                case "This":
                    var = "this";
                    if(moreThanTwo)
                        siblings.add(handleSiblings(siblings, "", i, branch_counter));
                    //result += " invokevirtual(this, " + content.get("val") + ")"; // invokevirtual ??
                    break;

                case "And":
                    if(content.getNumChildren() == 1){
                        content_string = " &&.bool " + generateOllirExpressionCode(content, args, branch_counter, symbolTable);
                        if(moreThanTwo)
                            siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                        else
                            result += content_string;
                    }else{

                        branch_counter.incrementTemp();
                        String temp = "temp" + branch_counter.getTemp_counter() + ".bool" ;

                        content_string = " &&.bool " + temp;
                        if(moreThanTwo)
                            siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                        else
                            result += content_string;

                        temp += " :=.bool ";
                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);

                        temp += ";\n";

                        temps += branch_counter.getident() + temp;
                    }

                    break;

                case "Less":

                    if(content.getNumChildren() == 1){
                        content_string = " <.i32 " + generateOllirExpressionCode(content, args, branch_counter, symbolTable);
                        if(moreThanTwo)
                            siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                        else
                            result += content_string;
                    }else{

                        branch_counter.incrementTemp();
                        String temp = "temp" + branch_counter.getTemp_counter() + ".bool" ;

                        content_string = " <.i32 " + temp;
                        if(moreThanTwo)
                            siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                        else
                            result += content_string;

                        temp += " :=.bool ";
                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);

                        temp += ";\n";

                        temps += branch_counter.getident() + temp;
                    }
                    break;

                case "PlusExpression":

                    if(content.getNumChildren() == 1){
                        content_string = " +.i32 " + generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        if(moreThanTwo)
                            siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                        else
                            result += content_string;

                    }else{

                        branch_counter.incrementTemp();
                        String temp = "temp"+ branch_counter.getTemp_counter() +".i32";

                        content_string =" +.i32 " + temp;
                        if(moreThanTwo)
                            siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                        else
                            result += content_string;


                        temp += " :=.i32 ";
                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        temp += ";\n";

                        temps += branch_counter.getident() + temp;
                    }

                    break;

                case "MinusExpression":
                    if(content.getNumChildren() == 1){
                        content_string = " -.i32 " + generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        if(moreThanTwo)
                            siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                        else
                            result += content_string;
                    }else{

                        branch_counter.incrementTemp();
                        String temp = "temp" + branch_counter.getTemp_counter() + ".i32";

                        content_string = " -.i32 " + temp;
                        if(moreThanTwo)
                            siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                        else
                            result += content_string;

                        temp += " :=.i32 ";
                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        temp += ";\n";

                        temps += branch_counter.getident() + temp;
                    }

                    break;

                case "MultExpression":
                    if(content.getNumChildren() == 1){
                        content_string = " *.i32 " + generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        if(moreThanTwo)
                            siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                        else
                            result += content_string;
                    }else{


                        branch_counter.incrementTemp();
                        String temp = "temp" + branch_counter.getTemp_counter() + ".i32";
                        String content_string1 = " *.i32 " + temp;
                        if(moreThanTwo)
                            handleSiblings(siblings, content_string1, i, branch_counter);
                        else
                        result += content_string1;

                        temp += " :=.i32 ";
                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        temp += ";\n";

                        temps += branch_counter.getident() + temp;
                    }

                    break;

                case "DivExpression":


                    if(content.getNumChildren() == 1){
                        content_string = " /.i32 " + generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        if(moreThanTwo)
                            siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                        else
                            result += content_string;
                    }else{


                        branch_counter.incrementTemp();
                        String temp = "temp" + branch_counter.getTemp_counter() + ".i32";

                        content_string = " /.i32 " + temp;
                        if(moreThanTwo)
                            siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                        else
                            result += content_string;

                        temp += " :=.i32 ";
                        temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        temp += ";\n";

                        temps += branch_counter.getident() + temp;
                    }

                    break;

                case "NotExpression":


                    //result += "!.bool " + generateOllirExpressionCode(content, args,branch_counter, symbolTable);


                    branch_counter.incrementTemp();
                    String temp = "temp" + branch_counter.getTemp_counter() + ".bool" ;

                    content_string = temp;
                    if(moreThanTwo)
                        siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                    else
                        result += content_string;

                    temp += " :=.bool !.bool ";
                    temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);

                    System.out.println("NotExprResult: " + temp);

                    temp += ";\n";

                    temps += branch_counter.getident() + temp;

                    /*
                    if(content.getNumChildren() == 1){
                        content_string = "!.bool " + generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                        if(moreThanTwo)
                            siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                        else
                            result += content_string;
                    }else{


                    }*/
                    break;

                case "SubExpression":

                    branch_counter.incrementTemp();
                    temp = "temp" + branch_counter.getTemp_counter() + ".i32" ;

                    if(moreThanTwo)
                        siblings.add(handleSiblings(siblings, temp, i, branch_counter));
                    else
                        result += temp;

                    temp += " :=.i32 ";
                    temp += generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                    temp += ";\n";

                    temps += branch_counter.getident() + temp;

                    break;

                case "Length":

                    content_string = ".length()";
                    if(moreThanTwo)
                        siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                    else
                        result += content_string;
                    break;

                case "ConstructorClass":
                    content_string = "new(" + content.get("val") + ")." + content.get("val");
                    if(moreThanTwo)
                        siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                    else
                        result += content_string;
                    break;

                case "ArrayIndex": // TODO: NEED FIXING BECAUSE OF moreThanTwo?

                    String arrayIndex = "["+generateOllirExpressionCode(content, args,branch_counter, symbolTable)+"].i32";

                    if(i == contents.size() - 1 || (!contents.get(i+1).getKind().equals("Assignment")))
                        result += var + arrayIndex;
                    else
                        var += arrayIndex;
                    break;

                case "ConstructorIntArray":
                    content_string = "["+generateOllirExpressionCode(content, args,branch_counter, symbolTable)+"].i32";
                    if(moreThanTwo)
                        siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                    else
                        result += content_string;
                    break;

                case "True":
                    content_string = "1.bool";
                    if(moreThanTwo)
                        siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                    else
                        result += content_string;
                    break;

                case "False":
                    content_string = "0.bool";
                    if(moreThanTwo)
                        siblings.add(handleSiblings(siblings, content_string, i, branch_counter));
                    else
                        result += content_string;
                    break;


                default:
                    throw new IllegalStateException("Unexpected value: " + content.getKind());
            }
        }

        System.out.println(siblings);

        if(moreThanTwo)
            return siblings.get(siblings.size()-1);
        else
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


    // CHECKPOINT 3

    String generateOllirWhileCode(JmmNode vars, ArrayList<String> args, BranchCounter branch_counter, SymbolTable symbolTable){

        List<JmmNode> whileContent = vars.getChildren();
        String ollirWhile="";

        int current_branch_counter = branch_counter.getWhile_counter();

        for(JmmNode content : whileContent){

            switch(content.getKind())
            {
                case "WhileExpression":
                    branch_counter.incrementIdent();
                    String condition = generateOllirExpressionCode(content, args,branch_counter, symbolTable);
                    branch_counter.decrementIdent();
                    ollirWhile += branch_counter.getident() + "Loop" + current_branch_counter + ":\n" + temps;
                    temps = "";
                    ollirWhile += branch_counter.getident() + "\t" + "if (" + condition + ") goto Body" + current_branch_counter + ";\n" + branch_counter.getident() + "\t" + "goto EndLoop" + current_branch_counter + "; \n" + branch_counter.getident() + "Body"+ current_branch_counter +":\n";
                    break;

                case "WhileBody":
                    List<JmmNode> bodyContent = content.getChildren();

                    for(JmmNode insideContent : bodyContent) {

                        if (insideContent.getKind().equals("Scope")) {
                            branch_counter.incrementIdent();
                            ollirWhile += generateOllirBodyCode(insideContent,args, branch_counter, symbolTable);
                            branch_counter.decrementIdent();
                        }
                    }
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + content.getKind());
            }
        }

        return ollirWhile + branch_counter.getident() + "EndLoop" + current_branch_counter + ":\n";
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
        int current_branch_counter = branch_counter.getIfelse_counter();
        String current_branch_ident = branch_counter.getident();

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

                    branch_counter.incrementTemp();
                    String ifTemp = "temp" + branch_counter.getTemp_counter() + ".bool";
                    result += branch_counter.getident() + ifTemp + ":=.bool " + aux + ";\n";

                    temps = "";
                    result += current_branch_ident + "if (";

                    result += ifTemp + " ==.bool 0.bool)";
                    if(else_exists)
                        result += " goto else";
                    else
                        result += " goto endif";

                    result += current_branch_counter + ";\n";

                    break;

                case "IfBody":
                    branch_counter.setIndet(current_branch_ident);
                    branch_counter.incrementIdent();
                    result += generateOllirBodyCode(content, args, branch_counter, symbolTable) ;

                    String extra_ident = "";
                    for(JmmNode node : content.getChildren()) {
                        if (node.getKind().equals("IfAndElse") || node.getKind().equals("While")) {
                            extra_ident += "\t";
                        }
                    }

                    if(else_exists)
                        result += current_branch_ident + "\t" + extra_ident + "goto endif"+current_branch_counter+";\n";
                    branch_counter.decrementIdent();
                    break;

                case "ElseBody":
                    result += current_branch_ident + "else" + current_branch_counter + ":\n";
                    branch_counter.setIndet(current_branch_ident);
                    branch_counter.incrementIdent();
                    result += generateOllirBodyCode(content, args, branch_counter, symbolTable);
                    branch_counter.decrementIdent();
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + content.getKind());
            }
        }

        return result + current_branch_ident + "endif" + current_branch_counter +":\n";
    }
}




/*
Program -> null
 Imports -> null
  ImportDeclaration -> io
 Class -> Test
  MethodDeclaration -> null
   RegularMethod -> func1
    ReturnType -> null
     Type -> int
    MethodParams -> null
     MethodParam -> null
      Type -> int
      VarId -> num
    MethodBody -> null
     VarDeclaration -> null
      Type -> int
      VarId -> num_aux
     VarDeclaration -> null
      Type -> int
      VarId -> a
     Var -> a
     Assignment -> null
      IntegerLiteral -> 0
     While -> null
      WhileExpression -> null
       Var -> a
       Less -> null
        IntegerLiteral -> 3
      WhileBody -> null
       Scope -> null
        Var -> a
        Assignment -> null
         Var -> a
         PlusExpression -> null
          IntegerLiteral -> 1
     IfAndElse -> null
      IfExpression -> null
       Var -> num
       Less -> null
        IntegerLiteral -> 1
      IfBody -> null
       Var -> num_aux
       Assignment -> null
        Var -> a
      ElseBody -> null
       Var -> num_aux
       Assignment -> null
        SubExpression -> null
         IntegerLiteral -> 1
         PlusExpression -> null
          IntegerLiteral -> 2
          MultExpression -> null
           IntegerLiteral -> 2
        MultExpression -> null
         IntegerLiteral -> 5
    ReturnStatement -> null
     Var -> num_aux
  MethodDeclaration -> null
   Main -> null
    ArgName -> args
    MethodBody -> null
     Var -> io
     MethodInvocation -> println
      MethodArgs -> null
       MethodArg -> null
        ConstructorClass -> Test
        MethodInvocation -> func
         MethodArgs -> null
          MethodArg -> null
           IntegerLiteral -> 10

 */