import java.util.ArrayList;

public class Extends extends SymbolTable {
    ArrayList<String> extendsSt;

    public Extends() {
        super();
        this.initializeExtends();
    }

    public Extends(SymbolTable parent) {
        super(parent);
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
}