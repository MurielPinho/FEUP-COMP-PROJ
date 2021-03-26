import java.util.HashMap;

public class Methods extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;

    public Methods() {
        super();
        this.initializeSymbolTable();
    }

    public Methods(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("params", new Var());
        this.symbol_table.put("scope", new Scope()); // it has all the local variables, it may be empty
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

}