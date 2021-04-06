import java.util.HashMap;

public class RootSymbolTable extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;

    public RootSymbolTable() {
        super();
        this.initializeSymbolTable();
    }

    public RootSymbolTable(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }
    
    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("imports", new Imports()); // it has all the imports, it may be empty
        this.symbol_table.put("classes", new Classes()); // it has all the classes
        this.symbol_table.put("locals", new Locals()); // in this case it's empty
    }

    public void buildSymbolTable(SimpleNode simpleNode) {
        int numChild = simpleNode.jjtGetNumChildren();
        int ind = 0;

        while(ind != numChild) {
            SimpleNode node = (SimpleNode) simpleNode.jjtGetChild(ind++);
            
            if(node.toString().equals("Imports")) this.addImports(node);
            else if(node.toString().equals("Class")) this.addClass(node);
        }
    }

    private void addImports(SimpleNode simpleNode) {
        int numChild = simpleNode.jjtGetNumChildren();
        int ind = 0;

        while(ind != numChild) {
            SimpleNode node = (SimpleNode) simpleNode.jjtGetChild(ind++);
            
            if(node.toString().equals("ImportDeclaration")) {
                Imports imports = (Imports) this.symbol_table.get("imports");
                imports.addImport(node.get("val"));
                this.symbol_table.put("imports", imports);
            }
        }
    }

    private void addClass(SimpleNode simpleNode) {
        Classes classes = (Classes) this.symbol_table.get("classes");
        classes.addClass(this.processClassName(simpleNode), this.processClass(simpleNode));
        this.symbol_table.put("classes", classes);
    }

    private Class processClass(SimpleNode simpleNode) {
        Class classElem = new Class();
        classElem.processClass(simpleNode);
        return classElem;
    }

    private String processClassName(SimpleNode simpleNode) {
        System.out.println(simpleNode.get("val").split(" ")[0]);
        return simpleNode.get("val").split(" ")[0];
    }
}