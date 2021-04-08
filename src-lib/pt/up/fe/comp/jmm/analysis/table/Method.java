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
            if(child.getKind().equals("ReturnType")) this.processReturnType(child);
            else if(child.getKind().equals("MethodParams")) this.processParams(child);
            else if(child.getKind().equals("MethodBody")) this.processBody(child);
        }
    }

    private void processReturnType(JmmNode node) {
        JmmNode child = node.getChildren().get(0);
        
        if(child.getKind().equals("Type")) this.returnType = SymbolTableUtils.processType(child.get("val"));
    }

    private void processParams(JmmNode node) {
        List<JmmNode> childrens = node.getChildren();

        for(JmmNode child: childrens)
            if(child.getKind().equals("MethodParam")) this.addSymbol(child);
    }

    private void addSymbol(JmmNode node) {
        Locals locals = (Locals) this.symbol_table.get("params");
        locals.addLocal(SymbolTableUtils.processSymbol(node));
        this.symbol_table.put("params", locals);
    }

    private void processBody(JmmNode node) {
        Scope scope = new Scope(this);
        scope.processScope(node);
        this.symbol_table.put("scope", scope);
    }

    public String print(String ini) {
        String ret = "";

        ret += ini + "RETURN:\n";
        ret += SymbolTableUtils.printType(this.returnType, ini + "   ");

        ret += "\n" + ini + "PARAMS:\n";
        ret += ((Locals) this.symbol_table.get("params")).print(ini + "   ");
        
        ret += "\n" + ini + "SCOPE:\n";
        ret += ((Scope) this.symbol_table.get("scope")).print(ini + "   ");

        return ret;
    }

    @Override
    public List<String> getImports() {
        return (this.parent != null) ? this.parent.getImports() : null;
    }

    @Override
    public String getClassName() {
        return (this.parent != null) ? this.parent.getClassName() : null;
    }

    @Override
    public String getSuper() {
        return (this.parent != null) ? this.parent.getSuper() : null;
    }

    @Override
    public List<Symbol> getFields() {
        return (this.parent != null) ? this.parent.getFields() : null;
    }

    @Override
    public List<String> getMethods() {
        return (this.parent != null) ? this.parent.getMethods() : null;
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