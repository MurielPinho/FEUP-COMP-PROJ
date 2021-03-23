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

            root.dump(""); // prints the tree on the screen
            // String json = root.toJson();
            // this.createJSONfile(json);

            myProg.printErrorMessages();

            return new JmmParserResult(root, new ArrayList<Report>());
        } 
        catch(Exception e) {
            throw new RuntimeException("Error while parsing", e);
        }
	}

    public static void main(String[] args) {
        System.out.println("\n#######################\n");
        System.out.println("Executing with args: " + Arrays.toString(args));
        System.out.println("\n#######################\n");

        Main main = new Main();
        String file = main.parseInput(args);

        JmmParserResult jmmParserResult = main.parse(file);
    }

    public String parseInput(String[] args){
        if(args.length != 0) return args[0];
        else return "docs/teste1.jmm";
    }

    private void createJSONfile(String json) {
        try {
            System.out.println("\n#######################\n");
            System.out.println("Craeting JSON file...");
            
            File file = new File("./generated_file.json");
            file.createNewFile();

            FileWriter myWriter = new FileWriter(file);
            myWriter.write(json);
            myWriter.close();

            System.out.println("JSON file created at: ./generated_file.json");
            System.out.println("\n#######################\n");
        }
        catch(Exception e){
            System.out.println("Error creating JSON file!");
            System.out.println("\n#######################\n");
        }
    }
}