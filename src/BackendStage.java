import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            ollirClass.buildCFGs(); // build the CFG of each method
            ollirClass.outputCFGs(); // output to .dot files the CFGs, one per method
            ollirClass.buildVarTables(); // build the table of variables for each method
            ollirClass.show(); // print to console main information about the input OLLIR


            generateHeader();
            generateGlobalFields();
            generateInit();
            generateMethods();

            System.out.println("\n"+jasminString.toString());

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
            jasminString.append(".field " + field.getFieldAccessModifier() + " " + field.getFieldName() + " " + field.getFieldType()+ "\n") ;
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
            if(method.isStaticMethod()) jasminString.append("static ");
            jasminString.append(method.getMethodName());
            getMethodParameters(method);
            getMethodLimits(method);
            getMethodBody(method);
            jasminString.append("\n.end method\n");

        }
    }



    private void getMethodParameters(Method method) {
        jasminString.append("(");

        for (var param: method.getParams()) {
            if(param.getType().toString() == "INT32")  jasminString.append("I");
            else if (param.getType().toString() == "ARRAYREF") jasminString.append("[I");
        }
        jasminString.append(")");
        if(method.getReturnType().toString() == "VOID"){
            jasminString.append("V");
        }
        else jasminString.append("I");



    }

    private void getMethodLimits(Method method) {
        jasminString.append("\n\t.limit locals ");
        jasminString.append(99);
        jasminString.append("\n\t.limit stack ");
        jasminString.append(99);
    }

    private void getMethodBody(Method method) {

        for (var instr:method.getInstructions()) {
            InstructionType instType = instr.getInstType();
            switch(instType){
                case ASSIGN:
                    generateInst((AssignInstruction) instr,method);
                    break;
                case RETURN:
                    generateInst((ReturnInstruction)instr,method);
                    break;
                default:
                    //jasminString.append("\n\n\t"+instType.toString());
            }
            getMethodReturn(method);

        }
    }

    private void generateInst(AssignInstruction instr, Method method) {
        InstructionType rhs = instr.getRhs().getInstType();
        String dest = ((Operand)instr.getDest()).getName();
        int virtualReg = method.getVarTable().get(dest).getVirtualReg() -1 ;
        switch(rhs){
            case NOPER:
                Element operand = ((SingleOpInstruction) instr.getRhs()).getSingleOperand();
                if (operand.isLiteral()) {
                    jasminString.append("\n\n\ticonst_" + ((LiteralElement) operand).getLiteral());
                    if(virtualReg <=3) jasminString.append("\n\tistore_"+virtualReg);
                    else {
                        jasminString.append("\n\tistore "+virtualReg);
                    }
                } else {

                    int auxvirtualReg = method.getVarTable().get(dest).getVirtualReg() -1 ;
                    jasminString.append("\n\n\taload_0");

                    if(auxvirtualReg <=3) jasminString.append("\n\tiload_"+auxvirtualReg);
                    else {
                        jasminString.append("\n\tiload "+auxvirtualReg);
                    }
                    jasminString.append("\n\tiaload");

                }
                break;
            case BINARYOPER:

                Element e1 = ((BinaryOpInstruction) instr.getRhs()).getLeftOperand();

                if(e1.isLiteral()) { // if the e1 is not a literal, then it is a variable
                    jasminString.append("\n\n\ticonst_"+((LiteralElement) e1).getLiteral());
                } else {
                    Operand o1 = (Operand) e1;
                    int auxvirtualReg = method.getVarTable().get(o1.getName()).getVirtualReg() -1 ;
                    if(auxvirtualReg <=3) jasminString.append("\n\n\tiload_"+auxvirtualReg);
                    else {
                        jasminString.append("\n\n\tiload "+auxvirtualReg);
                    }

                }
                e1 = ((BinaryOpInstruction) instr.getRhs()).getRightOperand();

                if(e1.isLiteral()) { // if the e1 is not a literal, then it is a variable
                    jasminString.append("\n\ticonst_"+((LiteralElement) e1).getLiteral());
                } else {
                    Operand o1 = (Operand) e1;
                    int auxvirtualReg = method.getVarTable().get(o1.getName()).getVirtualReg() -1 ;
                    if(auxvirtualReg <=3) jasminString.append("\n\tiload_"+auxvirtualReg);
                    else {
                        jasminString.append("\n\tiload "+auxvirtualReg);
                    }


                }
                int auxvirtualReg = method.getVarTable().get(dest).getVirtualReg() -1 ;
                OperationType operation = ((UnaryOpInstruction) instr.getRhs()).getUnaryOperation().getOpType();
                if(operation.toString() =="ADD") jasminString.append("\n\tiadd");
                if(auxvirtualReg <=3) jasminString.append("\n\tistore_"+auxvirtualReg);
                else {
                    jasminString.append("\n\tistore "+auxvirtualReg);
                }
                break;
            case CALL:
                auxvirtualReg = method.getVarTable().get(dest).getVirtualReg() -1 ;

                if(auxvirtualReg <=3) jasminString.append("\n\n\tiload_"+auxvirtualReg);
                else {
                    jasminString.append("\n\n\tiload "+auxvirtualReg);
                }
                jasminString.append("\n\taload_0");
                jasminString.append("\n\tarraylength");
                break;


        }

    }

    private void generateInst(ReturnInstruction instr, Method method) {
        if (!instr.hasReturnValue()) jasminString.append("\n\treturn");
        else {
            Element e1 = instr.getOperand();
            String o1 = ((Operand) e1).getName();
            int virtualReg = method.getVarTable().get(o1).getVirtualReg() -1 ;
            jasminString.append("\n\n\tiload_" + virtualReg);
            jasminString.append("\n\tireturn\n");
        }

    }


    private void getMethodReturn(Method method) {
        if(method.getMethodName() == "main") jasminString.append("\n\treturn");
    }





}
