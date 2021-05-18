import java.util.ArrayList;

import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.analysis.JmmAnalysis;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.ast.examples.AnalysisSemanticInfo;
import pt.up.fe.comp.jmm.ast.examples.AnalysisSemanticVisitor;
import pt.up.fe.comp.jmm.report.Report;
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

        System.out.println("\n#######################");
        System.out.println("Building Symbol Table...");

        RootSymbolTable rootSymbolTable = new RootSymbolTable();
        rootSymbolTable.buildSymbolTable(node);
        
        System.out.println("Symbol Table built");
        System.out.println("#######################\n");

        // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
        //            SEMANTIC ANALYSIS
        // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

        AnalysisSemanticInfo analysisSemanticInfo = new AnalysisSemanticInfo(rootSymbolTable);
        AnalysisSemanticVisitor visitor = new AnalysisSemanticVisitor();
        visitor.visit(node, analysisSemanticInfo);

        System.out.println("\n#######################");
        System.out.println("      SEMANTIC ERRORS      ");
        System.out.println("#######################\n");

        if (analysisSemanticInfo.getReports().size() == 0) System.out.println("No semantic errors were found!");

        for(Report report: analysisSemanticInfo.getReports()) System.out.println(report);

        System.out.println("#######################\n");

        return new JmmSemanticsResult(parserResult, rootSymbolTable, analysisSemanticInfo.getReports());

    }

}