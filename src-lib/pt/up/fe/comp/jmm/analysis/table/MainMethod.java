package pt.up.fe.comp.jmm.analysis.table;

import java.util.*;

import pt.up.fe.comp.jmm.JmmNode;

public class MainMethod implements SymbolTable {
    private SymbolTable parent;

    private HashMap<String, SymbolTable> symbol_table;
    private String paramName;

    private boolean exists;

    public MainMethod() {
        this.parent = null;
        this.exists = false;
        this.paramName = null;
        this.initializeSymbolTable();
    }

    public MainMethod(SymbolTable parent) {
        this.parent = parent;
        this.exists = false;
        this.paramName = null;
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("scope", new Scope(this)); // it has all the classes, it may be empty
    }

    public void processMethod(JmmNode node) {
        this.exists = true;

        List<JmmNode> childrens = node.getChildren();

        for(JmmNode child: childrens) {
            if(child.toString().equals("ArgName")) this.paramName = child.get("val");
            else if(child.toString().equals("MethodBody")) this.processBody(child);
        }
    }

    private void processBody(JmmNode node) {
        Scope scope = new Scope(this);
        scope.processScope(node);
        this.symbol_table.put("scope", scope);
    }

    public boolean getExists() {
        return this.exists;
    }

    public String print(String ini) {
        String ret = "";

        ret += ini + "ARG_NAME: " + this.paramName + "\n";

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
        return new Type("void", false);
    }

    @Override
    public List<Symbol> getParameters(String methodName) {
        if(this.paramName == null) return null;
        else {
            List<Symbol> ret = new ArrayList<>();

            ret.add(new Symbol(new Type("String", true), this.paramName));
            
            return ret;
        }
    }

    @Override
    public List<Symbol> getLocalVariables(String methodName) {
        return this.symbol_table.get("scope").getLocalVariables(methodName);
    }
}