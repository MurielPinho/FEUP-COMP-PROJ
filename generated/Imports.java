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

    public String print(String ini) {
        String ret = "";

        for(int i = 0; i < this.imports.size(); i++) ret += ini + this.imports.get(i) + "\n";

        return ret;
    }
}