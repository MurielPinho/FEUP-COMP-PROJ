package pt.up.fe.comp.jmm.analysis.table;

import java.util.*;

import pt.up.fe.comp.jmm.JmmNode;

public class RootSymbolTable implements SymbolTable {
    private SymbolTable parent;

    private HashMap<String, SymbolTable> symbol_table;

    public RootSymbolTable() {
        this.parent = null;
        this.initializeSymbolTable();
    }

    public RootSymbolTable(SymbolTable parent) {
        this.parent = parent;
        this.initializeSymbolTable();
    }
    
    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("imports", new Imports(this)); // it has all the imports, it may be empty
        this.symbol_table.put("class", new Class(this)); // it has all the classes
    }

    public void buildSymbolTable(JmmNode node) {
        List<JmmNode> childrens = node.getChildren();

        for(JmmNode child: childrens) {
            if(child.toString().equals("Imports")) this.addImports(child);
            else if(child.toString().equals("Class")) this.addClass(child);
        }
    }

    private void addImports(JmmNode node) {
        List<JmmNode> childrens = node.getChildren();

        for(JmmNode child: childrens) {
            if(child.toString().equals("ImportDeclaration")) {
                Imports imports = (Imports) this.symbol_table.get("imports");
                imports.addImport(child.get("val"));
                this.symbol_table.put("imports", imports);
            }
        }
    }

    private void addClass(JmmNode node) {
        Class classElem = new Class(this);
        classElem.processClass(node);
        this.symbol_table.put("class", classElem);
    }

    public String print() {
        String ret = "";
        
        ret += "IMPORTS:\n";
        ret += ((Imports) this.symbol_table.get("imports")).print("   ");

        ret += "\nCLASS:\n";
        ret += ((Class) this.symbol_table.get("class")).print("   ");
        
        return ret;
    }

    @Override
    public List<String> getImports() {
        return this.symbol_table.get("imports").getImports();
    }

    @Override
    public String getClassName() {
        return this.symbol_table.get("class").getClassName();
    }

    @Override
    public String getSuper() {
        return this.symbol_table.get("class").getSuper();
    }

    @Override
    public List<Symbol> getFields() {
        return this.symbol_table.get("class").getFields();
    }

    @Override
    public List<String> getMethods() {
        return this.symbol_table.get("class").getMethods();
    }

    @Override
    public Type getReturnType(String methodName) {
        return this.symbol_table.get("class").getReturnType(methodName);
    }

    @Override
    public List<Symbol> getParameters(String methodName) {
        return this.symbol_table.get("class").getParameters(methodName);
    }

    @Override
    public List<Symbol> getLocalVariables(String methodName) {
        return this.symbol_table.get("class").getLocalVariables(methodName);
    }
}