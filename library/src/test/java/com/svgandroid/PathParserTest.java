package com.svgandroid;

import android.graphics.Path;

import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Vlad Medvedev on 19.01.2016.
 * vladislav.medvedev@devfactory.com
 */
public class PathParserTest {

    @Test
    public void testParseSquare() throws Exception {
        Path path = mock(Path.class);
        PathParser.parse("M10,10H90V90H10L10,10", path);

        //draw square
        verify(path, times(1)).moveTo(eq(10.0f), eq(10.0f));
        verify(path).lineTo(eq(90.0f), eq(10.0f));
        verify(path).lineTo(eq(90.0f), eq(90.0f));
        verify(path).lineTo(eq(10.0f), eq(90.0f));
        verify(path).lineTo(eq(10.0f), eq(10.0f));
    }

    @Test
    public void testParseTriangle() throws Exception {
        Path path = mock(Path.class);
        PathParser.parse("M250,150L150,350L350,350Z", path);

        //draw triangle
        verify(path, times(2)).moveTo(eq(250.0f), eq(150.0f));
        verify(path).lineTo(eq(150.0f), eq(350.0f));
        verify(path).lineTo(eq(350.0f), eq(350.0f));
        verify(path).close();
    }

    @Test
    public void testParseCurveS() throws Exception {
        Path path = mock(Path.class);
        PathParser.parse("M10,10S150,150,180,80", path);

        //draw curve
        verify(path).moveTo(eq(10.0f), eq(10.0f));
        verify(path).cubicTo(eq(10.0f), eq(10.0f), eq(150.0f), eq(150.0f), eq(180.0f), eq(80.0f));
    }

}