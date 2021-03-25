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
        this.symbol_table.put("locals", new Locals()); // it has all the local variables, it may be empty
    }
}