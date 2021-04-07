package pt.up.fe.comp.jmm.analysis.table;

import java.util.*;

public class Extends implements SymbolTable {
    private SymbolTable parent;

    ArrayList<String> extendsSt;

    public Extends() {
        this.parent = null;
        this.initializeExtends();
    }

    public Extends(SymbolTable parent) {
        this.parent = parent;
        this.initializeExtends();
    }

    private void initializeExtends() {
        this.extendsSt = new ArrayList<String>();
    }

    public void addExtend(String extendsSt) {
        this.extendsSt.add(extendsSt);
    }

    public String print(String ini) {
        String ret = "";

        for(int i = 0; i < this.extendsSt.size(); i++) ret += ini + this.extendsSt.get(i) + "\n";

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
        String ret = "";

        for(String aux: this.extendsSt) ret += aux + " ";

        return ret;
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
        return null;
    }

    @Override
    public List<Symbol> getParameters(String methodName) {
        return null;
    }

    @Override
    public List<Symbol> getLocalVariables(String methodName) {
        return null;
    }
}
