import java.util.HashMap;

public class Scope extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;

    public Scope() {
        super();
        this.initializeSymbolTable();
    }

    public Scope(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("locals", new Locals()); // it has all the local variables, it may be empty
        // this.symbol_table.put("scopes", new Scopes()); // it has all the local variables, it may be empty
        // this.symbol_table.put("whiles", new Whiles()); 
        // this.symbol_table.put("ifs", new Ifs());
    }

    public void processScope(SimpleNode simpleNode) {
        int numChild = simpleNode.jjtGetNumChildren();
        int ind = 0;

        while(ind != numChild) {
            SimpleNode node = (SimpleNode) simpleNode.jjtGetChild(ind++);
            
            if(node.toString().equals("VarDeclaration")) this.addVar(node);
            // else if(node.toString().equals("Scope")) this.addScope(node);
            // else if(node.toString().equals("IfAndElse")) this.addIf(node);
            // else if(node.toString().equals("While")) this.addWhile(node);
            else this.processScope(node);
        }
    }

    private void addScope(SimpleNode simpleNode) {
        Scopes scopes = (Scopes) this.symbol_table.get("scopes");
        scopes.addScope(this.processScope1(simpleNode));
        this.symbol_table.put("scopes", scopes);
    }

    private Scope processScope1(SimpleNode simpleNode) {
        Scope scope = new Scope();
        scope.processScope(simpleNode);
        return scope;
    }

    private void addIf(SimpleNode simpleNode) {
        Ifs ifs = (Ifs) this.symbol_table.get("ifs");
        ifs.addIf(this.processIf(simpleNode));
        this.symbol_table.put("ifs", ifs);
    }

    private If processIf(SimpleNode simpleNode) {
        If ifElem = new If();
        ifElem.processIf(simpleNode);
        return ifElem;
    }

    private void addWhile(SimpleNode simpleNode) {
        Whiles whiles = (Whiles) this.symbol_table.get("whiles");
        whiles.addWhile(this.processWhile(simpleNode));
        this.symbol_table.put("whiles", whiles);
    }

    private While processWhile(SimpleNode simpleNode) {
        While whileElem = new While();
        whileElem.processWhile(simpleNode);
        return whileElem;
    }

    private void addVar(SimpleNode simpleNode) {
        Locals locals = (Locals) this.symbol_table.get("locals");
        Var var = this.processVar(simpleNode);
        locals.addLocal(var.getName(), var);
        this.symbol_table.put("locals", locals);
    }

    private Var processVar(SimpleNode simpleNode) {
        Var var = new Var();
        var.processVar(simpleNode);
        return var;
    }

    public String print(String ini) {
        String ret = "";

        ret += ini + "LOCALS:\n";
        ret += ((Locals) this.symbol_table.get("locals")).print(ini + "   ");
        
        // ret += "\n" + ini + "SCOPES:\n";
        // ret += ((Scopes) this.symbol_table.get("scopes")).print(ini + "   ");

        // ret += "\n" + ini + "IFS:\n";
        // ret += ((Ifs) this.symbol_table.get("ifs")).print(ini + "   ");

        // ret += "\n" + ini + "WHILES:\n";
        // ret += ((Whiles) this.symbol_table.get("whiles")).print(ini + "   ");

        return ret;
    }
}