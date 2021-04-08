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
        return (this.parent != null) ? this.parent.getImports() : null;
    }

    @Override
    public String getClassName() {
        return (this.parent != null) ? this.parent.getClassName() : null;
    }

    @Override
    public String getSuper() {
        String ret = "";

        for(String aux: this.extendsSt) ret += aux + " ";

        return ret;
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
