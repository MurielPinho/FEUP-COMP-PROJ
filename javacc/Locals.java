import java.util.HashMap;

public class Locals extends SymbolTable {
    private HashMap<String, Var> symbol_table;

    public Locals() {
        super();
        this.initializeSymbolTable();
    }

    public Locals(Scope parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, Var>();
    }

    public boolean isEmpty() {
        return this.symbol_table.isEmpty();
    }

    public void addLocal(String varId, Var var) {
        this.symbol_table.put(varId, var);
    }

    public String print(String ini) {
        String ret = "";

        for(String varName: this.symbol_table.keySet()) {
            ret += "\n" + ini + "NAME: " + varName + "\n";
            ret += ((Var) this.symbol_table.get(varName)).print(ini + "   ");
        }

        return ret;
    }
}