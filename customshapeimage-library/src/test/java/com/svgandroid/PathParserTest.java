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
    public void testParseLineNext() throws Exception {
        Path path = mock(Path.class);
        PathParser.parse("l5,5+10,10", path);

        //line
        verify(path).rLineTo(eq(5.0f), eq(5.0f));
        verify(path).rLineTo(eq(10.0f), eq(10.0f));
    }

    @Test
    public void testParseCubicPlus() throws Exception {
        Path path = mock(Path.class);
        PathParser.parse("c3,3,3,3,3,3+10,10,10,10,10,10", path);

        //cubic bezier
        verify(path).cubicTo(eq(3.0f), eq(3.0f), eq(3.0f), eq(3.0f), eq(3.0f), eq(3.0f));
        verify(path).cubicTo(eq(13.0f), eq(13.0f), eq(13.0f), eq(13.0f), eq(13.0f), eq(13.0f));
    }


    @Test
    public void testParseMoveNext() throws Exception {
        Path path = mock(Path.class);
        PathParser.parse("m1,2+10,23", path);

        //move
        verify(path).rMoveTo(eq(1.0f), eq(2.0f));
        verify(path).rLineTo(eq(10.0f), eq(23.0f));
    }

    @Test
    public void testParseMove() throws Exception {
        Path path = mock(Path.class);
        PathParser.parse("M10,10m10,10", path);

        //move
        verify(path).moveTo(eq(10.0f), eq(10.0f));
        verify(path).rMoveTo(eq(10.0f), eq(10.0f));
    }


    @Test
    public void testParseLine() throws Exception {
        Path path = mock(Path.class);
        PathParser.parse("ML10,10l0,10", path);

        //line
        verify(path).lineTo(eq(10.0f), eq(10.0f));
        verify(path).rLineTo(eq(0.0f), eq(10.0f));
    }

    @Test
    public void testParseHLine() throws Exception {
        Path path = mock(Path.class);
        PathParser.parse("H10h5", path);

        //horizontal line
        verify(path).lineTo(eq(10.0f),eq(0.0f));
        verify(path).rLineTo(eq(5.0f),eq(0.0f));
    }

    @Test
    public void testParseVLine() throws Exception {
        Path path = mock(Path.class);
        PathParser.parse("V10v5", path);

        //vertical line
        verify(path).lineTo(eq(0.0f),eq(10.0f));
        verify(path).rLineTo(eq(0.0f),eq(5.0f));
    }

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
    public void testParseCurve() throws Exception {
        Path path = mock(Path.class);
        PathParser.parse("C150,150,180,80,100,120c30,100,40,100,30,90", path);

        //draw curve
        verify(path).cubicTo(eq(150.0f), eq(150.0f), eq(180.0f), eq(80.0f), eq(100.0f), eq(120.0f));
        verify(path).cubicTo(eq(130.0f), eq(220.0f), eq(140.0f), eq(220.0f), eq(130.0f), eq(210.0f));
    }

    @Test
    public void testParseCurveS() throws Exception {
        Path path = mock(Path.class);
        PathParser.parse("S150,150,180,80s30,10,20,35", path);

        //draw curve
        verify(path).cubicTo(eq(0.0f), eq(0.0f), eq(150.0f), eq(150.0f), eq(180.0f), eq(80.0f));
        verify(path).cubicTo(eq(210.0f), eq(10.0f), eq(210.0f), eq(90.0f), eq(200.0f), eq(115.0f));
    }

    @Test
    public void testParseArc() throws Exception {
        Path path = mock(Path.class);
        //is not implemented yet. Just call it
        PathParser.parse("A5,5,20,20,30,10,10", path);
    }

}