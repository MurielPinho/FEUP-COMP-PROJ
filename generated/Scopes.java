import java.util.ArrayList;

public class Scopes extends SymbolTable {
    private ArrayList<Scope> scopes;

    public Scopes() {
        super();
        this.initializeSymbolTable();
    }

    public Scopes(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.scopes = new ArrayList<Scope>();
    }

    public void addScope(Scope scope) {
        this.scopes.add(scope);
    }

    public String print(String ini) {
        String ret = "";

        for(Scope scope: this.scopes) {
            ret += "\n" + ini + "SCOPE:\n";
            ret += scope.print(ini + "   ");
        }
        
        return ret;
    }
}