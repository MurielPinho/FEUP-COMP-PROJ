import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.specs.comp.ollir.*;

import org.specs.comp.ollir.AssignInstruction;

import pt.up.fe.comp.jmm.jasmin.JasminBackend;
import pt.up.fe.comp.jmm.jasmin.JasminResult;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.Stage;

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
    private ClassUnit ollirClass;

    @Override
    public JasminResult toJasmin(OllirResult ollirResult) {
        ollirClass = ollirResult.getOllirClass();
        try {

            // Example of what you can do with the OLLIR class
            ollirClass.checkMethodLabels(); // check the use of labels in the OLLIR loaded
            //ollirClass.buildCFGs(); // build the CFG of each method
            //ollirClass.outputCFGs(); // output to .dot files the CFGs, one per method
            ollirClass.buildVarTables(); // build the table of variables for each method
            ollirClass.show(); // print to console main information about the input OLLIR


            generateHeader();
            generateGlobalFields();
            generateInit();
            generateMethods();

            try {
                FileWriter myWriter = new FileWriter("generatedJasmin/"+ollirClass.getClassName()+".j");
                myWriter.write(jasminString.toString());
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // // Convert the OLLIR to a String containing the equivalent Jasmin code
             String jasminCode = jasminString.toString(); // Convert node ...

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
            jasminString.append("\n.method public ");
            jasminString.append("static ");
            jasminString.append(method.getMethodName());
            getMethodParameters(method);
            getMethodLimits(method);
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
        String stack = "20 ; TBD";
        int locals = method.getVarTable().size() + method.getParams().size() + 1;
        jasminString.append("\n\t\t.limit stack ");
        jasminString.append(stack);
        jasminString.append("\n\t\t.limit locals ");
        jasminString.append(locals);
    }

    private void getMethodBody(Method method) throws OllirErrorException {



        for (var instr:method.getInstructions()) {
            HashMap<String, Instruction> map = method.getLabels();
            for (Map.Entry<String, Instruction> entry : map.entrySet()) {
                String key = entry.getKey();
                if(method.getLabels().get(key)==instr) jasminString.append("\n\n\t"+key+":");
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
    }

    private void generateInst(CallInstruction instr, Method method) {
        Element e1 = instr.getFirstArg();
        Element e2 = instr.getSecondArg();
        jasminString.append("\n\n\t\tinvokestatic ");
        if(e1.isLiteral()){
            String literal = ((LiteralElement)e1).getLiteral();
            String newLit = literal.replace("\"","");
            jasminString.append(newLit);
        }
        else{
            Operand o1 = (Operand) e1;
            jasminString.append(o1.getName());
        }
        jasminString.append("/");
        if(e2.isLiteral())
        {
            String literal = ((LiteralElement)e2).getLiteral();
            String newLit = literal.replace("\"","");
            jasminString.append(newLit);
        }
        else{
            Operand o2 = (Operand) e2;
            jasminString.append(o2.getName());
        }
        jasminString.append("(");
        //for (var oper :instr.getListOfOperands()) { }
        jasminString.append(")V\n");


    }

    private void generateInst(AssignInstruction instr, Method method) {
        InstructionType rhs = instr.getRhs().getInstType();
        String dest = ((Operand)instr.getDest()).getName();
        int virtualReg = method.getVarTable().get(dest).getVirtualReg() -1 ;
        switch(rhs){
            case NOPER:
                Element operand = ((SingleOpInstruction) instr.getRhs()).getSingleOperand();
                if (operand.isLiteral()) {
                    jasminString.append("\n\n\t\ticonst_" + ((LiteralElement) operand).getLiteral());
                    if(virtualReg <=3) jasminString.append("\n\t\tistore_"+virtualReg);
                    else {
                        jasminString.append("\n\t\tistore "+virtualReg);
                    }
                }
                else {
                    Operand o1 = (Operand)((SingleOpInstruction) instr.getRhs()).getSingleOperand();
                    int auxvirtualReg = method.getVarTable().get(dest).getVirtualReg() -1 ;
                    int iVirtualReg = method.getVarTable().get(o1.getName()).getVirtualReg() -1 ;

                    if(o1.getName().equals("i")){
                        jasminString.append("\n\n\t\taload_0");
                    }
                    else jasminString.append("\n");
                    if(iVirtualReg <=3) jasminString.append("\n\t\tiload_"+iVirtualReg);
                    else {
                        jasminString.append("\n\t\tiload "+iVirtualReg);
                    }
                    if(o1.getName().equals("i")){
                        jasminString.append("\n\t\tiaload");
                    }
                    if(auxvirtualReg <=3) jasminString.append("\n\t\tistore_"+auxvirtualReg);
                    else {
                        jasminString.append("\n\t\tistore "+auxvirtualReg);
                    }

                }
                break;
            case BINARYOPER:

                Element e1 = ((BinaryOpInstruction) instr.getRhs()).getLeftOperand();
                Element e2 = ((BinaryOpInstruction) instr.getRhs()).getRightOperand();
                OperationType operation = ((UnaryOpInstruction) instr.getRhs()).getUnaryOperation().getOpType();
                if(dest.equals("i") && operation.toString() =="ADD" && e2.isLiteral()) {
                    Operand o1 = (Operand) e1;
                    int auxvirtualReg = method.getVarTable().get(o1.getName()).getVirtualReg() -1 ;
                    jasminString.append("\n\n\t\tiinc "+auxvirtualReg+" "+((LiteralElement) e2).getLiteral());
                    break;
                }

                if(e1.isLiteral()) {
                    jasminString.append("\n\n\t\ticonst_"+((LiteralElement) e1).getLiteral());
                } else {
                    Operand o1 = (Operand) e1;
                    int auxvirtualReg = method.getVarTable().get(o1.getName()).getVirtualReg() -1 ;
                    if(auxvirtualReg <=3) jasminString.append("\n\n\t\tiload_"+auxvirtualReg);
                    else {
                        jasminString.append("\n\n\t\tiload "+auxvirtualReg);
                    }

                }


                if(e2.isLiteral()) {
                    jasminString.append("\n\t\ticonst_"+((LiteralElement) e2).getLiteral());
                } else {
                    Operand o1 = (Operand) e2;
                    int auxvirtualReg = method.getVarTable().get(o1.getName()).getVirtualReg() -1 ;
                    if(auxvirtualReg <=3) jasminString.append("\n\t\tiload_"+auxvirtualReg);
                    else {
                        jasminString.append("\n\t\tiload "+auxvirtualReg);
                    }


                }
                int auxvirtualReg = method.getVarTable().get(dest).getVirtualReg() -1 ;
                operation = ((UnaryOpInstruction) instr.getRhs()).getUnaryOperation().getOpType();
                jasminString.append("\n\t\ti"+ operation.toString().toLowerCase(Locale.ROOT));
                if(auxvirtualReg <=3) jasminString.append("\n\t\tistore_"+auxvirtualReg);
                else {
                    jasminString.append("\n\t\tistore "+auxvirtualReg);
                }
                break;
            case CALL:
                Element firstArg = ((CallInstruction) instr.getRhs()).getFirstArg();
                Element secondArg = ((CallInstruction) instr.getRhs()).getSecondArg();

                if(firstArg.toString().equals("ARRAYREF")){
                auxvirtualReg = method.getVarTable().get(dest).getVirtualReg() -1 ;
                jasminString.append("\n\n\t\taload_0");
                jasminString.append("\n\t\tarraylength");
                    if(auxvirtualReg <=3) jasminString.append("\n\t\tistore_"+auxvirtualReg);
                    else {
                        jasminString.append("\n\t\tistore "+auxvirtualReg);
                    }
                }
                else {
                    /*for (var oper :((CallInstruction) instr.getRhs()).getListOfOperands()) {
                        if(oper.isLiteral()){
                            jasminString.append(((LiteralElement) oper).getLiteral());
                        }
                        else {
                            jasminString.append(((Operand)oper).getName());
                        }
                    }*/

                }
                break;
            case GETFIELD:
                int destvirtualReg = method.getVarTable().get(dest).getVirtualReg() -1 ;
                Element e = ((GetFieldInstruction) instr.getRhs()).getSecondOperand();
                Operand o1 = (Operand) e;
                auxvirtualReg = method.getVarTable().get(o1.getName()).getVirtualReg() -1;
                if(auxvirtualReg <=3) jasminString.append("\n\n\t\tiload_"+auxvirtualReg);
                else {
                    jasminString.append("\n\t\tiload "+ auxvirtualReg);
                }
                if(destvirtualReg <=3) jasminString.append("\n\t\tistore_"+destvirtualReg);
                else {
                    jasminString.append("\n\t\tistore " + destvirtualReg);
                }

        }

    }

    private void generateInst(ReturnInstruction instr, Method method) {
        if (!instr.hasReturnValue()) jasminString.append("\n\t\treturn");
        else {
            Element e1 = instr.getOperand();
            String o1 = ((Operand) e1).getName();
            int virtualReg = method.getVarTable().get(o1).getVirtualReg() -1 ;
            jasminString.append("\n\n\t\tiload_" + virtualReg);
            jasminString.append("\n\t\tireturn\n");
        }

    }

    private void generateInst(GotoInstruction instr, Method method) {
        jasminString.append("\n\n\t\tgoto "+instr.getLabel());


    }

    private void generateInst(CondBranchInstruction instr, Method method) {

        Element e1 = instr.getLeftOperand();
        Element e2 = instr.getRightOperand();
        String o1 = ((Operand) e1).getName();
        int e1VirtualReg = method.getVarTable().get(o1).getVirtualReg() -1 ;
        jasminString.append("\n\n\t\tiload_" + e1VirtualReg);
        String o2;
        if(e2.isLiteral())
        {
            jasminString.append("\n\t\ticonst_"+((LiteralElement) e2).getLiteral());
        }else{
            o2 = ((Operand) e2).getName();
            int e2VirtualReg = method.getVarTable().get(o2).getVirtualReg() -1 ;
            if(e2VirtualReg <=3) jasminString.append("\n\t\tiload_"+e2VirtualReg);
            else {
                jasminString.append("\n\t\tiload "+ e2VirtualReg);
            }
        }
        jasminString.append("\n\t\tif_icmp");
        String cond = instr.getCondOperation().getOpType().toString();
        if(cond.equals("LTH")){
            jasminString.append("lt ");
        }else if(cond.equals("GTH")){
            jasminString.append("gt ");
        }else if(cond.equals("EQ")){
            jasminString.append("eq ");
        }else if(cond.equals("NEQ")){
            jasminString.append("ne ");
        }else if(cond.equals("LTE")){
            jasminString.append("le ");
        }else if(cond.equals("GTE")){
            jasminString.append("ge ");
        }
        jasminString.append(instr.getLabel());
    }


    private void getMethodReturn(Method method) {
        if(method.getMethodName().equals("main")) jasminString.append("\n\n\t\treturn");
    }





}
