import java.util.HashMap;

public class While extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;

    public While() {
        super();
        this.initializeSymbolTable();
    }

    public While(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("condition", new Locals());
        this.symbol_table.put("scope", new Scope()); // while scope; it has all the local variables, it may be empty
    }

}