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
        this.symbol_table.put("main", new MainMethod()); // methods
        this.symbol_table.put("locals", new Locals()); // fields
    }

    public void processClass(SimpleNode simpleNode) {
        this.processExtends(simpleNode);

        int numChild = simpleNode.jjtGetNumChildren();
        int ind = 0;

        while(ind != numChild) {
            SimpleNode node = (SimpleNode) simpleNode.jjtGetChild(ind++);
            
            if(node.toString().equals("VarDeclaration")) this.addVar(node);
            else if(node.toString().equals("MethodDeclaration")) this.addMethod((SimpleNode) node.jjtGetChild(0));
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
        if(simpleNode.toString().equals("RegularMethod")) {
            Methods methods = (Methods) this.symbol_table.get("methods");
            methods.addMethod(simpleNode.get("val"), this.processMethod(simpleNode));
            this.symbol_table.put("methods", methods);
        }
        else if (simpleNode.toString().equals("Main")) {
            this.symbol_table.put("main", this.processMainMethod(simpleNode));
        }
    }

    private Method processMethod(SimpleNode simpleNode) {
        Method method = new Method();
        method.processMethod(simpleNode);
        return method;
    }

    private MainMethod processMainMethod(SimpleNode simpleNode) {
        MainMethod method = new MainMethod();
        method.processMethod(simpleNode);
        return method;
    }

    public String print(String ini) {
        String ret = "";

        ret += ini + "EXTENDS:\n";
        ret += ((Extends) this.symbol_table.get("extends")).print(ini + "   ");

        ret += "\n" + ini + "FIELDS:\n";
        ret += ((Locals) this.symbol_table.get("locals")).print(ini + "   ");
        
        ret += "\n" + ini + "MAIN_METHOD:";
        ret += ((MainMethod) this.symbol_table.get("main")).print(ini + "   ");

        ret += "\n" + ini + "METHODS:";
        ret += ((Methods) this.symbol_table.get("methods")).print(ini + "   ");

        return ret;
    }
}