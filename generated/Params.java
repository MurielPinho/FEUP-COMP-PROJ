import java.util.HashMap;

public class Params extends SymbolTable {
    private HashMap<String, Var> params;

    public Params() {
        super();
        this.initializeParams();
    }

    public Params(Scope parent) {
        super(parent);
        this.initializeParams();
    }

    private void initializeParams() {
        this.params = new HashMap<String, Var>();
    }
}