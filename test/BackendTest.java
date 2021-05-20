
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

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.junit.Test;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.ollir.OllirUtils;
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
    public void testVarLimit() {

        var result = TestUtils.backend(SpecsIo.getResource("fixtures/public/varLimits.jmm"));
        System.out.println("\n"+result.getJasminCode());
        TestUtils.noErrors(result.getReports());
        var output = result.run();

    }

    @Test
    public void testAssign() {

        var result = TestUtils.backend(SpecsIo.getResource("fixtures/public/Test.jmm"));
        System.out.println("\n"+result.getJasminCode());
        StringTokenizer defaultTokenizer = new StringTokenizer(result.getJasminCode(),"\n");
        int lines = defaultTokenizer.countTokens();
        TestUtils.noErrors(result.getReports());
        var output = result.run();
        assertEquals(lines,25);

    }


    @Test
    public void testIfElse() {
        var result = TestUtils.backend(new OllirResult(SpecsIo.getResource("fixtures/public/ollir/if.ollir")));
        System.out.println("\n"+result.getJasminCode());
        //var result = TestUtils.backend(SpecsIo.getResource("fixtures/public/SimpleIF.jmm"));
        TestUtils.noErrors(result.getReports());
        var output = result.run();

    }

    @Test
    public void testWhile() {
        var result = TestUtils.backend(new OllirResult(SpecsIo.getResource("fixtures/public/ollir/myclass1.ollir")));
        System.out.println("\n"+result.getJasminCode());
        //var result = TestUtils.backend(SpecsIo.getResource("fixtures/public/SimpleIF.jmm"));
        TestUtils.noErrors(result.getReports());
        var output = result.run();

    }

}
