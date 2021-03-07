
import pt.up.fe.comp.jmm.JmmParser;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.report.Report;

import java.util.Arrays;
import java.util.ArrayList;
import java.io.StringReader;

public class Main implements JmmParser {


/*	public void parse(String jmmCode) {

		Parser myProg = new Parser(new StringReader(jmmCode));
		//SimpleNode root = myProg.Program(); // returns reference to root node

		//root.dump(""); // prints the tree on the screen

		//return new JmmParserResult(root, new ArrayList<Report>());
	}*/

    public static void main(String[] args) throws ParseException {
        System.out.println("Executing with args: " + Arrays.toString(args));
        if (args[0].contains("fail")) {
            throw new RuntimeException("It's supposed to fail");
        }
        Parser myProg = new Parser(new StringReader("../docs/teste1.jmm"));
       /* Main main = new Main();
        JmmParserResult jmmParserResult = main.parse("../docs/teste1.jmm");*/
    }


    @Override
    public JmmParserResult parse(String jmmCode) {
        return null;
    }
}