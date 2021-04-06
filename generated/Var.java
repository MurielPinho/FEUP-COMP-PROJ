public class Var {
    private String name = null;
    private String type = null; // it can be int, int[], boolean ...
    private String value = null; // it has the value of the variable, if it has been initialized

    public void processVar(SimpleNode simpleNode) {
        int numChild = simpleNode.jjtGetNumChildren();
        int ind = 0;

        while(ind != numChild) {
            SimpleNode node = (SimpleNode) simpleNode.jjtGetChild(ind++);
            
            if(node.toString().equals("Type")) this.type = node.get("val");
            else if(node.toString().equals("VarId")) this.name = node.get("val");
        }
    }

    public String getName() {
        return this.name;
    }

    public String print(String ini) {
        String ret = "";
            
        ret += ini + "TYPE: " + this.type + "\n";
        ret += ini + "VALUE: ";
        
        if (this.value == null) ret += "not initialized\n";
        else ret += this.value + "\n";

        return ret;
    }
}