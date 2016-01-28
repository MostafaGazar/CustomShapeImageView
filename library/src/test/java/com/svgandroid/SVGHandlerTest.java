package com.svgandroid;

import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xml.sax.SAXException;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Vlad Medvedev on 22.01.2016.
 * vladislav.medvedev@devfactory.com
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RectF.class})
public class SVGHandlerTest extends SVGHandlerTestSupport {

    @Test
    public void testScale() throws SAXException {
        //given
        when(canvas.getWidth()).thenReturn(2000);
        when(canvas.getHeight()).thenReturn(500);

        //when
        parserHandler.startElement("", "svg", "svg", new AttributesMock(attr("width", "100px"), attr("height", "50px")));

        //then
        verify(canvas).translate(500f, 0.0f);
        verify(canvas).scale(10f, 10f);
    }

    @Test
    public void testParseGroup() throws SAXException {
        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(), "g");
        startElement(parserHandler, attributes(attr("width", "100"), attr("height", "200"), attr("style", "stroke:#ff0000")), "rect");
        endElement(parserHandler, "rect");
        endElement(parserHandler, "g");
        endSVG(parserHandler);

        //then
        verify(canvas).drawRect(0.0f, 0.0f, 100.0f, 200.0f, paint);
    }

    @Test
    public void testParseGroup_boundsMode() throws Exception {
        //given
        RectF createdBounds = mock(RectF.class);
        PowerMockito.whenNew(RectF.class).withArguments(eq(0.0f), eq(0.0f), eq(100.0f), eq(100.0f)).thenReturn(createdBounds);

        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("id", "bounds"), attr("x", "0"), attr("y", "0"), attr("width", "400"), attr("height", "400")), "g");
        startElement(parserHandler, attributes(attr("width", "100"), attr("height", "200"), attr("style", "stroke:#ff0000")), "rect");
        endElement(parserHandler, "rect");
        endElement(parserHandler, "g");
        endSVG(parserHandler);

        //then
        assertThat(parserHandler.bounds, is(createdBounds));
        verify(canvas, never()).drawRect(0.0f, 0.0f, 100.0f, 200.0f, paint);
    }

    @Test
    public void testParseGroup_hidden() throws SAXException {
        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(), "g");
        startElement(parserHandler, attributes(attr("width", "50"), attr("height", "50"), attr("style", "stroke:#ff0000")), "rect");
        endElement(parserHandler, "rect");

        //hidden group
        startElement(parserHandler, attributes(attr("display", "none")), "g");
        startElement(parserHandler, attributes(attr("width", "400"), attr("height", "400"), attr("style", "stroke:#ff0000")), "rect");
        endElement(parserHandler, "rect");
        endElement(parserHandler, "g");

        endElement(parserHandler, "g");
        endSVG(parserHandler);

        //then
        verify(canvas, never()).drawRect(0.0f, 0.0f, 400.0f, 400.0f, paint);
        verify(canvas).drawRect(0.0f, 0.0f, 50.0f, 50.0f, paint);
    }

    @Test
    public void testParseLine() throws SAXException {
        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("x1", "0.0"), attr("y1", "0.0"), attr("x2", "10.0"), attr("y2", "10.0"), attr("style", "stroke:#ff0000")), "line");
        endElement(parserHandler, "line");
        endSVG(parserHandler);

        //then
        verify(canvas).drawLine(0.0f, 0.0f, 10.0f, 10.0f, paint);
    }

    @Test
    public void testParseCircle() throws SAXException {
        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("cx", "10.0"), attr("cy", "10.0"), attr("r", "5"), attr("style", "fill:#ff0000;stroke:#ff0000")), "circle");
        endElement(parserHandler, "circle");
        endSVG(parserHandler);

        //then
        verify(canvas, times(2)).drawCircle(10.0f, 10.0f, 5.0f, paint);
    }

    @Test
    public void testParseEllipse() throws SAXException {
        //when
        startSVG(parserHandler);
        RectF rectF = mock(RectF.class);
        parserHandler.rect = rectF;
        startElement(parserHandler, attributes(attr("cx", "10.0"), attr("cy", "10.0"), attr("rx", "5.0"), attr("ry", "5.0"), attr("style", "fill:#ff0000;stroke:#ff0000")), "ellipse");
        endElement(parserHandler, "ellipse");
        endSVG(parserHandler);

        //then
        verify(rectF).set(5.0f, 5.0f, 15.0f, 15.0f);
        verify(canvas, times(2)).drawOval(rectF, paint);
    }


    @Test
    public void testParseRect() throws SAXException {
        //when
        SVGParser.SVGHandler parserHandler = spy(this.parserHandler);
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("width", "100"), attr("height", "200"), attr("style", "fill:#ff0000;stroke:#ff0000")), "rect");
        endElement(parserHandler, "rect");
        endSVG(parserHandler);

        //then
        verify(canvas, times(2)).drawRect(0.0f, 0.0f, 100.0f, 200.0f, paint);
    }

    @Test
    public void testPushTransofrm() throws SAXException {
        //given
        SVGParser.SVGHandler parserHandler = spy(this.parserHandler);
//        Matrix matrix = mock(Matrix.class);
//        doReturn(matrix).when(parserHandler).createMatrix();

        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("width", "100"), attr("height", "200"), attr("transform", "skewY(50)")), "rect");
        endElement(parserHandler, "rect");
        endSVG(parserHandler);

        //then
        verify(canvas).drawRect(0.0f, 0.0f, 100.0f, 200.0f, paint);
        verify(canvas).save();
        verify(canvas).concat(matrix);
        verify(canvas).restore();

    }

    @Test
    public void testParsePolygon() throws SAXException {
        //given
        SVGParser.SVGHandler parserHandler = spy(this.parserHandler);
//        Path path = mock(Path.class);
//        doReturn(path).when(parserHandler).createPath();

        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("points", "220,10 300,210 170,250 123,234"), attr("style", "fill:#ff0000;stroke:#ff0000;stroke-width:1")), "polygon");
        endElement(parserHandler, "polygon");
        endSVG(parserHandler);

        //then
        verify(path).moveTo(220.0f, 10.0f);
        verify(path).lineTo(300.0f, 210.0f);
        verify(path).lineTo(170.0f, 250.0f);
        verify(path).lineTo(123.0f, 234.0f);
        verify(path).close();
        verify(canvas, times(2)).drawPath(path, paint);
    }

    @Test
    public void testParsePath() throws SAXException {
        //given
        SVGParser.SVGHandler parserHandler = Mockito.spy(this.parserHandler);
//        Path path = mock(Path.class);
//        doReturn(path).when(parserHandler).createPath();

        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("d", "M150 0 L75 200 L225 200 Z"), attr("style", "fill:#ff0000;stroke:#ff0000;stroke-width:1")), "path");
        endElement(parserHandler, "path");
        endSVG(parserHandler);

        //then
        verify(canvas, times(2)).drawPath(path, paint);
    }

    @Test
    public void testParseLinearGradient() throws Exception {
        //given
        when(picture.beginRecording(anyInt(), anyInt())).thenReturn(canvas);
        LinearGradient linearGradient = mock(LinearGradient.class);
        PowerMockito.whenNew(LinearGradient.class).withArguments(
                eq(10.1f), eq(4.1f), eq(11.1f), eq(12.2f), eq(new int[]{-2130771968}), eq(new float[]{10.1f}), eq(Shader.TileMode.CLAMP)).
                thenReturn(linearGradient);

        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("id", "g1"), attr("x1", "10.1"), attr("y1", "4.1"), attr("x2", "11.1"), attr("y2", "12.2")), "linearGradient");
        startElement(parserHandler, attributes(attr("offset", "10.1"), attr("style", "stop-color:#ff0000;stop-opacity:0.5")), "stop");
        endElement(parserHandler, "stop");
        endElement(parserHandler, "linearGradient");
        startElement(parserHandler, attributes(attr("width", "100"), attr("height", "100"), attr("fill", "url(#g1)")), "rect");
        endElement(parserHandler, "rect");

        endSVG(parserHandler);

        //then
        verify(paint).setShader(linearGradient);
    }

    @Test
    public void testParseLinearGradient_xlink() throws Exception {
        //given
        when(picture.beginRecording(anyInt(), anyInt())).thenReturn(canvas);
        SVGParser.SVGHandler parserHandler = spy(this.parserHandler);
        LinearGradient gr1 = mock(LinearGradient.class);
        PowerMockito.whenNew(LinearGradient.class).withArguments(eq(10.1f), eq(4.1f), eq(11.1f), eq(0.0f), eq(new int[0]), eq(new float[0]), eq(Shader.TileMode.CLAMP)).
                thenReturn(gr1);

        LinearGradient gr2 = mock(LinearGradient.class);
        PowerMockito.whenNew(LinearGradient.class).withArguments(eq(5.1f), eq(1.1f), eq(20.1f), eq(25.0f), eq(new int[0]), eq(new float[0]), eq(Shader.TileMode.CLAMP)).
                thenReturn(gr2);

        //when
        startSVG(parserHandler);
        //parent
        startElement(parserHandler, attributes(attr("id", "gr1"), attr("x1", "10.1"), attr("y1", "4.1"), attr("x2", "11.1"), attr("gradientTransform", "matrix(0.2883 0 0 0.2865 153.3307 265.0264)")), "linearGradient");
        endElement(parserHandler, "linearGradient");
        //child
        startElement(parserHandler, attributes(attr("id", "gr2"), attr("x1", "5.1"), attr("y1", "1.1"), attr("x2", "20.1"), attr("y2", "25.0"), attr("href", "#gr1")), "linearGradient");
        endElement(parserHandler, "linearGradient");
        endSVG(parserHandler);

        //then
        verify(gr2).setLocalMatrix(matrix);
    }

    @Test
    public void testParseRadialGradient() throws Exception {
        //given
        when(picture.beginRecording(anyInt(), anyInt())).thenReturn(canvas);
        SVGParser.SVGHandler parserHandler = spy(this.parserHandler);
        RadialGradient gradient = mock(RadialGradient.class);
        PowerMockito.whenNew(RadialGradient.class).withArguments(
                eq(10.1f), eq(4.1f), eq(5.0f), eq(new int[]{-65536}), eq(new float[]{10.1f}), eq(Shader.TileMode.CLAMP)
        ).thenReturn(gradient);

        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("id", "g1"), attr("cx", "10.1"), attr("cy", "4.1"), attr("r", "5.0")), "radialGradient");
        startElement(parserHandler, attributes(attr("offset", "10.1"), attr("style", "stop-color:ff0000")), "stop");
        endElement(parserHandler, "stop");
        endElement(parserHandler, "radialGradient");
        startElement(parserHandler, attributes(attr("width", "100"), attr("height", "100"), attr("fill", "url(#g1)")), "rect");
        endElement(parserHandler, "rect");
        endSVG(parserHandler);

        //then
        verify(paint).setShader(gradient);
        ;
    }

    @Test
    public void testParseRadialGradient_xlink() throws Exception {
        //given
        when(picture.beginRecording(anyInt(), anyInt())).thenReturn(canvas);
        SVGParser.SVGHandler parserHandler = spy(this.parserHandler);

        RadialGradient gr1 = mock(RadialGradient.class);
        PowerMockito.whenNew(RadialGradient.class).withArguments(eq(10.1f), eq(4.1f), eq(5.0f), eq(new int[0]), eq(new float[0]), eq(Shader.TileMode.CLAMP)).
                thenReturn(gr1);

        RadialGradient gr2 = mock(RadialGradient.class);
        PowerMockito.whenNew(RadialGradient.class).withArguments(eq(5.0f), eq(5.0f), eq(2.0f), eq(new int[0]), eq(new float[0]), eq(Shader.TileMode.CLAMP)).
                thenReturn(gr2);


        Matrix subMatrix = mock(Matrix.class);
        PowerMockito.whenNew(Matrix.class).withArguments(matrix).thenReturn(subMatrix);

        //when
        startSVG(parserHandler);
        //parent gradient
        startElement(parserHandler, attributes(attr("id", "gr1"), attr("cx", "10.1"), attr("cy", "4.1"), attr("r", "5.0"), attr("gradientTransform", "matrix(0.2883 0 0 0.2865 153.3307 265.0264)")), "radialGradient");
        endElement(parserHandler, "radialGradient");
        //child gradient
        startElement(parserHandler, attributes(attr("id", "gr2"), attr("cx", "5.0"), attr("cy", "5.0"), attr("r", "2.0"), attr("href", "#gr1")), "radialGradient");
        endElement(parserHandler, "radialGradient");
        endSVG(parserHandler);

        //then
        verify(gr1, times(1)).setLocalMatrix(matrix);
        verify(gr2, times(1)).setLocalMatrix(subMatrix);
        verify(matrix).setValues(new float[]{0.2883f, 0.0f, 153.3307f, 0.0f, 0.2865f, 265.0264f, 0.0f, 0.0f, 1.0f});
    }

    @Test
    public void testDoFill_none() {
        boolean res = parserHandler.doFill(
                new SVGParser.Properties(attributes(attr("display", "none"))),
                new HashMap<String, Shader>());

        assertThat(res, is(false));
    }

    @Test
    public void testDoFill_whiteMode() {
        parserHandler.setWhiteMode(true);

        boolean res = parserHandler.doFill(
                new SVGParser.Properties(attributes()),
                new HashMap<String, Shader>());

        verify(paint).setStyle(Paint.Style.FILL);
        verify(paint).setColor(0xFFFFFFFF);
        assertThat(res, is(true));
    }

    @Test
    public void testDoFill_byURL() {
        //given
        HashMap<String, Shader> gradients = new HashMap<>();
        Shader shader = mock(Shader.class);
        gradients.put("gr1", shader);

        //when
        boolean res = parserHandler.doFill(
                new SVGParser.Properties(attributes(attr("fill", "url(#gr1)"))),
                gradients);

        //then
        assertThat(res, is(true));
        verify(paint).setShader(shader);
        verify(paint).setStyle(Paint.Style.FILL);
    }

    @Test
    public void testDoFill_byURL_shaderNotFound() {
        //given
        HashMap<String, Shader> gradients = new HashMap<>();
        Shader shader = mock(Shader.class);
        gradients.put("gr1", shader);

        //when
        boolean res = parserHandler.doFill(
                new SVGParser.Properties(attributes(attr("fill", "#gr2)"))),
                gradients);

        //then
        assertThat(res, is(false));
    }

    @Test
    public void testDoFill_byHexColor() {
        //given
        SVGParser.Properties properties = new SVGParser.Properties(attributes(attr("fill", "#ff0000")));

        //when
        boolean res = parserHandler.doFill(
                properties,
                new HashMap<String, Shader>());

        //then
        verify(paint).setStyle(Paint.Style.FILL);
        Integer hexColor = properties.getHex("fill");
        verify(paint).setColor((0xFFFFFF & hexColor) | 0xFF000000);
        assertThat(res, is(true));
    }

    @Test
    public void testDoFill_default() {
        boolean res = parserHandler.doFill(
                new SVGParser.Properties(attributes()),
                new HashMap<String, Shader>());

        verify(paint).setStyle(Paint.Style.FILL);
        verify(paint).setColor(0xFF000000);
        assertThat(res, is(true));
    }

    @Test
    public void testDoStroke_whiteModeTrue() {
        parserHandler.setWhiteMode(true);

        assertThat(parserHandler.doStroke(new SVGParser.Properties(attributes())), is(false));
    }

    @Test
    public void testDoStroke_displayNone() {
        assertThat(parserHandler.doStroke(new SVGParser.Properties(attributes())), is(false));
    }

    @Test
    public void testDoStroke() {
        assertThat(parserHandler.doStroke(new SVGParser.Properties(attributes(attr("stroke", "#ff0000")))), is(true));
        verify(paint).setStyle(Paint.Style.STROKE);
    }


    private void testDoStroke_strokeLinecap(String val) {
        assertThat(parserHandler.doStroke(new SVGParser.Properties(attributes(attr("stroke", "#ff0000"), attr("stroke-linecap", val)))), is(true));
        verify(paint).setStyle(Paint.Style.STROKE);
    }

    @Test
    public void testDoStroke_strokeLinecapRound() {
        testDoStroke_strokeLinecap("round");
        verify(paint).setStrokeCap(Paint.Cap.ROUND);
    }

    @Test
    public void testDoStroke_strokeLinecapSquare() {
        testDoStroke_strokeLinecap("square");
        verify(paint).setStrokeCap(Paint.Cap.SQUARE);
    }

    @Test
    public void testDoStroke_strokeLinecapButt() {
        testDoStroke_strokeLinecap("butt");
        verify(paint).setStrokeCap(Paint.Cap.BUTT);
    }

    private void testDoStroke_linejoin(String val) {
        assertThat(parserHandler.doStroke(new SVGParser.Properties(attributes(attr("stroke", "#ff0000"), attr("stroke-linejoin", val)))), is(true));
        verify(paint).setStyle(Paint.Style.STROKE);
    }

    @Test
    public void testDoStroke_linejoinMiter() {
        testDoStroke_linejoin("miter");
        verify(paint).setStrokeJoin(Paint.Join.MITER);
    }

    @Test
    public void testDoStroke_linejoinRound() {
        testDoStroke_linejoin("round");
        verify(paint).setStrokeJoin(Paint.Join.ROUND);
    }

    @Test
    public void testDoStroke_linejoinBevel() {
        testDoStroke_linejoin("bevel");
        verify(paint).setStrokeJoin(any(Paint.Join.class));
    }


}