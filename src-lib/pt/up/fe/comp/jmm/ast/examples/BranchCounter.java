package pt.up.fe.comp.jmm.ast.examples;

public class BranchCounter {
    private int ifelse_counter;
    private int while_counter;

    public BranchCounter() {
        this.ifelse_counter=0;
        this.while_counter=0;
    }

    public void incrementIfElse() {
        this.ifelse_counter++;
    }

    public void incrementWhile(){
        this.while_counter++;
    }

}
