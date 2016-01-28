package com.svgandroid;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

/**
 * Created by Vlad Medvedev on 21.01.2016.
 * vladislav.medvedev@devfactory.com
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SVGParser.class, SVGParser.SVGHandler.class})
public class SVGParserTest extends SVGTestSupport {
    @Test
    public void parseShape() {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("shape_star.svg");
        assertThat(SVGParser.getSVGFromInputStream(resourceAsStream, 0, 0), is(not(nullValue())));
    }

    @Test
    public void parseNumbers() {
        String numbers = "10.0  -10.0-1f";
        SVGParser.NumberParse numberParse = SVGParser.parseNumbers(numbers);
        assertThat(numberParse.getNumber(0), is(10.0f));
        assertThat(numberParse.getNumber(1), is(-10.0f));
        assertThat(numberParse.getNumber(2), is(-1.0f));
        assertThat(numberParse.getNextCmd(), is(numbers.length()));
    }

    @Test
    public void testGetHexAttr_null() {
        Integer res = SVGParser.getHexAttr("someColor", new AttributesMock());
        assertThat(res, is(nullValue()));
    }

    @Test
    public void testGetHexAttr() {
        Integer res = SVGParser.getHexAttr("someColor", new AttributesMock(new AttributesMock.Pair("someColor", "#ff0000")));
        assertThat(res, is(16711680));
    }

    @Test
    public void testGetHexAttr_wrongFormat() {
        Integer res = SVGParser.getHexAttr("someColor", new AttributesMock(new AttributesMock.Pair("someColor", "#blabla")));
        assertThat(res, is(nullValue()));
    }
}