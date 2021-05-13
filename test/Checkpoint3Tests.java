import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.specs.util.SpecsIo;

import static org.junit.Assert.assertEquals;

public class Checkpoint3Tests {

    /**
     * -------- SYNTATIC ---------
     */

    @Test
    public void testSyntatic1() {
        System.out.println("-------------------");
        System.out.println("Test 1 - Syntatic");

        var result = TestUtils.parse(SpecsIo.getResource("fixtures/checkpoint3/syntatic1.jmm"));
        //var parserResult = TestUtils.parse(SpecsIo.getResource("fixtures/public/HelloWorld.jmm"));
    }

    @Test
    public void testSyntatic2() {
        System.out.println("-------------------");
        System.out.println("Test 2 - Syntatic");
    }

    /**
     * -------- SEMANTIC ---------
     */

    @Test
    public void testSemantic1() {
        System.out.println("-------------------");
        System.out.println("Test 1 - Semantic");
    }

    @Test
    public void testSemantic2() {
        System.out.println("-------------------");
        System.out.println("Test 2 - Semantic");
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
        System.out.println("Test 1 - Jasmin");
    }

    @Test
    public void testJasmin2() {
        System.out.println("-------------------");
        System.out.println("Test 2 - Jasmin");
    }

    /**
     * -------- FULL TEST --------
     */

    @Test
    public void testFull1() {
        System.out.println("-------------------");
        System.out.println("Test 1 - Full");
    }

    @Test
    public void testFull2() {
        System.out.println("-------------------");
        System.out.println("Test 2 - Full");
    }
}