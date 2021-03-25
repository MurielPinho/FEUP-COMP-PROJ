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
}