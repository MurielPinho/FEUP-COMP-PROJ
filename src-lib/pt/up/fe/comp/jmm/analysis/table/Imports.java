package pt.up.fe.comp.jmm.analysis.table;

import java.util.*;

public class Imports implements SymbolTable {
    private SymbolTable parent;

    ArrayList<String> imports;

    public Imports() {
        this.parent = null;
        this.initializeImports();
    }

    public Imports(SymbolTable parent) {
        this.parent = parent;
        this.initializeImports();
    }

    private void initializeImports() {
        this.imports = new ArrayList<String>();
    }

    public void addImport(String importSt) {
        this.imports.add(importSt);
    }

    public String print(String ini) {
        String ret = "";

        for(int i = 0; i < this.imports.size(); i++) ret += ini + this.imports.get(i) + "\n";

        return ret;
    }

    @Override
    public List<String> getImports() {
        return this.imports;
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