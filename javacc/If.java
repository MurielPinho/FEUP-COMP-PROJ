import java.util.HashMap;

public class If extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;

    public If() {
        super();
        this.initializeSymbolTable();
    }

    public If(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("condition", new Locals(this));
        this.symbol_table.put("if_scope", new Scope(this)); // if scope; it has all the local variables, it may be empty
        this.symbol_table.put("else_scope", new Scope(this)); // else scope; it has all the local variables, it may be empty and not even exist
    }

    public void processIf(SimpleNode simpleNode) {
        int numChild = simpleNode.jjtGetNumChildren();
        int ind = 0;

        while(ind != numChild) {
            SimpleNode node = (SimpleNode) simpleNode.jjtGetChild(ind++);
            
            if(node.toString().equals("IfExpression")) this.processIfExpression(node);
            else if(node.toString().equals("IfBody")) this.processIfBody(node);
            else if(node.toString().equals("ElseBody")) this.processElseBody(node);
        }
    }

    private void processIfExpression(SimpleNode simpleNode) {}

    private void processIfBody(SimpleNode simpleNode) {
        Scope scope = new Scope(this);
        scope.processScope(simpleNode);
        this.symbol_table.put("if_scope", scope);
    }

    private void processElseBody(SimpleNode simpleNode) {
        Scope scope = new Scope(this);
        scope.processScope(simpleNode);
        this.symbol_table.put("else_scope", scope);
    }

    public String print(String ini) {
        String ret = "";

        ret += ini + "CONDITION:\n";
        ret += ((Locals) this.symbol_table.get("condition")).print(ini + "   ");

        ret += "\n" + ini + "IF_BODY:\n";
        ret += ((Scope) this.symbol_table.get("if_scope")).print(ini + "   ");
        
        ret += "\n" + ini + "ELSE_BODY:\n";
        ret += ((Scope) this.symbol_table.get("else_scope")).print(ini + "   ");

        return ret;
    }

}