import java.util.HashMap;

public class Method extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;

    public Method() {
        super();
        this.initializeSymbolTable();
    }

    public Method(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("params", new Params()); // it has all the params of the method, it may be empty
        this.symbol_table.put("scope", new Scope()); // it has all the classes, it may be empty
    }

    public void processMethod(SimpleNode simpleNode) {}
}