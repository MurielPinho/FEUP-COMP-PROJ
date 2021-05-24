import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;
import pt.up.fe.specs.util.SpecsIo;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Checkpoint3Tests {

    /**
     * Verifies if there is some report containing the Stage specified.
     * @param reports
     * @param stage
     * @return true if reports contains some report with that Stage, false otherwise
     */
    public static boolean hasReport(List<Report> reports, Stage stage) {
        for (Report report: reports) {
            if (report.getType().equals(ReportType.ERROR) && report.getStage().equals(stage))
                return true;
        }

        return false;
    }

    /**
     * Counts how many reports there is of a specific Stage.
     * @param reports
     * @param stage
     * @return number of reports of a specific Stage.
     */
    public static int countReport(List<Report> reports, Stage stage) {
        int cont = 0;

        for (Report report: reports) {
            if (report.getType().equals(ReportType.ERROR) && report.getStage().equals(stage))
                cont++;
        }

        return cont;
    }

    /**
     * -------- SYNTATIC ---------
     */

    /**
     * This test should have found a syntatic error.
     * This error is because it is missing a right parentheses in while expression.
     */
    @Test
    public void testSyntatic1() {
        System.out.println("-------------------");
        System.out.println("Test 1 - Syntatic: Missing right parentheses in while expression (1 error)");

        System.out.println("\n--- START OF PROG OUTPUT ---\n");
        var result = TestUtils.parse(SpecsIo.getResource("fixtures/checkpoint3/syntatic1.jmm"));
        System.out.println("---- END OF PROG OUTPUT ----\n");

        // Verifies if occured some syntatic report
        boolean reportSyntatic = hasReport(result.getReports(), Stage.SYNTATIC);
        assertEquals(reportSyntatic, true);

        // Verifies how many syntatic reports there is
        int contReportSyntatic = countReport(result.getReports(), Stage.SYNTATIC);
        assertEquals(contReportSyntatic, 1);
    }

    /**
     * This test should have found several syntatic errors.
     */
    @Test
    public void testSyntatic2() {
        System.out.println("-------------------");
        System.out.println("Test 2 - Syntatic: Contains multiple erros in while expressions (4 errors)");

        System.out.println("\n--- START OF PROG OUTPUT ---\n");
        var result = TestUtils.parse(SpecsIo.getResource("fixtures/checkpoint3/syntatic2.jmm"));
        System.out.println("---- END OF PROG OUTPUT ----\n");

        // Verifies if occured some syntatic report
        boolean reportSyntatic = hasReport(result.getReports(), Stage.SYNTATIC);
        assertEquals(reportSyntatic, true);

        // Verifies how many syntatic reports there is
        int contReportSyntatic = countReport(result.getReports(), Stage.SYNTATIC);
        assertEquals(contReportSyntatic, 4);
    }

    /**
     * This test contains several syntatic errors but it
     * should have found only one.
     */
    @Test
    public void testSyntatic3() {
        System.out.println("-------------------");
        System.out.println("Test 3 - Syntatic: Contains multiple erros in while expression but should find only one because the rest is ignored (1 errors)");

        System.out.println("\n--- START OF PROG OUTPUT ---\n");
        var result = TestUtils.parse(SpecsIo.getResource("fixtures/checkpoint3/syntatic3.jmm"));
        System.out.println("---- END OF PROG OUTPUT ----\n");

        // Verifies if occured some syntatic report
        boolean reportSyntatic = hasReport(result.getReports(), Stage.SYNTATIC);
        assertEquals(reportSyntatic, true);

        // Verifies how many syntatic reports there is
        int contReportSyntatic = countReport(result.getReports(), Stage.SYNTATIC);
        assertEquals(contReportSyntatic, 1);
    }

    /**
     * -------- SEMANTIC ---------
     */

    /**
     * This test tests the invoker of methods.
     */
    @Test
    public void testSemantic1() {
        System.out.println("-------------------");
        System.out.println("Test 1 - Semantic: Testes the invoker of methods");

        System.out.println("\n--- START OF PROG OUTPUT ---\n");
        // Bad invocation of methods
        var result = TestUtils.analyse(SpecsIo.getResource("fixtures/checkpoint3/semantic11.jmm"));
        System.out.println("---- END OF PROG OUTPUT ----\n");

        // Verifies if occured some syntatic report
        boolean reportSemantic = hasReport(result.getReports(), Stage.SEMANTIC);
        assertEquals(reportSemantic, true);

        // Verifies how many syntatic reports there is
        int contReportSemantic = countReport(result.getReports(), Stage.SEMANTIC);
        assertEquals(contReportSemantic, 7);

        System.out.println("\n--- START OF PROG OUTPUT ---\n");
        // Good invocation of methods
        result = TestUtils.analyse(SpecsIo.getResource("fixtures/checkpoint3/semantic12.jmm"));
        System.out.println("---- END OF PROG OUTPUT ----\n");

        // Verifies if occured some syntatic report
        reportSemantic = hasReport(result.getReports(), Stage.SEMANTIC);
        assertEquals(reportSemantic, false);

        // Verifies how many syntatic reports there is
        contReportSemantic = countReport(result.getReports(), Stage.SEMANTIC);
        assertEquals(contReportSemantic, 0);
    }

    /**
     * This test tests the parameters of a method.
     */
    @Test
    public void testSemantic2() {
        System.out.println("-------------------");
        System.out.println("Test 2 - Semantic: Tests the parameters of methods");

        System.out.println("\n--- START OF PROG OUTPUT ---\n");
        // Bad invocation of methods
        var result = TestUtils.analyse(SpecsIo.getResource("fixtures/checkpoint3/semantic21.jmm"));
        System.out.println("---- END OF PROG OUTPUT ----\n");

        // Verifies if occured some syntatic report
        boolean reportSemantic = hasReport(result.getReports(), Stage.SEMANTIC);
        assertEquals(reportSemantic, true);

        // Verifies how many syntatic reports there is
        int contReportSemantic = countReport(result.getReports(), Stage.SEMANTIC);
        assertEquals(contReportSemantic, 4);

        System.out.println("\n--- START OF PROG OUTPUT ---\n");
        // Good invocation of methods
        result = TestUtils.analyse(SpecsIo.getResource("fixtures/checkpoint3/semantic22.jmm"));
        System.out.println("---- END OF PROG OUTPUT ----\n");

        // Verifies if occured some syntatic report
        reportSemantic = hasReport(result.getReports(), Stage.SEMANTIC);
        assertEquals(reportSemantic, false);

        // Verifies how many syntatic reports there is
        contReportSemantic = countReport(result.getReports(), Stage.SEMANTIC);
        assertEquals(contReportSemantic, 0);
    }

    /**
     * This test tests the expressions types.
     */
    @Test
    public void testSemantic3() {
        System.out.println("-------------------");
        System.out.println("Test 3 - Semantic: Tests the expressions types");

        System.out.println("\n--- START OF PROG OUTPUT ---\n");
        // Bad invocation of methods
        var result = TestUtils.analyse(SpecsIo.getResource("fixtures/checkpoint3/semantic31.jmm"));
        System.out.println("---- END OF PROG OUTPUT ----\n");

        // Verifies if occured some syntatic report
        boolean reportSemantic = hasReport(result.getReports(), Stage.SEMANTIC);
        assertEquals(reportSemantic, true);

        // Verifies how many syntatic reports there is
        int contReportSemantic = countReport(result.getReports(), Stage.SEMANTIC);
        assertEquals(contReportSemantic, 21);

        System.out.println("\n--- START OF PROG OUTPUT ---\n");
        // Good invocation of methods
        result = TestUtils.analyse(SpecsIo.getResource("fixtures/checkpoint3/semantic32.jmm"));
        System.out.println("---- END OF PROG OUTPUT ----\n");

        // Verifies if occured some syntatic report
        reportSemantic = hasReport(result.getReports(), Stage.SEMANTIC);
        assertEquals(reportSemantic, false);

        // Verifies how many syntatic reports there is
        contReportSemantic = countReport(result.getReports(), Stage.SEMANTIC);
        assertEquals(contReportSemantic, 0);
    }

    /**
     * ---------- OLLIR ----------
     */

    @Test
    public void testOllir1() {
        System.out.println("-------------------");
        System.out.println("Test 1 - Ollir");
    }

    @Test
    public void testOllir2() {
        System.out.println("-------------------");
        System.out.println("Test 2 - Ollir");
    }

    /**
     * ---------- JASMIN ---------
     */

    @Test
    public void testJasmin1() {
        System.out.println("-------------------");
        System.out.println("Test 1 - Jasmin: Tests the precedence of the equations.");
        System.out.println("The test passes if the output of the equation is correct.");
        System.out.println("-------------------");
        var result = TestUtils.backend(SpecsIo.getResource("fixtures/checkpoint3/jasmin1.jmm"));
        System.out.println("\n--- START OF JASMIN CODE OUTPUT ---\n");
        System.out.println("\n"+result.getJasminCode());
        System.out.println("\n--- END OF JASMIN CODE OUTPUT ---\n");
        TestUtils.noErrors(result.getReports());
        System.out.println("The equation is 2+(8*16)/4+32-2");
        System.out.println("Expected Output: 64");
        System.out.println("Result:");
        var output = result.run();
        assertEquals("64", output.trim());

    }

    @Test
    public void testJasmin2() {
        System.out.println("-------------------");
        System.out.println("Test 2 - Jasmin: Tests IF Else and While Generation");
        System.out.println("The test passes if the output of the loop is correct.");
        System.out.println("-------------------");
        var result = TestUtils.backend(new OllirResult(SpecsIo.getResource("fixtures/checkpoint3/jasmin2.ollir")));
        System.out.println("\n--- START OF JASMIN CODE OUTPUT ---\n");
        System.out.println("\n"+result.getJasminCode());
        System.out.println("\n--- END OF JASMIN CODE OUTPUT ---\n");
        TestUtils.noErrors(result.getReports());
        System.out.println("The While loop adds 8 to the current value 16 times, with the initial value 0.");
        System.out.println("Expected Output: 128");
        System.out.println("Result:");
        var output = result.run();
        assertEquals("128", output.trim());
    }

    /**
     * -------- FULL TEST --------
     */

    @Test
    public void testFull1() {
        System.out.println("-------------------");
        System.out.println("Test 1 - Full");
        System.out.println("-------------------");
        var result = TestUtils.backend(SpecsIo.getResource("fixtures/checkpoint3/Pot1.jmm"));
        System.out.println("\n--- START OF JASMIN CODE OUTPUT ---\n");
        System.out.println("\n"+result.getJasminCode());
        System.out.println("\n--- END OF JASMIN CODE OUTPUT ---\n");
        TestUtils.noErrors(result.getReports());
        var output = result.run();
    }

    @Test
    public void testFull2() {
        System.out.println("-------------------");
        System.out.println("Test 2 - Full");
        System.out.println("-------------------");
        var result = TestUtils.backend(SpecsIo.getResource("fixtures/checkpoint3/Pot2.jmm"));
        System.out.println("\n--- START OF JASMIN CODE OUTPUT ---\n");
        System.out.println("\n"+result.getJasminCode());
        System.out.println("\n--- END OF JASMIN CODE OUTPUT ---\n");
        TestUtils.noErrors(result.getReports());
        var output = result.run();
    }

    @Test
    public void testFull3() {
        System.out.println("-------------------");
        System.out.println("Test 3 - Full");
        System.out.println("-------------------");
        var result = TestUtils.backend(SpecsIo.getResource("fixtures/checkpoint3/Pot3.jmm"));
        System.out.println("\n--- START OF JASMIN CODE OUTPUT ---\n");
        System.out.println("\n"+result.getJasminCode());
        System.out.println("\n--- END OF JASMIN CODE OUTPUT ---\n");
        TestUtils.noErrors(result.getReports());
        var output = result.run();
    }

    @Test
    public void testFull4() {
        System.out.println("-------------------");
        System.out.println("Test 4 - Full");
        System.out.println("-------------------");
        var result = TestUtils.backend(SpecsIo.getResource("fixtures/checkpoint3/Pot4.jmm"));
        System.out.println("\n--- START OF JASMIN CODE OUTPUT ---\n");
        System.out.println("\n"+result.getJasminCode());
        System.out.println("\n--- END OF JASMIN CODE OUTPUT ---\n");
        TestUtils.noErrors(result.getReports());
        var output = result.run();
    }
    @Test
    public void testFull5() {
        System.out.println("-------------------");
        System.out.println("Test 5 - Full");
        System.out.println("-------------------");
        var result = TestUtils.backend(SpecsIo.getResource("fixtures/checkpoint3/Pot5.jmm"));
        System.out.println("\n--- START OF JASMIN CODE OUTPUT ---\n");
        System.out.println("\n"+result.getJasminCode());
        System.out.println("\n--- END OF JASMIN CODE OUTPUT ---\n");
        TestUtils.noErrors(result.getReports());
        var output = result.run();
    }
}
