package pt.up.fe.comp.jmm.analysis.table;

import pt.up.fe.comp.jmm.JmmNode;

import java.util.*;

public class Symbol {
    private Type type;
    private String name;

    public Symbol() {
        this.type = null;
        this.name = null;
    }

    public Symbol(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public void processSymbol(JmmNode node) {
        List<JmmNode> childrens = node.getChildren();

        for(JmmNode child: childrens) {
            if(child.toString().equals("Type")) this.type = new Type(child.get("val"));
            else if(child.toString().equals("VarId")) this.name = child.get("val");
        }
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Symbol [type=" + type + ", name=" + name + "]";
    }

    public String print(String ini) {
        String ret = "";

        ret += ini + "NAME: " + this.name + "\n";
        ret += this.type.print(ini+ "   ");
        
        return ret;
    }

}
