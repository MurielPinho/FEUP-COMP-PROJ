package pt.up.fe.comp.jmm.ast.examples;

import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.Type;

import java.util.*;

public class AnalysisSemanticInfo {
    List<Report> reports;

    SymbolTable symbolTable;

    public AnalysisSemanticInfo(SymbolTable symbolTable) {
        this.reports = new ArrayList<>();
        this.symbolTable = symbolTable;
    }

    public void addReport(Report report) {
        this.reports.add(report);
    }

    public List<Report> getReports() {
        return this.reports;
    }

    public String getClassName() {
        return this.symbolTable.getClassName();
    }

    public String getSuper() {
        return this.symbolTable.getSuper();
    }

    public List<String> getMethods() {
        return this.symbolTable.getMethods();
    }

    public List<Symbol> getMethodParameters(String methodName) {
        return this.symbolTable.getParameters(methodName);
    }

    public List<String> getImports() {
        return this.symbolTable.getImports();
    }

    public List<Symbol> getLocalVariables(String methodName) {
        return this.symbolTable.getLocalVariables(methodName);
    }

    public List<Symbol> getFields() {
        return this.symbolTable.getFields();
    }

    public Type getReturnType(String methodName) {
        return this.symbolTable.getReturnType(methodName);
    }

    public boolean methodExists(String name) {
        List<String> methods = this.symbolTable.getMethods();

        for(String method: methods) if (method.equals(name)) return true;

        return false;
    }
}
