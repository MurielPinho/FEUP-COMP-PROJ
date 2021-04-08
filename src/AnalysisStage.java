import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.analysis.JmmAnalysis;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.ast.examples.ExamplePostorderVisitor;
import pt.up.fe.comp.jmm.ast.examples.ExamplePreorderVisitor;
import pt.up.fe.comp.jmm.ast.examples.ExamplePrintVariables;
import pt.up.fe.comp.jmm.ast.examples.ExampleVisitor;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;
import pt.up.fe.comp.jmm.analysis.table.*;

public class AnalysisStage implements JmmAnalysis {

    @Override
    public JmmSemanticsResult semanticAnalysis(JmmParserResult parserResult) {
        // if (TestUtils.getNumReports(parserResult.getReports(), ReportType.ERROR) > 0) {
        //     System.out.println("OI");
        //     var errorReport = new Report(ReportType.ERROR, Stage.SEMANTIC, -1,
        //             "Started semantic analysis but there are errors from previous stage");
        //     return new JmmSemanticsResult(parserResult, null, Arrays.asList(errorReport));
        // }

        // if (parserResult.getRootNode() == null) {
        //     var errorReport = new Report(ReportType.ERROR, Stage.SEMANTIC, -1,
        //             "Started semantic analysis but AST root node is null");
        //     return new JmmSemanticsResult(parserResult, null, Arrays.asList(errorReport));
        // }

        // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
        //            BUILD SYMBOL TABLE
        // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

        JmmNode node = parserResult.getRootNode();
        // System.out.println(node);

        System.out.println("\n#######################\n");
        System.out.println("Building Symbol Table...");

        RootSymbolTable rootSymbolTable = new RootSymbolTable();
        rootSymbolTable.buildSymbolTable(node);
        
        System.out.println("Symbol Table built");
        System.out.println("\n#######################\n");
        System.out.println( rootSymbolTable.print());
        System.out.println("\n#######################\n");

        // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
        //            SEMANTIC ANALYSIS
        // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

        // System.out.println("Dump tree with Visitor where you control tree traversal");
        // ExampleVisitor visitor = new ExampleVisitor("VarDeclaration", "val");
        // System.out.println(visitor.visit(node, ""));

        System.out.println("Dump tree with Visitor that automatically performs preorder tree traversal");
        var preOrderVisitor = new ExamplePreorderVisitor("ArrayIndex", "val");
        // System.out.println(preOrderVisitor.visit(node, ""));
        preOrderVisitor.visit(node, "");

        // System.out.println(
        //         "Create histogram of node kinds with Visitor that automatically performs postorder tree traversal");
        // var postOrderVisitor = new ExamplePostorderVisitor();
        // var kindCount = new HashMap<String, Integer>();
        // postOrderVisitor.visit(node, kindCount);
        // System.out.println("Kinds count: " + kindCount + "\n");

        // System.out.println(
        //         "Print variables name and line, and their corresponding parent with Visitor that automatically performs preorder tree traversal");
        // var varPrinter = new ExamplePrintVariables("Variable", "name", "line");
        // varPrinter.visit(node, null);

        // No Symbol Table being calculated yet
        return new JmmSemanticsResult(parserResult, rootSymbolTable, new ArrayList<>());

    }

}