import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.analysis.JmmAnalysis;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.ast.examples.AnalysisSemanticInfo;
import pt.up.fe.comp.jmm.ast.examples.AnalysisSemanticVisitor;
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

        AnalysisSemanticInfo analysisSemanticInfo = new AnalysisSemanticInfo(rootSymbolTable);
        AnalysisSemanticVisitor visitor = new AnalysisSemanticVisitor();
        visitor.visit(node, analysisSemanticInfo);

        for(Report report: analysisSemanticInfo.getReports()) System.out.println(report);

        // No Symbol Table being calculated yet
        return new JmmSemanticsResult(parserResult, rootSymbolTable, new ArrayList<>());

    }

}