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
            String json = root.toJson();
            this.createJSONfile(json);

            myProg.printErrorMessages();

            return new JmmParserResult(root, myProg.getReports());
        } 
        catch(Exception e) {
            throw new RuntimeException("Error while parsing", e);
        }
	}

    private void createJSONfile(String json) {
        try {
            System.out.println("\n#######################");
            System.out.println("Creating JSON file...");
            
            File file = new File("./generated_file.json");
            file.createNewFile();

            FileWriter myWriter = new FileWriter(file);
            myWriter.write(json);
            myWriter.close();

            System.out.println("JSON file created at: ./generated_file.json");
            System.out.println("#######################\n");
        }
        catch(Exception e){
            System.out.println("Error creating JSON file!");
            System.out.println("#######################\n");
        }
    }
}
