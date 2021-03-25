public class SymbolTable {
    private SymbolTable parent; // reference to its parent, null if it has not one

    public SymbolTable() {
        this.parent = null;
    }

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
    }
}