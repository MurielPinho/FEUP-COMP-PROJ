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
    }

    public void addClass(String name, SymbolTable symbolTable) {
        this.symbol_table.put(name, symbolTable);
    }

    public String print(String ini) {
        String ret = "";

        for(String className: this.symbol_table.keySet()) {
            ret += "\n" + ini + "CLASS: " + className + "\n";
            ret += ((Class) this.symbol_table.get(className)).print(ini + "   ");
        }
        
        return ret;
    }
}