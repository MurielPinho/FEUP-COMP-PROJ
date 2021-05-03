import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.specs.util.SpecsIo;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.println("\n?????????????????????????????????");
        System.out.println("???????? - HOW TO RUN - ?????????");
        System.out.println("?????????????????????????????????\n");
        System.out.println("RUNNING EXAMPLE: .\\comp2021-6c.bat Main fixtures/public/HelloWorld.jmm");
        System.out.println("\n?????????????????????????????????\n");

        System.out.println("#######################\n");
        System.out.println("Executing with args: " + Arrays.toString(args));
        System.out.println("\n#######################\n");

        Main main = new Main();
        String file = main.parseInput(args);

        JmmParserResult jmmParserResult = new ParseStage().parse(SpecsIo.getResource(file));
        JmmSemanticsResult jmmSemanticsResult = new AnalysisStage().semanticAnalysis(jmmParserResult);
        OllirResult ollirResult = new OptimizationStage().toOllir(jmmSemanticsResult);

        JmmNode node = jmmSemanticsResult.getRootNode();

        System.out.println(node.toTree());
    }

    public String parseInput(String[] args){
        if(args.length != 0) return args[0];
        else return "fixtures/public/Test.jmm";
    }
}