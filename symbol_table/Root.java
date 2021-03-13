import java.util.HashMap;

public class Root extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;

    public Root() {
        super();
        this.initializeSymbolTable();
    }

    public Root(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }
    
    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("imports", new Imports()); // it has all the imports, it may be empty
        this.symbol_table.put("classes", new Classes()); // it has all the classes
        this.symbol_table.put("locals", new Locals()); // it has all the local variables, it may be empty
    }
}