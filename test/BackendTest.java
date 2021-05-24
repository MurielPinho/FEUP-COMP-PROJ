
/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.specs.util.SpecsIo;


public class BackendTest {

    @Test
    public void testHelloWorld() {

        var result = TestUtils.backend(SpecsIo.getResource("fixtures/public/HelloWorld.jmm"));
        System.out.println("\n"+result.getJasminCode());
        TestUtils.noErrors(result.getReports());
        var output = result.run();
        assertEquals("Hello, World!", output.trim());
    }

    @Test
    public void testPrecedence() {

        var result = TestUtils.backend(SpecsIo.getResource("fixtures/checkpoint3/jasmin1.jmm"));
        System.out.println("\n"+result.getJasminCode());
        TestUtils.noErrors(result.getReports());
        var output = result.run();
        assertEquals("64", output.trim());

    }

    @Test
    public void testIfElseWhile() {
        var result = TestUtils.backend(new OllirResult(SpecsIo.getResource("fixtures/checkpoint3/jasmin2.ollir")));
        System.out.println("\n"+result.getJasminCode());
        TestUtils.noErrors(result.getReports());
        var output = result.run();
        assertEquals("128", output.trim());
    }

    @Test
    public void testWhile() {
        var result = TestUtils.backend(new OllirResult(SpecsIo.getResource("fixtures/public/ollir/myclass1.ollir")));
        System.out.println("\n"+result.getJasminCode());
        TestUtils.noErrors(result.getReports());
        var output = result.run();

    }
    @Test
    public void testFac() {

        var result = TestUtils.backend(new OllirResult(SpecsIo.getResource("fixtures/public/ollir/Fac.ollir")));
        System.out.println("\n"+result.getJasminCode());
        TestUtils.noErrors(result.getReports());
        var output = result.run();

    }


    @Test
    public void testPot1() {

        var result = TestUtils.backend(SpecsIo.getResource("fixtures/checkpoint3/Pot1.jmm"));
        System.out.println("\n"+result.getJasminCode());
        TestUtils.noErrors(result.getReports());
        var output = result.run();

    }

    @Test
    public void testPot2() {

        var result = TestUtils.backend(SpecsIo.getResource("fixtures/checkpoint3/Pot2.jmm"));
        System.out.println("\n"+result.getJasminCode());
        TestUtils.noErrors(result.getReports());
        var output = result.run();

    }
    @Test
    public void testPot3() {

        var result = TestUtils.backend(SpecsIo.getResource("fixtures/checkpoint3/Pot3.jmm"));
        System.out.println("\n"+result.getJasminCode());
        TestUtils.noErrors(result.getReports());
        var output = result.run();

    }

    @Test
    public void testPot4() {

        var result = TestUtils.backend(SpecsIo.getResource("fixtures/checkpoint3/Pot4.jmm"));
        System.out.println("\n"+result.getJasminCode());
        TestUtils.noErrors(result.getReports());
        var output = result.run();

    }


    @Test
    public void testPot5() {

        var result = TestUtils.backend(SpecsIo.getResource("fixtures/checkpoint3/Pot5.jmm"));
        System.out.println("\n"+result.getJasminCode());
        TestUtils.noErrors(result.getReports());
        var output = result.run();

    }



}
