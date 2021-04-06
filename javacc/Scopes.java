import java.util.HashMap;

public class Scopes extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;

    public Scopes() {
        super();
        this.initializeSymbolTable();
    }

    public Scopes(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("locals", new Locals()); // it has all the local variables, it may be empty
    }

    public void addScope(String name, SymbolTable symbolTable) {
        this.symbol_table.put(name, symbolTable);
    }
}