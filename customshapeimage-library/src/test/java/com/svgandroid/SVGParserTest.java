package com.svgandroid;

import android.graphics.Paint;
import android.graphics.Picture;

import org.junit.Test;

import java.io.InputStream;
import java.net.URL;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by Vlad Medvedev on 21.01.2016.
 * vladislav.medvedev@devfactory.com
 */
public class SVGParserTest {
    @Test
    public void parseShape() {
        //given
        InputStream resourceAsStream = this.getClass().getResourceAsStream("shape_star.svg");
        Picture picture = mock(Picture.class);
        Paint paint = mock(Paint.class);
        SVGParser.SVGHandler handler = spy(new SVGParser.SVGHandler(picture, paint));
        doNothing().when(handler).logDebug(anyString(), anyString());

        //when
        SVG svgFromInputStream = SVGParser.parse(resourceAsStream, 0, 0, false, handler, picture);

        //then
        assertThat(svgFromInputStream, is(not(nullValue())));
    }

    @Test
    public void parseNumbers() {
        SVGParser.NumberParse numberParse = SVGParser.parseNumbers("10.0  -10.0-1f");
        assertThat(numberParse.getNumber(0), is(10.0f));
        assertThat(numberParse.getNumber(1), is(-10.0f));
        assertThat(numberParse.getNumber(2), is(-1.0f));
        assertThat(numberParse.getNextCmd(), is(14));
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