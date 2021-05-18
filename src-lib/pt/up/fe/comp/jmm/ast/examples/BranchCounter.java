package pt.up.fe.comp.jmm.ast.examples;

public class BranchCounter {
    private int ifelse_counter;
    private int while_counter;
    private int temp_counter;
    private String ident;

    public BranchCounter() {
        this.ifelse_counter=0;
        this.while_counter=0;
        this.temp_counter = 0;
        this.ident = "\t\t";
    }

    public void incrementIfElse() {
        this.ifelse_counter++;
    }

    public void incrementWhile(){
        this.while_counter++;
    }

    public void incrementTemp(){
        this.temp_counter++;
    }

    public void incrementIdent() {this.ident += "\t";}

    public void decrementIdent() {if(this.ident.length() >= 3) this.ident = this.ident.substring(0,this.ident.length()-1) ;}

    public int getTemp_counter() { return this.temp_counter; }

    public int getIfelse_counter() { return this.ifelse_counter; }

    public int getWhile_counter() { return this.while_counter; }

    public String getident() { return this.ident; }

    public void setTemp_counter(int x) { this.temp_counter = x; }

    public void setIfelse_counter(int x) { this.ifelse_counter = x; }

    public void setWhile_counter(int x) { this.while_counter = x; }

    public void setIndet(String x) { this.ident = x; }



}
