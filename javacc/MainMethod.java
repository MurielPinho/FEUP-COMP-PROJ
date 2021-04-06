import java.util.HashMap;

public class MainMethod extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;

    public MainMethod() {
        super();
        this.initializeSymbolTable();
    }

    public MainMethod(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("scope", new Scope()); // it has all the classes, it may be empty
    }

    public void processMethod(SimpleNode simpleNode) {}

    public String print(String ini) {
        String ret = "";

        ret += "\n" + ini + "SCOPE:\n";
        ret += ((Scope) this.symbol_table.get("scope")).print(ini + "   ");

        return ret;
    }
}