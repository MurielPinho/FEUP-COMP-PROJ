import java.util.HashMap;

public class Class extends SymbolTable {
    private HashMap<String, SymbolTable> symbol_table;

    public Class() {
        super();
        this.initializeSymbolTable();
    }

    public Class(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("extends", new Extends()); // methods
        this.symbol_table.put("methods", new Methods()); // methods
        this.symbol_table.put("locals", new Locals()); // fields
    }

    public void processClass(SimpleNode simpleNode) {
        this.processExtends(simpleNode);

        int numChild = simpleNode.jjtGetNumChildren();
        int ind = 0;

        while(ind != numChild) {
            SimpleNode node = (SimpleNode) simpleNode.jjtGetChild(ind++);
            
            if(node.toString().equals("VarDeclaration")) this.addVar(node);
            else if(node.toString().equals("MethodDeclaration")) this.addMethod(node);
        }
    }

    private void processExtends(SimpleNode simpleNode) {
        String names[] = simpleNode.get("val").split(" ");

        if (names.length > 0) {
            for(int i = 1; i < names.length; i++) {
                System.out.println("Extends" + i + ": " + names[i]);
                Extends aux = (Extends) this.symbol_table.get("extends");
                aux.addExtend(names[i]);
                this.symbol_table.put("extends", aux);
            }
        }
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

    private void addMethod(SimpleNode simpleNode) {
    	Methods methods = (Methods) this.symbol_table.get("methods");
        methods.addMethod(simpleNode.get("val"), this.processMethod(simpleNode));
        this.symbol_table.put("methods", methods);
    }

    private Method processMethod(SimpleNode simpleNode) {
        Method method = new Method();
        method.processMethod(simpleNode);
        return method;
    }
}