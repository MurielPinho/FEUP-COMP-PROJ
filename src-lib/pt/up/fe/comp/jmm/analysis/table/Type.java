package pt.up.fe.comp.jmm.analysis.table;

public class Type {
    private String name;
    private boolean isArray;

    public Type(String name) {
        this.processName(name);
    }

    public Type(String name, boolean isArray) {
        this.name = name;
        this.isArray = isArray;
    }

    private void processName(String name) {
        int num = name.indexOf("[");
        
        if(num == -1) {
            this.name = name;
            this.isArray = false;
        }
        else {
            this.name = name.substring(0, num);
            this.isArray = true;
        }
    }

    public String getName() {
        return name;
    }

    public boolean isArray() {
        return isArray;
    }

    @Override
    public String toString() {
        return "Type [name=" + name + ", isArray=" + isArray + "]";
    }

    public String print(String ini) {
        String ret = "";

        ret += ini + "TYPE: " + this.name;
        ret += "\n" + ini + "IS_ARRAY: " + String.valueOf(this.isArray) + "\n";

        return ret;
    }

}
