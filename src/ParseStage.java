import pt.up.fe.comp.jmm.JmmParser;
import pt.up.fe.comp.jmm.JmmParserResult;

import java.io.*;

public class ParseStage implements JmmParser {
    @Override
	public JmmParserResult parse(String jmmCode) {
        try {
            Parser myProg = new Parser(new StringReader(jmmCode));
            SimpleNode root = myProg.Program(); // returns reference to root node

            root.dump(""); // prints the tree on the screen
            this.createJSONfile(root);

            myProg.printErrorMessages();

            return new JmmParserResult(root, myProg.getReports());
        } 
        catch(Exception e) {
            throw new RuntimeException("Error while parsing", e);
        }
	}

    private void createJSONfile(SimpleNode parsedResult) {
        try {
            System.out.println("\n#######################");
            String filename = ((SimpleNode)parsedResult.jjtGetChild(1)).get("val");
            System.out.println("Creating JSON file...");

            File file = new File("./"+filename+".json");
            file.createNewFile();

            FileWriter myWriter = new FileWriter(file);
            myWriter.write(parsedResult.toJson());
            myWriter.close();

            System.out.println("JSON file created at: "+"./"+filename+".json");
            System.out.println("#######################\n");
        }
        catch(Exception e){
            System.out.println("Error creating JSON file!");
            System.out.println("#######################\n");
        }
    }
}
