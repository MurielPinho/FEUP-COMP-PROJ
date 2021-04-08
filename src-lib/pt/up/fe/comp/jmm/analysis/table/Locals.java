package pt.up.fe.comp.jmm.analysis.table;

import java.util.*;

public class Locals implements SymbolTable {
    private SymbolTable parent;
    
    private List<Symbol> symbols;

    public Locals() {
        this.parent = null;
        this.initializeSymbolTable();
    }

    public Locals(SymbolTable parent) {
        this.parent = parent;
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbols = new ArrayList<Symbol>();
    }

    public boolean isEmpty() {
        return this.symbols.isEmpty();
    }

    public void addLocal(Symbol symbol) {
        this.symbols.add(symbol);
    }

    public List<Symbol> getSymbols() {
        return this.symbols;
    }

    public String print(String ini) {
        String ret = "";

        for(Symbol symbol: this.symbols) ret += symbol.print(ini);

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
        return (this.parent != null) ? this.parent.getReturnType(methodName) : null;
    }

    @Override
    public List<Symbol> getParameters(String methodName) {
        return (this.parent != null) ? this.parent.getParameters(methodName) : null;
    }

    @Override
    public List<Symbol> getLocalVariables(String methodName) {
        return (this.parent != null) ? this.parent.getLocalVariables(methodName) : null;
    }
}