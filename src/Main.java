
import pt.up.fe.comp.jmm.JmmParser;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.report.Report;

import java.util.Arrays;
import java.util.ArrayList;
import java.io.StringReader;
import java.util.*;
import java.io.*;

public class Main implements JmmParser {

    @Override
	public JmmParserResult parse(String jmmCode) {
        try {
            System.setIn(new FileInputStream(jmmCode));
            Parser myProg = new Parser(System.in);
            SimpleNode root = myProg.Program(); // returns reference to root node

            // root.dump(""); // prints the tree on the screen
            root.toJson();

            return new JmmParserResult(root, new ArrayList<Report>());
        } 
        catch(Exception e) {
            throw new RuntimeException("Error while parsing", e);
        }
	}

    public static void main(String[] args) {
        System.out.println("Executing with args: " + Arrays.toString(args));

        Main main = new Main();
        String file = main.parseInput(args);

        JmmParserResult jmmParserResult = main.parse(file);
    }

    public String parseInput(String[] args){
        if(args.length != 0){
            if (args[0].contains("fail")) throw new RuntimeException("It's supposed to fail");
            else return args[0];
        }
        else return "docs/teste1.jmm";
    }
}