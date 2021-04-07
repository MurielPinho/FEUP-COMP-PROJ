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