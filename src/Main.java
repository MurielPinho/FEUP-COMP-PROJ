import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.println("\n#######################\n");
        System.out.println("Executing with args: " + Arrays.toString(args));
        System.out.println("\n#######################\n");

        Main main = new Main();
        String file = main.parseInput(args);

        JmmParserResult jmmParserResult = new ParseStage().parse(file);
        JmmSemanticsResult jmmSemanticsResult = new AnalysisStage().semanticAnalysis(jmmParserResult);
    }

    public String parseInput(String[] args){
        if(args.length != 0) return args[0];
        else return "docs/teste1.jmm";
    }
}