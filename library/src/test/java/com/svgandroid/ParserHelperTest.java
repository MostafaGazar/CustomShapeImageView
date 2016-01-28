package com.svgandroid;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by Vlad Medvedev on 25.01.2016.
 * vladislav.medvedev@devfactory.com
 */
public class ParserHelperTest {

    @Test
    public void testNextFloat() {
        ParserHelper parserHelper = new ParserHelper("+0.1 +05.1 -10.2E-13 1.234e2 3.8E8 .7e3 -1.4e-45 3.4e+38f", 0);

        assertThat(parserHelper.parseFloat(), is(0.1f));
        assertThat(parserHelper.nextFloat(), is(5.1f));
        assertThat(parserHelper.nextFloat(), is(-1.02E-12F));
        assertThat(parserHelper.nextFloat(), is(1.234e2F));
        assertThat(parserHelper.nextFloat(), is(3.8E8F));
        assertThat(parserHelper.nextFloat(), is(700.0F));
        assertThat(parserHelper.nextFloat(), is(-1.4E-45F));
        assertThat(parserHelper.nextFloat(), is(3.4e+38f));
    }

    @Test
    public void testParseFloat() {
        assertThat(new ParserHelper("0", 0).parseFloat(), is(0.0f));
        assertThat(new ParserHelper("0.09", 0).parseFloat(), is(0.09F));
        assertThat(new ParserHelper("0.0e3", 0).parseFloat(), is(0.0F));
        assertThat(new ParserHelper("0.01", 0).parseFloat(), is(0.01F));
        assertThat(new ParserHelper("foo", 0).parseFloat(), is(Float.NaN));
    }


    private void testParseFloat_UnexpectedChar(String val) {
        ParserHelper parserHelper = new ParserHelper(val, 0);
        try {
            parserHelper.parseFloat();
            fail("method must throw RuntimeException when parse wrong value");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is(containsString("Unexpected char")));
        }
    }

    @Test
    public void testParseFloat_UnexpectedCharAfterE() {
        testParseFloat_UnexpectedChar("10.2EA13");
    }

    @Test
    public void testParseFloat_UnexpectedCharAfterPlus() {
        testParseFloat_UnexpectedChar("10.2E+A");
    }

    @Test
    public void testParseFloat_UnexpectedCharAfterDot() {
        testParseFloat_UnexpectedChar(".e+3");
    }

    @Test
    public void testBuildFloat() {
        assertThat(ParserHelper.buildFloat(0, 0), is(0.0f));
        assertThat(ParserHelper.buildFloat(1, 129), is(Float.POSITIVE_INFINITY));
        assertThat(ParserHelper.buildFloat(-1, 128), is(Float.NEGATIVE_INFINITY));
        assertThat(ParserHelper.buildFloat(1, 0), is(1f));
        assertThat(ParserHelper.buildFloat(1, 2), is(100f));
        assertThat(ParserHelper.buildFloat(100000000, 1), is(1.0E9F));
    }
}