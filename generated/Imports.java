import java.util.ArrayList;

public class Imports extends SymbolTable {
    ArrayList<String> imports;

    public Imports() {
        super();
        this.initializeImports();
    }

    public Imports(SymbolTable parent) {
        super(parent);
        this.initializeImports();
    }

    private void initializeImports() {
        this.imports = new ArrayList<String>();
    }

    public void addImport(String importSt) {
        this.imports.add(importSt);
    }
}