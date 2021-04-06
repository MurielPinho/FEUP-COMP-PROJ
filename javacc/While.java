import java.util.HashMap;

public class While extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;

    public While() {
        super();
        this.initializeSymbolTable();
    }

    public While(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("condition", new Locals(this));
        this.symbol_table.put("scope", new Scope(this)); // while scope; it has all the local variables, it may be empty
    }

    public void processWhile(SimpleNode simpleNode) {
        int numChild = simpleNode.jjtGetNumChildren();
        int ind = 0;

        while(ind != numChild) {
            SimpleNode node = (SimpleNode) simpleNode.jjtGetChild(ind++);
            
            if(node.toString().equals("WhileExpression")) this.processWhileExpression(node);
            else if(node.toString().equals("WhileBody")) this.processWhileBody(node);

        }
    }

    private void processWhileExpression(SimpleNode simpleNode) {}

    private void processWhileBody(SimpleNode simpleNode) {
        Scope scope = new Scope(this);
        scope.processScope(simpleNode);
        this.symbol_table.put("scope", scope);
    }

    public String print(String ini) {
        String ret = "";

        ret += ini + "CONDITION:\n";
        ret += ((Locals) this.symbol_table.get("condition")).print(ini + "   ");

        ret += "\n" + ini + "WHILE_BODY:\n";
        ret += ((Scope) this.symbol_table.get("scope")).print(ini + "   ");
        
        return ret;
    }
}