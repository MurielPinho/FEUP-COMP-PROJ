import java.util.HashMap;

public class Classes extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;

    public Classes() {
        super();
        this.initializeSymbolTable();
    }

    public Classes(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        // this.symbol_table.put("locals", new Class());
    }

    public void addClass(String name, SymbolTable symbolTable) {
        this.symbol_table.put(name, symbolTable);
    }
}