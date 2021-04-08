package pt.up.fe.comp.jmm.analysis.table;

import java.util.*;

import pt.up.fe.comp.jmm.JmmNode;

public class SymbolTableUtils {
    public static Type processType(String name) {
        int num = name.indexOf("[");
        
        if(num == -1) return new Type(name, false);
        else return new Type(name.substring(0, num), true);
    }

    public static Symbol processSymbol(JmmNode node) {
        List<JmmNode> childrens = node.getChildren();

        Type type = null;
        String name = null;

        for(JmmNode child: childrens) {
            if(child.getKind().equals("Type")) type = SymbolTableUtils.processType((child.get("val")));
            else if(child.getKind().equals("VarId")) name = child.get("val");
        }

        return new Symbol(type, name);
    }

    public static String printSymbol(Symbol symbol, String ini) {
        String ret = "";

        ret += ini + "NAME: " + symbol.getName() + "\n";
        ret += SymbolTableUtils.printType(symbol.getType(), ini + "   ");
        
        return ret;
    }

    public static String printType(Type type, String ini) {
        String ret = "";

        ret += ini + "TYPE: " + type.getName();
        ret += "\n" + ini + "IS_ARRAY: " + String.valueOf(type.isArray()) + "\n";
        
        return ret;
    }

}
