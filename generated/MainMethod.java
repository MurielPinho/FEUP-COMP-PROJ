import java.util.HashMap;

public class MainMethod extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;
    private String paramName = null;

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
        this.symbol_table.put("scope", new Scope(this)); // it has all the classes, it may be empty
    }

    public void processMethod(SimpleNode simpleNode) {
        int numChild = simpleNode.jjtGetNumChildren();
        int ind = 0;

        while(ind != numChild) {
            SimpleNode node = (SimpleNode) simpleNode.jjtGetChild(ind++);
            
            if(node.toString().equals("ArgName")) this.paramName = node.get("val");
            else if(node.toString().equals("MethodBody")) this.processBody(node);
        }
    }

    private void processBody(SimpleNode simpleNode) {
        Scope scope = new Scope(this);
        scope.processScope(simpleNode);
        this.symbol_table.put("scope", scope);
    }

    public String print(String ini) {
        String ret = "";

        ret += ini + "ARG_NAME: " + this.paramName + "\n";

        ret += "\n" + ini + "SCOPE:\n";
        ret += ((Scope) this.symbol_table.get("scope")).print(ini + "   ");

        return ret;
    }
}