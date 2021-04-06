import java.util.HashMap;

public class If extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;

    public If() {
        super();
        this.initializeSymbolTable();
    }

    public If(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("condition", new Locals());
        this.symbol_table.put("if_scope", new Scope()); // if scope; it has all the local variables, it may be empty
        this.symbol_table.put("else_scope", new Scope()); // else scope; it has all the local variables, it may be empty and not even exist
    }

}