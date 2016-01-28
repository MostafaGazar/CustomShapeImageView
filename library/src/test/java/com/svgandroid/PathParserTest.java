package com.svgandroid;

import android.graphics.Path;

import org.junit.Before;
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
    private Path path;

    @Before
    public void setUp(){
        path = mock(Path.class);
    }
    
    @Test
    public void testParseLineNext() throws Exception {
        PathParser.parse("l5,5+10,10", path);

        //line
        verify(path).rLineTo(5.0f, 5.0f);
        verify(path).rLineTo(10.0f, 10.0f);
    }

    @Test
    public void testParseCubicPlus() throws Exception {
        PathParser.parse("c3,3,3,3,3,3+10,10,10,10,10,10", path);

        //cubic bezier
        verify(path).cubicTo(3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f);
        verify(path).cubicTo(13.0f, 13.0f, 13.0f, 13.0f, 13.0f, 13.0f);
    }


    @Test
    public void testParseMoveNext() throws Exception {
        PathParser.parse("m1,2+10,23", path);

        //move
        verify(path).rMoveTo(1.0f, 2.0f);
        verify(path).rLineTo(10.0f, 23.0f);
    }

    @Test
    public void testParseMove() throws Exception {
        PathParser.parse("M10,10m10,10", path);

        //move
        verify(path).moveTo(10.0f, 10.0f);
        verify(path).rMoveTo(10.0f, 10.0f);
    }


    @Test
    public void testParseLine() throws Exception {
        PathParser.parse("ML10,10l0,10", path);

        //line
        verify(path).lineTo(10.0f, 10.0f);
        verify(path).rLineTo(0.0f, 10.0f);
    }

    @Test
    public void testParseHLine() throws Exception {
        PathParser.parse("H10h5", path);

        //horizontal line
        verify(path).lineTo(10.0f,0.0f);
        verify(path).rLineTo(5.0f,0.0f);
    }

    @Test
    public void testParseVLine() throws Exception {
        PathParser.parse("V10v5", path);

        //vertical line
        verify(path).lineTo(0.0f,10.0f);
        verify(path).rLineTo(0.0f,5.0f);
    }

    @Test
    public void testParseSquare() throws Exception {
        PathParser.parse("M10,10H90V90H10L10,10", path);

        //draw square
        verify(path, times(1)).moveTo(10.0f, 10.0f);
        verify(path).lineTo(90.0f, 10.0f);
        verify(path).lineTo(90.0f, 90.0f);
        verify(path).lineTo(10.0f, 90.0f);
        verify(path).lineTo(10.0f, 10.0f);
    }

    @Test
    public void testParseTriangle() throws Exception {
        PathParser.parse("M250,150L150,350L350,350Z", path);

        //draw triangle
        verify(path, times(2)).moveTo(250.0f, 150.0f);
        verify(path).lineTo(150.0f, 350.0f);
        verify(path).lineTo(350.0f, 350.0f);
        verify(path).close();
    }

    @Test
    public void testParseCurve() throws Exception {
        PathParser.parse("C150,150,180,80,100,120c30,100,40,100,30,90", path);

        //draw curve
        verify(path).cubicTo(150.0f, 150.0f, 180.0f, 80.0f, 100.0f, 120.0f);
        verify(path).cubicTo(130.0f, 220.0f, 140.0f, 220.0f, 130.0f, 210.0f);
    }

    @Test
    public void testParseCurveS() throws Exception {
        PathParser.parse("S150,150,180,80s30,10,20,35", path);

        //draw curve
        verify(path).cubicTo(0.0f, 0.0f, 150.0f, 150.0f, 180.0f, 80.0f);
        verify(path).cubicTo(210.0f, 10.0f, 210.0f, 90.0f, 200.0f, 115.0f);
    }

    @Test
    public void testParseArc() throws Exception {
        //is not implemented yet. Just call it
        PathParser.parse("A5,5,20,20,30,10,10", path);
    }

}