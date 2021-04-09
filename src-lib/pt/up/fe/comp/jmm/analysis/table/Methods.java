package pt.up.fe.comp.jmm.analysis.table;

import java.util.*;

public class Methods implements SymbolTable {
    private SymbolTable parent;

    private HashMap<String, SymbolTable> symbol_table;

    public Methods() {
        this.parent = null;
        this.initializeSymbolTable();
    }

    public Methods(SymbolTable parent) {
        this.parent = parent;
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
    }

    public void addMethod(String name, SymbolTable symbolTable) {
        this.symbol_table.put(name, symbolTable);
    }

    public String print(String ini) {
        String ret = "";

        for(String methodName: this.symbol_table.keySet()) {
            ret += "\n" + ini + "METHOD: " + methodName + "\n";
            ret += ((Method) this.symbol_table.get(methodName)).print(ini + "   ");
        }
        
        return ret;
    }

    public List<String> getMethodNames() {
        List<String> ret = new ArrayList<>();

        for(String method: this.symbol_table.keySet()) ret.add(method);

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
        return (this.symbol_table.containsKey(methodName)) ? this.symbol_table.get(methodName).getReturnType(methodName) : null;
    }

    @Override
    public List<Symbol> getParameters(String methodName) {
        return (this.symbol_table.containsKey(methodName)) ? this.symbol_table.get(methodName).getParameters(methodName) : null;
    }

    @Override
    public List<Symbol> getLocalVariables(String methodName) {
        return (this.symbol_table.containsKey(methodName)) ? this.symbol_table.get(methodName).getLocalVariables(methodName) : null;
    }
}