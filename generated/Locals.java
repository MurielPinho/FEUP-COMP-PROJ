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
}