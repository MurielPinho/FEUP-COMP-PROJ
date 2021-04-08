package pt.up.fe.comp.jmm.analysis.table;

import java.util.*;

import pt.up.fe.comp.jmm.JmmNode;

public class Class implements SymbolTable {
    private SymbolTable parent;

    private HashMap<String, SymbolTable> symbol_table;
    private String className;

    public Class() {
        this.parent = null;
        this.className = null;
        this.initializeSymbolTable();
    }

    public Class(SymbolTable parent) {
        this.parent = parent;
        this.className = null;
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("extends", new Extends(this)); // methods
        this.symbol_table.put("methods", new Methods(this)); // methods
        this.symbol_table.put("main", new MainMethod(this)); // methods
        this.symbol_table.put("locals", new Locals(this)); // fields
    }

    public void processClass(JmmNode node) {
        this.processDef(node);

        List<JmmNode> childrens = node.getChildren();

        for(JmmNode child: childrens) {
            if(child.toString().equals("VarDeclaration")) this.addSymbol(child);
            else if(child.toString().equals("MethodDeclaration")) this.addMethod(child.getChildren().get(0));
        }
    }

    private void processDef(JmmNode node) {
        String names[] = node.get("val").split(" ");

        this.className = names[0];

        if (names.length > 1) {
            for(int i = 1; i < names.length; i++) {
                Extends aux = (Extends) this.symbol_table.get("extends");
                aux.addExtend(names[i]);
                this.symbol_table.put("extends", aux);
            }
        }
    }

    private void addSymbol(JmmNode node) {
        Locals locals = (Locals) this.symbol_table.get("locals");
        Symbol symbol = this.processSymbol(node);
        locals.addLocal(symbol);
        this.symbol_table.put("locals", locals);
    }

    private Symbol processSymbol(JmmNode node) {
        Symbol symbol = new Symbol();
        symbol.processSymbol(node);
        return symbol;
    }

    private void addMethod(JmmNode node) {
        if(node.toString().equals("RegularMethod")) {
            Methods methods = (Methods) this.symbol_table.get("methods");
            methods.addMethod(node.get("val"), this.processMethod(node));
            this.symbol_table.put("methods", methods);
        }
        else if (node.toString().equals("Main")) {
            this.symbol_table.put("main", this.processMainMethod(node));
        }
    }

    private Method processMethod(JmmNode node) {
        Method method = new Method(this);
        method.processMethod(node);
        return method;
    }

    private MainMethod processMainMethod(JmmNode node) {
        MainMethod method = new MainMethod(this);
        method.processMethod(node);
        return method;
    }

    public String print(String ini) {
        String ret = "";

        ret += ini + "NAME: " + this.className +"\n";

        ret += "\n" + ini + "EXTENDS:\n";
        ret += ((Extends) this.symbol_table.get("extends")).print(ini + "   ");

        ret += "\n" + ini + "FIELDS:\n";
        ret += ((Locals) this.symbol_table.get("locals")).print(ini + "   ");
        
        ret += "\n" + ini + "MAIN_METHOD:\n";
        ret += ((MainMethod) this.symbol_table.get("main")).print(ini + "   ");

        ret += "\n" + ini + "METHODS:";
        ret += ((Methods) this.symbol_table.get("methods")).print(ini + "   ");

        return ret;
    }

    @Override
    public List<String> getImports() {
        return (this.parent != null) ? this.parent.getImports() : null;
    }

    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public String getSuper() {
        return this.symbol_table.get("extends").getSuper();
    }

    @Override
    public List<Symbol> getFields() {
        return ((Locals) this.symbol_table.get("locals")).getSymbols();
    }

    @Override
    public List<String> getMethods() {
        List<String> methods = this.symbol_table.get("methods").getMethods();

        if(((MainMethod) this.symbol_table.get("main")).getExists()) methods.add("main");

        return this.symbol_table.get("class").getMethods();
    }

    @Override
    public Type getReturnType(String methodName) {
        if(methodName.equals("main")) return this.symbol_table.get("main").getReturnType(methodName);
        else return this.symbol_table.get("methods").getReturnType(methodName);
    }

    @Override
    public List<Symbol> getParameters(String methodName) {
        if(methodName.equals("main")) return this.symbol_table.get("main").getParameters(methodName);
        else return this.symbol_table.get("methods").getParameters(methodName);
    }

    @Override
    public List<Symbol> getLocalVariables(String methodName) {
        if(methodName.equals("main")) return this.symbol_table.get("main").getLocalVariables(methodName);
        else return this.symbol_table.get("methods").getLocalVariables(methodName);
    }
}