package pt.up.fe.comp.jmm.analysis.table;

import java.util.*;

import pt.up.fe.comp.jmm.JmmNode;

public class Method implements SymbolTable {
    private SymbolTable parent;

    private HashMap<String, SymbolTable> symbol_table;
    private Type returnType;

    public Method() {
        this.parent = null;
        this.returnType = null;
        this.initializeSymbolTable();
    }

    public Method(SymbolTable parent) {
        this.parent = parent;
        this.returnType = null;
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("params", new Locals(this)); // it has all the params of the method, it may be empty
        this.symbol_table.put("scope", new Scope(this)); // it has all the classes, it may be empty
    }

    public void processMethod(JmmNode node) {
        List<JmmNode> childrens = node.getChildren();

        for(JmmNode child: childrens) {
            if(child.toString().equals("ReturnType")) this.processReturnType(child);
            else if(child.toString().equals("MethodParams")) this.processParams(child);
            else if(child.toString().equals("MethodBody")) this.processBody(child);
        }
    }

    private void processReturnType(JmmNode node) {
        JmmNode child = node.getChildren(0);
        
        if(child.toString().equals("Type")) this.returnType = new Type(child.get("val"));
    }

    private void processParams(JmmNode node) {
        List<JmmNode> childrens = node.getChildren();

        for(JmmNode child: childrens)
            if(child.toString().equals("MethodParam")) this.addSymbol(child);
    }

    private void addSymbol(JmmNode node) {
        Locals locals = (Locals) this.symbol_table.get("params");
        Symbol symbol = this.processSymbol(node);
        locals.addLocal(symbol);
        this.symbol_table.put("params", locals);
    }

    private Symbol processSymbol(JmmNode node) {
        Symbol symbol = new Symbol();
        symbol.processSymbol(node);
        return symbol;
    }

    private void processBody(JmmNode node) {
        Scope scope = new Scope(this);
        scope.processScope(node);
        this.symbol_table.put("scope", scope);
    }

    public String print(String ini) {
        String ret = "";

        ret += ini + "RETURN:\n";
        ret += this.returnType.print(ini + "   ");

        ret += "\n" + ini + "PARAMS:\n";
        ret += ((Locals) this.symbol_table.get("params")).print(ini + "   ");
        
        ret += "\n" + ini + "SCOPE:\n";
        ret += ((Scope) this.symbol_table.get("scope")).print(ini + "   ");

        return ret;
    }

    @Override
    public List<String> getImports() {
        return null;
    }

    @Override
    public String getClassName() {
        return null;
    }

    @Override
    public String getSuper() {
        return null;
    }

    @Override
    public List<Symbol> getFields() {
        return null;
    }

    @Override
    public List<String> getMethods() {
        return null;
    }

    @Override
    public Type getReturnType(String methodName) {
        return this.returnType;
    }

    @Override
    public List<Symbol> getParameters(String methodName) {
        return ((Locals) this.symbol_table.get("params")).getSymbols();
    }

    @Override
    public List<Symbol> getLocalVariables(String methodName) {
        return this.symbol_table.get("scope").getLocalVariables(methodName);
    }
}