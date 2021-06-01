import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import org.specs.comp.ollir.*;

import org.specs.comp.ollir.AssignInstruction;

import pt.up.fe.comp.jmm.jasmin.JasminBackend;
import pt.up.fe.comp.jmm.jasmin.JasminResult;
import pt.up.fe.comp.jmm.jasmin.JasminUtils;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.Stage;
import pt.up.fe.specs.util.SpecsIo;

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

public class BackendStage implements JasminBackend {
    StringBuilder jasminString = new StringBuilder();
    StringBuilder methodString = new StringBuilder();
    int stackLimit = 0;
    private ClassUnit ollirClass;
    private int cmpCounter = 0;

    @Override
    public JasminResult toJasmin(OllirResult ollirResult) {
        ollirClass = ollirResult.getOllirClass();
        try {


            ollirClass.checkMethodLabels(); // check the use of labels in the OLLIR loaded
            //ollirClass.buildCFGs(); // build the CFG of each method
            //ollirClass.outputCFGs(); // output to .dot files the CFGs, one per method
            ollirClass.buildVarTables(); // build the table of variables for each method
            ollirClass.show(); // print to console main information about the input OLLIR


            generateHeader();
            generateGlobalFields();
            generateInit();
            generateMethods();

            // // Convert the generated string to a .j file
            String Filename = ollirClass.getClassName()+".j";
            try {
                FileWriter myWriter = new FileWriter(Filename);
                myWriter.write(jasminString.toString());
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // // Convert the .j file to .class
            File jasminFile = new File(Filename);
            Path dir = Path.of("./");
            JasminUtils.assemble(jasminFile,dir.toFile());

            String jasminCode = jasminString.toString();

            // // More reports from this stage
            List<Report> reports = new ArrayList<>();

            return new JasminResult(ollirResult, jasminCode, reports);


        } catch (OllirErrorException e) {
            return new JasminResult(ollirClass.getClassName(), null,
                    Arrays.asList(Report.newError(Stage.GENERATION, -1, -1, "Exception during Jasmin generation", e)));
        }

    }




    private void generateHeader() {
        jasminString.append(".class public " + ollirClass.getClassName());
        if(! (ollirClass.getSuperClass() == null)) {
            jasminString.append("\n.super " + ollirClass.getSuperClass());
        }
        else jasminString.append("\n.super java/lang/Object");
        jasminString.append("\n\n");
    }

    private void generateInit() {
        jasminString.append("; standard initializer");
        jasminString.append("\n.method public <init>()V"+ "\n");
        jasminString.append("\taload_0"+ "\n");
        if(! (ollirClass.getSuperClass() == null)) {
            jasminString.append("\tinvokenonvirtual " + ollirClass.getSuperClass() + "/<init>()V"+ "\n");
        }
        else jasminString.append("\tinvokenonvirtual java/lang/Object/<init>()V"+ "\n");
        jasminString.append("\treturn"+ "\n");
        jasminString.append(".end method\n");
        jasminString.append("\n");
    }

    private void generateGlobalFields() {
        jasminString.append("; class fields\n");
        for (var field:ollirClass.getFields()) {
            String type = field.getFieldType().toString();
            if(type.equals("INT32")) type = "I";
            jasminString.append(".field " + field.getFieldAccessModifier().toString().toLowerCase(Locale.ROOT) + " " + field.getFieldName() + " " + type+ "\n") ;
        }
        jasminString.append("\n");
    }

    private void generateMethods() {
        jasminString.append("; methods");
        for (var method: ollirClass.getMethods()) {
             generateMethod(method);
        }
    }

    private void generateMethod(Method method) {
        if(!method.isConstructMethod())
        {
            jasminString.append("\n.method ");
            jasminString.append(method.getMethodAccessModifier().toString().toLowerCase(Locale.ROOT));

            jasminString.append(method.isStaticMethod() ? " static ": " ");
            jasminString.append(method.getMethodName());
            getMethodParameters(method);
            try {
                getMethodBody(method);
            } catch (OllirErrorException e) {
                e.printStackTrace();
            }
            jasminString.append("\n.end method\n");

        }
    }



    private void getMethodParameters(Method method) {
        jasminString.append("(");

        for (var param: method.getParams()) {
            if(param.getType().toString() == "INT32")  jasminString.append("I");
            else if (param.getType().toString() == "ARRAYREF"){
                if(method.getMethodName().equals("main")){
                    jasminString.append("[Ljava/lang/String;");
                }
                else{
                    jasminString.append("[I");
                }
            }
        }
        jasminString.append(")");
        if(method.getReturnType().toString() == "VOID"){
            jasminString.append("V");
        }
        else jasminString.append("I");



    }

    private void getMethodLimits(Method method) {
        int locals = method.getVarTable().size() + method.getParams().size() + 1;
        jasminString.append("\n\t\t.limit stack ");
        jasminString.append(stackLimit);
        jasminString.append("\n\t\t.limit locals ");
        jasminString.append(locals);
    }

    private void getMethodBody(Method method) throws OllirErrorException {
        methodString = new StringBuilder();
        for (var instr:method.getInstructions()) {
            HashMap<String, Instruction> map = method.getLabels();
            for (Map.Entry<String, Instruction> entry : map.entrySet()) {
                String key = entry.getKey();
                if(method.getLabels().get(key)==instr) methodString.append("\n\n\t"+key+":");
            }
            InstructionType instType = instr.getInstType();
            switch(instType){
                case ASSIGN:
                    generateInst((AssignInstruction) instr,method);
                    break;
                case RETURN:
                    generateInst((ReturnInstruction)instr,method);
                    break;
                case BRANCH:
                    generateInst((CondBranchInstruction)instr,method);
                    break;
                case GOTO:
                    generateInst((GotoInstruction)instr,method);
                    break;
                case CALL:
                    generateInst((CallInstruction)instr,method);
                    break;
                default:
                    //jasminString.append("\n\n\t"+instType.toString());
            }
        }
        getMethodReturn(method);
        getMethodLimits(method);
        jasminString.append(methodString.toString());
    }

    private void generateInst(CallInstruction instr, Method method) {

        Element e1 = instr.getFirstArg();
        Element e2 = instr.getSecondArg();
        stackLimit = Integer.max(stackLimit,instr.getListOfOperands().size());
        Type type = e1.getType();
        int auxvirtualReg;
        if(type.toString().equals("CLASS"))
        {
            for (var oper :instr.getListOfOperands()) {
                if(oper.isLiteral()){
                    int N = Integer.parseInt(((LiteralElement) oper).getLiteral());
                    if(N <=5) methodString.append("\n\n\t\ticonst_"+N);
                    else {
                        methodString.append("\n\n\t\tbipush "+ N);
                    }
                }else{
                    auxvirtualReg = method.getVarTable().get(((Operand)oper).getName()).getVirtualReg() ;
                    if(auxvirtualReg <=3) methodString.append("\n\t\tiload_"+auxvirtualReg);
                    else {
                        methodString.append("\n\t\tiload "+auxvirtualReg);
                    }
                }
            }
            methodString.append("\n\t\tinvokestatic ");
            if(e1.isLiteral()){
                String literal = ((LiteralElement)e1).getLiteral();
                String newLit = literal.replace("\"","");
                methodString.append(newLit);
            }
            else{
                Operand o1 = (Operand) e1;
                methodString.append(o1.getName());
            }
            methodString.append("/");
            if(e2.isLiteral())
            {
                String literal = ((LiteralElement)e2).getLiteral();
                String newLit = literal.replace("\"","");
                methodString.append(newLit);
            }
            else{
                Operand o2 = (Operand) e2;
                methodString.append(o2.getName());
            }
            methodString.append("(");
            for (var oper :instr.getListOfOperands()) {
                if(oper.isLiteral()){
                    methodString.append("I");
                }else if (oper.getType().toString().equals("INT32")){
                    methodString.append("I");
                }
            }

            methodString.append(")V\n");
        }
        else if(type.toString().equals("OBJECTREF")){
            for (var oper :instr.getListOfOperands()) {
                if(oper.isLiteral()){
                    int N = Integer.parseInt(((LiteralElement) oper).getLiteral());
                    if(N <=5) methodString.append("\n\n\t\ticonst_"+N);
                    else {
                        methodString.append("\n\n\t\tbipush "+ N);
                    }
                }else{
                    auxvirtualReg = method.getVarTable().get(((Operand)oper).getName()).getVirtualReg() ;
                    if(auxvirtualReg <=3) methodString.append("\n\t\tiload_"+auxvirtualReg);
                    else {
                        methodString.append("\n\t\tiload "+auxvirtualReg);
                    }
                }
            }
            String aux = ((LiteralElement)e2).getLiteral();
            String Func = aux.replace("\"","");
            if(Func.equals("<init>")) {
                methodString.append("\n\t\tinvokespecial ");
            }
            else{
                int valuevirtualReg = method.getVarTable().get(((Operand) e1).getName()).getVirtualReg();

                if(valuevirtualReg <=3) methodString.append("\n\t\taload_"+valuevirtualReg);
                else {
                    methodString.append("\n\t\taload "+valuevirtualReg);
                }
                methodString.append("\n\t\tinvokevirtual ");
            }
            methodString.append(Func);
            methodString.append("(");
            for (var oper :instr.getListOfOperands()) {
                if(oper.isLiteral()){
                    methodString.append("I");
                }else if (oper.getType().toString().equals("INT32")){
                    methodString.append("I");
                }
            }
            methodString.append(")V\n");

        }
        else if(type.toString().equals("THIS")){
            String aux = ((LiteralElement)e2).getLiteral();
            String Func = aux.replace("\"","");
            methodString.append("\n\n\t\taload_0");
            methodString.append("\n\t\tinvokespecial java/lang/Object.");
            methodString.append(Func);
            methodString.append("(");
            // for (var oper :instr.getListOfOperands()) {methodString.append(oper); }
            methodString.append(")V\n");

        }

    }

    private void generateInst(AssignInstruction instr, Method method) {
        InstructionType rhs = instr.getRhs().getInstType();
        String dest = ((Operand)instr.getDest()).getName();
        int virtualReg = method.getVarTable().get(dest).getVirtualReg() ;
        int auxvirtualReg;
        int N;
        Operand o1 ;
        Operand o2 ;
        switch(rhs){
            case NOPER:
                stackLimit = Integer.max(stackLimit,1);
                Element operand = ((SingleOpInstruction) instr.getRhs()).getSingleOperand();
                if (operand.isLiteral()) {
                    N = Integer.parseInt(((LiteralElement) operand).getLiteral());
                    if(N <=5) methodString.append("\n\n\t\ticonst_"+N);
                    else {
                        methodString.append("\n\n\t\tbipush "+ N);
                    }
                    if(virtualReg <=3) methodString.append("\n\t\tistore_"+virtualReg);
                    else {
                        methodString.append("\n\t\tistore "+virtualReg);
                    }
                }
                else {
                    o1 = (Operand)((SingleOpInstruction) instr.getRhs()).getSingleOperand();
                    auxvirtualReg = method.getVarTable().get(dest).getVirtualReg() ;
                    int iVirtualReg = method.getVarTable().get(o1.getName()).getVirtualReg() ;

                    if(o1.getName().equals("i")){
                        methodString.append("\n\n\t\taload_0");
                    }
                    else methodString.append("\n");
                    if(iVirtualReg <=3) methodString.append("\n\t\tiload_"+iVirtualReg);
                    else {
                        methodString.append("\n\t\tiload "+iVirtualReg);
                    }
                    if(o1.getName().equals("i")){
                        methodString.append("\n\t\tiaload");
                    }
                    if(auxvirtualReg <=3) methodString.append("\n\t\tistore_"+auxvirtualReg);
                    else {
                        methodString.append("\n\t\tistore "+auxvirtualReg);
                    }

                }
                break;
            case BINARYOPER:
                Element e1 = ((BinaryOpInstruction) instr.getRhs()).getLeftOperand();
                Element e2 = ((BinaryOpInstruction) instr.getRhs()).getRightOperand();
                OperationType operation = ((UnaryOpInstruction) instr.getRhs()).getUnaryOperation().getOpType();
                if(dest.equals("i") && operation.toString() =="ADD" && e2.isLiteral()) {
                    stackLimit = Integer.max(stackLimit,2);
                    o1 = (Operand) e1;
                    auxvirtualReg = method.getVarTable().get(o1.getName()).getVirtualReg() ;
                    methodString.append("\n\n\t\tiinc "+auxvirtualReg+" "+((LiteralElement) e2).getLiteral());
                    break;
                }

                if(e1.isLiteral()) {
                    N = Integer.parseInt(((LiteralElement) e1).getLiteral());
                    if(N <=5) methodString.append("\n\n\t\ticonst_"+N);
                    else {
                        methodString.append("\n\n\t\tbipush "+ N);
                    }
                } else {
                    o1 = (Operand) e1;
                    auxvirtualReg = method.getVarTable().get(o1.getName()).getVirtualReg() ;
                    if(auxvirtualReg <=3) methodString.append("\n\n\t\tiload_"+auxvirtualReg);
                    else {
                        methodString.append("\n\n\t\tiload "+auxvirtualReg);
                    }
                }
                if(e2.isLiteral()) {
                    N = Integer.parseInt(((LiteralElement) e2).getLiteral());
                    if(N <=5) methodString.append("\n\t\ticonst_"+N);
                    else {
                        methodString.append("\n\t\tbipush "+ N);
                    }
                } else {
                    o1 = (Operand) e2;
                    auxvirtualReg = method.getVarTable().get(o1.getName()).getVirtualReg() ;
                    if(auxvirtualReg <=3) methodString.append("\n\t\tiload_"+auxvirtualReg);
                    else {
                        methodString.append("\n\t\tiload "+auxvirtualReg);
                    }


                }
                auxvirtualReg = method.getVarTable().get(dest).getVirtualReg() ;
                operation = ((UnaryOpInstruction) instr.getRhs()).getUnaryOperation().getOpType();
                if(operation.toString().equals("ANDB")){

                    methodString.append("\n\t\tiand");
                    if(auxvirtualReg <=3) methodString.append("\n\t\tistore_"+auxvirtualReg);
                    else {
                        methodString.append("\n\t\tistore "+auxvirtualReg);
                    }

                }
                else if(operation.toString().equals("LTH")){
                    methodString.append("\n\t\tif_icmplt cmpt"+cmpCounter);
                    methodString.append("\n\t\ticonst_0");
                    if(auxvirtualReg <=3) methodString.append("\n\t\tistore_"+auxvirtualReg);
                    else {
                        methodString.append("\n\t\tistore "+auxvirtualReg);
                    }
                    methodString.append("\n\t\tgoto endcmp"+cmpCounter);
                    methodString.append("\n\tcmpt"+cmpCounter+":");
                    methodString.append("\n\t\ticonst_1");
                    if(auxvirtualReg <=3) methodString.append("\n\t\tistore_"+auxvirtualReg);
                    else {
                        methodString.append("\n\t\tistore "+auxvirtualReg);
                    }
                    methodString.append("\n\tendcmp"+cmpCounter+":");
                    cmpCounter++;
                }
                else  {

                    methodString.append("\n\t\ti"+ operation.toString().toLowerCase(Locale.ROOT));
                    if(auxvirtualReg <=3) methodString.append("\n\t\tistore_"+auxvirtualReg);
                    else {
                        methodString.append("\n\t\tistore "+auxvirtualReg);
                    }
                }
                stackLimit = Integer.max(stackLimit,2);
                break;
            case UNARYOPER:
                stackLimit = Integer.max(stackLimit,1);
                auxvirtualReg = method.getVarTable().get(dest).getVirtualReg() ;
                operand = ((UnaryOpInstruction) instr.getRhs()).getRightOperand();
                if(operand.isLiteral())
                {
                    N = Integer.parseInt(((LiteralElement) operand).getLiteral());
                    if(N <=5) methodString.append("\n\n\t\ticonst_"+N);
                    else {
                        methodString.append("\n\n\t\tbipush "+ N);
                    }
                    methodString.append("\n\t\tifeq cmpt"+cmpCounter);
                    methodString.append("\n\t\ticonst_0");
                    if(auxvirtualReg <=3) methodString.append("\n\t\tistore_"+auxvirtualReg);
                    else {
                        methodString.append("\n\t\tistore "+auxvirtualReg);
                    }
                    methodString.append("\n\t\tgoto endcmp"+cmpCounter);
                    methodString.append("\n\tcmpt"+cmpCounter+":");
                    methodString.append("\n\t\ticonst_1");
                    if(auxvirtualReg <=3) methodString.append("\n\t\tistore_"+auxvirtualReg);
                    else {
                        methodString.append("\n\t\tistore "+auxvirtualReg);
                    }
                    methodString.append("\n\tendcmp"+cmpCounter+":");
                    cmpCounter++;
                }
                else{
                    o1 = (Operand) operand;
                    int destvirtualReg = method.getVarTable().get(dest).getVirtualReg();
                    auxvirtualReg = method.getVarTable().get(o1.getName()).getVirtualReg() ;
                    if(auxvirtualReg <=3) methodString.append("\n\n\t\tiload_"+auxvirtualReg);
                    else {
                        methodString.append("\n\n\t\tiload "+auxvirtualReg);
                    }
                    methodString.append("\n\t\tifeq cmpt"+cmpCounter);
                    methodString.append("\n\t\ticonst_0");
                    if(destvirtualReg <=3) methodString.append("\n\t\tistore_"+destvirtualReg);
                    else {
                        methodString.append("\n\t\tistore "+destvirtualReg);
                    }
                    methodString.append("\n\t\tgoto endcmp"+cmpCounter);
                    methodString.append("\n\tcmpt"+cmpCounter+":");
                    methodString.append("\n\t\ticonst_1");
                    if(destvirtualReg <=3) methodString.append("\n\t\tistore_"+destvirtualReg);
                    else {
                        methodString.append("\n\t\tistore "+destvirtualReg);
                    }
                    methodString.append("\n\tendcmp"+cmpCounter+":");
                    cmpCounter++;
                }

                break;
            case CALL:
                Element firstArg = ((CallInstruction) instr.getRhs()).getFirstArg();
                Element secondArg = ((CallInstruction) instr.getRhs()).getSecondArg();

                if(firstArg.toString().equals("ARRAYREF")){
                    if(((CallInstruction) instr.getRhs()).getNumOperands() > 1)
                    {
                        stackLimit = Integer.max(stackLimit,1);
                        auxvirtualReg = method.getVarTable().get(dest).getVirtualReg();
                        Element d = (((CallInstruction) instr.getRhs()).getListOfOperands().get(0));
                        N = Integer.parseInt(((LiteralElement) d).getLiteral());
                        if(N <=5) methodString.append("\n\n\t\ticonst_"+N);
                        else {
                            methodString.append("\n\n\t\tbipush "+ N);
                        }
                        methodString.append("\n\t\tanewarray "+ollirClass.getClassName());
                        if(auxvirtualReg <=3) methodString.append("\n\t\tastore_"+auxvirtualReg);
                        else {
                            methodString.append("\n\t\tastore "+auxvirtualReg);
                        }
                    }
                    else{
                        stackLimit = Integer.max(stackLimit,1);
                        auxvirtualReg = method.getVarTable().get(dest).getVirtualReg() ;
                        int valuevirtualReg = method.getVarTable().get(((Operand) firstArg).getName()).getVirtualReg();

                        if(valuevirtualReg <=3) methodString.append("\n\n\t\taload_"+valuevirtualReg);
                        else {
                            methodString.append("\n\n\t\taload "+valuevirtualReg);
                        }
                        methodString.append("\n\t\tarraylength");
                        if(auxvirtualReg <=3) methodString.append("\n\t\tistore_"+auxvirtualReg);
                        else {
                            methodString.append("\n\t\tistore "+auxvirtualReg);
                        }
                    }
                }
                else if(instr.getDest().getType().toString().equals("OBJECTREF")){
                    stackLimit = Integer.max(stackLimit,1);

                    methodString.append("\n\n\t\tnew "+((Operand)firstArg).getName());
                    methodString.append("\n\t\tdup");

                }
                else if(instr.getDest().getType().toString().equals("INT32")) {
                    for (var oper :((CallInstruction) instr.getRhs()).getListOfOperands()) {
                        if(oper.isLiteral()){
                            N = Integer.parseInt(((LiteralElement) oper).getLiteral());
                            if(N <=5) methodString.append("\n\n\t\ticonst_"+N);
                            else {
                                methodString.append("\n\n\t\tbipush "+ N);
                            }
                        }else{
                            auxvirtualReg = method.getVarTable().get(((Operand)oper).getName()).getVirtualReg() ;
                            if(auxvirtualReg <=3) methodString.append("\n\n\t\tiload_"+auxvirtualReg);
                            else {
                                methodString.append("\n\n\t\tiload "+auxvirtualReg);
                            }
                        }
                    }
                    String aux = ((LiteralElement)secondArg).getLiteral();
                    String Func = aux.replace("\"","");
                    if(Func.equals("<init>")) {
                        methodString.append("\n\t\tinvokespecial ");
                    }
                    else{
                        int valuevirtualReg = method.getVarTable().get(((Operand) firstArg).getName()).getVirtualReg();

                        if(((Operand) firstArg).getName().equals("this")){
                            methodString.append("\n\t\taload_0");
                        }
                        else{
                            if(valuevirtualReg <=3) methodString.append("\n\t\taload_"+valuevirtualReg);
                            else {
                                methodString.append("\n\t\taload "+valuevirtualReg);
                            }
                        }

                        methodString.append("\n\t\tinvokevirtual ");
                    }
                    methodString.append(Func);
                    methodString.append("(");
                    for (var oper :((CallInstruction) instr.getRhs()).getListOfOperands()) {
                        if(oper.isLiteral()){
                            methodString.append("I");
                        }else if (oper.getType().toString().equals("INT32")){
                            methodString.append("I");
                        }
                    }
                    methodString.append(")V\n");

                }
                break;
            case GETFIELD:
                stackLimit = Integer.max(stackLimit,2);
                int destvirtualReg = method.getVarTable().get(dest).getVirtualReg() ;
                o1 = (Operand)(((GetFieldInstruction) instr.getRhs()).getFirstOperand());
                o2 = (Operand)(((GetFieldInstruction) instr.getRhs()).getSecondOperand());
                auxvirtualReg = method.getVarTable().get(o1.getName()).getVirtualReg();
                if(o1.getName().equals("this")){
                    methodString.append("\n\n\t\taload_0");
                    methodString.append("\n\t\tgetfield I " +o2.getName());
                    if(destvirtualReg <=3) methodString.append("\n\t\tistore_"+destvirtualReg);
                    else {
                        methodString.append("\n\t\tistore " + destvirtualReg);
                    }
                }else{
                    if(auxvirtualReg <=3) methodString.append("\n\n\t\taload_"+auxvirtualReg);
                    else {
                        methodString.append("\n\n\t\taload "+auxvirtualReg);
                    }
                    methodString.append("\n\t\tgetfield I " +o2.getName());
                    if(destvirtualReg <=3) methodString.append("\n\t\tistore_"+destvirtualReg);
                    else {
                        methodString.append("\n\t\tistore " + destvirtualReg);
                    }

                }
            break;
            case PUTFIELD:
                stackLimit = Integer.max(stackLimit,1);
                o1 = (Operand)(((GetFieldInstruction) instr.getRhs()).getFirstOperand());
                o2 = (Operand)(((GetFieldInstruction) instr.getRhs()).getSecondOperand());
                auxvirtualReg = method.getVarTable().get(o1.getName()).getVirtualReg();
                if(o1.getName().equals("this")){
                    methodString.append("\n\n\t\taload_0");
                    methodString.append("\n\t\tputfield " +o2.getName() +" I");
                }else{
                    if(auxvirtualReg <=3) methodString.append("\n\n\t\taload_"+auxvirtualReg);
                    else {
                        methodString.append("\n\n\t\taload "+auxvirtualReg);
                    }
                    methodString.append("\n\t\tputfield " +o2.getName() +" I");
                }
            break;
        }

    }

    private void generateInst(ReturnInstruction instr, Method method) {
        if (!instr.hasReturnValue()) methodString.append("\n\t\treturn");
        else {
            stackLimit = Integer.max(stackLimit,1);
            Element e1 = instr.getOperand();
            int virtualReg;

            if(e1.isLiteral()){
                int N = Integer.parseInt(((LiteralElement) e1).getLiteral());
                if(N <=5) methodString.append("\n\n\t\ticonst_"+N);
                else {
                    methodString.append("\n\n\t\tbipush "+ N);
                }
                methodString.append("\n\t\tireturn\n");
            }
            else{
                String o1 = ((Operand) e1).getName();
                String type = e1.getType().toString();
                virtualReg = method.getVarTable().get(o1).getVirtualReg() ;
                if(type.equals("OBJECTREF")||type.equals("ARRAYREF"))
                {
                    if(virtualReg <=3) methodString.append("\n\n\t\taload_"+virtualReg);
                    else {
                        methodString.append("\n\n\t\taload "+ virtualReg);
                    }
                    methodString.append("\n\t\tareturn\n");
                }
                else if(type.equals("BOOLEAN")||type.equals("INT32")){
                    if(virtualReg <=3) methodString.append("\n\n\t\tiload_"+virtualReg);
                    else {
                        methodString.append("\n\n\t\tiload "+ virtualReg);
                    }
                    methodString.append("\n\t\tireturn\n");
                }
                else{
                    methodString.append("\n\n\t\treturn\n");
                }
            }
        }
    }

    private void generateInst(GotoInstruction instr, Method method) {
        methodString.append("\n\n\t\tgoto "+instr.getLabel());
    }

    private void generateInst(CondBranchInstruction instr, Method method) {
        stackLimit = Integer.max(stackLimit,2);
        Element e1 = instr.getLeftOperand();
        Element e2 = instr.getRightOperand();
        int N;
        if(e1.isLiteral())
        {
            N = Integer.parseInt(((LiteralElement) e1).getLiteral());
            if(N <=5) methodString.append("\n\n\t\ticonst_"+N);
            else {
                methodString.append("\n\n\t\tbipush "+ N);
            }
        }
        else{
            String o1 = ((Operand) e1).getName();
            int e1VirtualReg = method.getVarTable().get(o1).getVirtualReg() ;
            if(e1VirtualReg <=3) methodString.append("\n\t\tiload_"+e1VirtualReg);
            else {
                methodString.append("\n\t\tiload "+ e1VirtualReg);
            }
        }
        String o2;
        if(e2.isLiteral())
        {
            N = Integer.parseInt(((LiteralElement) e2).getLiteral());
            if(N <=5) methodString.append("\n\n\t\ticonst_"+N);
            else {
                methodString.append("\n\n\t\tbipush "+ N);
            }
        }else{
            o2 = ((Operand) e2).getName();
            int e2VirtualReg = method.getVarTable().get(o2).getVirtualReg() ;
            if(e2VirtualReg <=3) methodString.append("\n\t\tiload_"+e2VirtualReg);
            else {
                methodString.append("\n\t\tiload "+ e2VirtualReg);
            }
        }
        OperationType operation = instr.getCondOperation().getOpType();
        String cond = instr.getCondOperation().getOpType().toString();
        if(operation.toString().equals("ANDB")){

            methodString.append("\n\t\tiand");
            cond = "EQ";

        }
        methodString.append("\n\t\tif_icmp");

        if(cond.equals("LTH")){
            methodString.append("lt ");
        }else if(cond.equals("GTH")){
            methodString.append("gt ");
        }else if(cond.equals("EQ")){
            methodString.append("eq ");
        }else if(cond.equals("NEQ")){
            methodString.append("ne ");
        }else if(cond.equals("LTE")){
            methodString.append("le ");
        }else if(cond.equals("GTE")){
            methodString.append("ge ");
        }
        methodString.append(instr.getLabel());
    }


    private void getMethodReturn(Method method) {
        if(method.getMethodName().equals("main")) methodString.append("\n\n\t\treturn");
    }

}
