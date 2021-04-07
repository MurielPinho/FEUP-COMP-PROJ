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
        return this.symbol_table.get(methodName).getReturnType(methodName);
    }

    @Override
    public List<Symbol> getParameters(String methodName) {
        return this.symbol_table.get(methodName).getParameters(methodName);
    }

    @Override
    public List<Symbol> getLocalVariables(String methodName) {
        return this.symbol_table.get(methodName).getLocalVariables(methodName);
    }
}