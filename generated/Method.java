import java.util.HashMap;

public class Method extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;
    private String returnType = null;

    public Method() {
        super();
        this.initializeSymbolTable();
    }

    public Method(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("params", new Locals()); // it has all the params of the method, it may be empty
        this.symbol_table.put("scope", new Scope()); // it has all the classes, it may be empty
    }

    public void processMethod(SimpleNode simpleNode) {
        int numChild = simpleNode.jjtGetNumChildren();
        int ind = 0;

        while(ind != numChild) {
            SimpleNode node = (SimpleNode) simpleNode.jjtGetChild(ind++);
            
            if(node.toString().equals("ReturnType")) this.processReturnType(node);
            else if(node.toString().equals("MethodParams")) this.processParams(node);
            else if(node.toString().equals("MethodBody")
                   || node.toString().equals("ReturnStatement")) this.processBody(node);
        }
    }

    private void processReturnType(SimpleNode simpleNode) {
        SimpleNode node = (SimpleNode) simpleNode.jjtGetChild(0);
        
        if(node.toString().equals("Type")) this.returnType = node.get("val");
    }

    private void processParams(SimpleNode simpleNode) {
        int numChild = simpleNode.jjtGetNumChildren();
        int ind = 0;

        while(ind != numChild) {
            SimpleNode node = (SimpleNode) simpleNode.jjtGetChild(ind++);
            
            if(node.toString().equals("MethodParam")) this.addVar(node);
            
        }
    }

    private void addVar(SimpleNode simpleNode) {
        Locals locals = (Locals) this.symbol_table.get("params");
        Var var = this.processVar(simpleNode);
        locals.addLocal(var.getName(), var);
        this.symbol_table.put("params", locals);
    }

    private Var processVar(SimpleNode simpleNode) {
        Var var = new Var();
        var.processVar(simpleNode);
        return var;
    }

    private void processBody(SimpleNode simpleNode) {}

    public String print(String ini) {
        String ret = "";

        ret += ini + "RETURN: " + this.returnType + "\n";

        ret += "\n" + ini + "PARAMS:\n";
        ret += ((Locals) this.symbol_table.get("params")).print(ini + "   ");
        
        ret += "\n" + ini + "SCOPE:\n";
        ret += ((Scope) this.symbol_table.get("scope")).print(ini + "   ");

        return ret;
    }
}