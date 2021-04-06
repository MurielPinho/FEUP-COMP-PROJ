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
        // this.symbol_table.put("params", new Method());
    }

    public void addMethod(String name, SymbolTable symbolTable) {
        this.symbol_table.put(name, symbolTable);
    }

}