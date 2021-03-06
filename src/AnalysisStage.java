import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.analysis.JmmAnalysis;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.ast.examples.AnalysisSemanticInfo;
import pt.up.fe.comp.jmm.ast.examples.AnalysisSemanticVisitor;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.analysis.table.*;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;

public class AnalysisStage implements JmmAnalysis {
    @Override
    public JmmSemanticsResult semanticAnalysis(JmmParserResult parserResult) {
        if (TestUtils.getNumReports(parserResult.getReports(), ReportType.ERROR) > 0) {
            var errorReport = new Report(ReportType.ERROR, Stage.SEMANTIC, -1,
                    "Started semantic analysis but there are errors from previous stage");
            return new JmmSemanticsResult(parserResult, null, Arrays.asList(errorReport));
        }

        if (parserResult.getRootNode() == null) {
            var errorReport = new Report(ReportType.ERROR, Stage.SEMANTIC, -1,
                    "Started semantic analysis but AST root node is null");
            return new JmmSemanticsResult(parserResult, null, Arrays.asList(errorReport));
        }

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

        this.createSymbolTablefile(rootSymbolTable);

        return new JmmSemanticsResult(parserResult, rootSymbolTable, analysisSemanticInfo.getReports());

    }

    private void createSymbolTablefile(RootSymbolTable rootSymbolTable) {
        try {
            System.out.println("\n#######################");

            System.out.println("Creating SymbolTable file...");
            String filename = rootSymbolTable.getClassName();
            File file = new File("./"+filename+".symbols.txt");
            file.createNewFile();
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(rootSymbolTable.print());
            myWriter.close();

            System.out.println("JSON file created at: ./"+filename+".symbols.txt");
            System.out.println("#######################\n");
        }
        catch(Exception e){
            System.out.println("Error creating SymbolTable file!");
            System.out.println("#######################\n");
        }
    }

}