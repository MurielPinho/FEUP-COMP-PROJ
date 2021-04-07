package pt.up.fe.comp.jmm.analysis.table;

import java.util.*;

import pt.up.fe.comp.jmm.JmmNode;

public class Scope implements SymbolTable {
    private SymbolTable parent;

    private HashMap<String, SymbolTable> symbol_table;

    public Scope() {
        this.parent = null;
        this.initializeSymbolTable();
    }

    public Scope(SymbolTable parent) {
        this.parent = parent;
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.symbol_table = new HashMap<String, SymbolTable>();
        this.symbol_table.put("locals", new Locals()); // it has all the local variables, it may be empty
    }

    public void processScope(JmmNode node) {
        List<JmmNode> childrens = node.getChildren();

        for(JmmNode child: childrens) {
            if(child.toString().equals("VarDeclaration")) this.addSymbol(child);
            else this.processScope(child);
        }
    }

    private void addSymbol(JmmNode node) {
        Locals locals = (Locals) this.symbol_table.get("locals");
        Symbol symbol = this.processSymbol(node);
        locals.addLocal(symbol);
        this.symbol_table.put("locals", locals);
    }

    private Symbol processSymbol(JmmNode node) {
        Symbol symbol = new Symbol();
        symbol.processSymbol(node);
        return symbol;
    }

    public String print(String ini) {
        String ret = "";

        ret += ini + "LOCALS:\n";
        ret += ((Locals) this.symbol_table.get("locals")).print(ini + "   ");

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
        return null;
    }

    @Override
    public List<Symbol> getParameters(String methodName) {
        return null;
    }

    @Override
    public List<Symbol> getLocalVariables(String methodName) {
        return ((Locals) this.symbol_table.get("locals")).getSymbols();
    }
}