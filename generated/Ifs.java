import java.util.ArrayList;

public class Ifs extends SymbolTable {
    private ArrayList<If> ifs;

    public Ifs() {
        super();
        this.initializeSymbolTable();
    }

    public Ifs(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.ifs = new ArrayList<If>();
    }

    public void addIf(If ifSt) {
        this.ifs.add(ifSt);
    }

    public String print(String ini) {
        String ret = "";

        for(If ifSt: this.ifs) {
            ret += "\n" + ini + "IF:\n";
            ret += ifSt.print(ini + "   ");
        }
        
        return ret;
    }
}