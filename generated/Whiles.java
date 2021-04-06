import java.util.ArrayList;

public class Whiles extends SymbolTable {
    private ArrayList<While> whiles;

    public Whiles() {
        super();
        this.initializeSymbolTable();
    }

    public Whiles(SymbolTable parent) {
        super(parent);
        this.initializeSymbolTable();
    }

    private void initializeSymbolTable() {
        this.whiles = new ArrayList<While>();
    }

    public void addWhile(While whileSt) {
        this.whiles.add(whileSt);
    }

    public String print(String ini) {
        String ret = "";

        for(While whileSt: this.whiles) {
            ret += "\n" + ini + "WHILE:\n";
            ret += whileSt.print(ini + "   ");
        }
        
        return ret;
    }
}