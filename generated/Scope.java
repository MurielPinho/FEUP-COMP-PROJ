import java.util.HashMap;

public class Scope extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;

    public Scope() {
        super();
        this.initializeSymbolTable();
    }

    public Scope(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("locals", new Locals()); // it has all the local variables, it may be empty
        this.symbol_table.put("scopes", new Scopes()); // it has all the local variables, it may be empty
    }

    public void processScope(SimpleNode simpleNode) {}
}