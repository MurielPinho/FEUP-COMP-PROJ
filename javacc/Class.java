import java.util.HashMap;

public class Class extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;

    public Class() {
        super();
        this.initializeSymbolTable();
    }

    public Class(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("methods", new Methods()); // it has all the methods, it may be empty
        this.symbol_table.put("locals", new Locals()); // it has all the local variables(attributes), it may be empty
    }

    public void processClass(SimpleNode simpleNode) {
        int numChild = simpleNode.jjtGetNumChildren();
        int ind = 0;

        while(ind != numChild) {
            SimpleNode node = (SimpleNode) simpleNode.jjtGetChild(ind++);
            
            if(node.toString().equals("VarDeclaration")) this.addVar(node);
            else if(node.toString().equals("MethodDeclaration")) this.addMethod(node);
        }
    }

    private void addVar(SimpleNode simpleNode) {
        int numChild = simpleNode.jjtGetNumChildren();
        int ind = 0;

        Locals locals = this.symbol_table.get("locals");
        Var var = new Var();
        String varId;

        while(ind != numChild) {
            SimpleNode node = (SimpleNode) simpleNode.jjtGetChild(ind++);
            
            if(node.toString().equals("Type")) {
                var.setType(node.get("val"));
            }
            else if(node.toString().equals("VarId")) {
                varId = node.get("val");
            }
        }

        locals.addLocal(varId, var);
    }

    private void addMethod(SimpleNode simpleNode) {
    	Methods methods = (Methods) this.symbol_table.get("methods");
        methods.addClass(simpleNode.get("val"), this.processClass(simpleNode));
        this.symbol_table.put("methods", methods);
    }
}