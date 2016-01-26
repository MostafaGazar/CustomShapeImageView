package com.svgandroid;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
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
public class SVGHandlerTest {
    private SVGParser.SVGHandler parserHandler;
    private Canvas canvas;
    private Paint paint;
    private Picture picture;

    private void startSVG(SVGParser.SVGHandler svgHandler) throws SAXException {
        svgHandler.startElement("", "svg", "svg", new AttributesMock(attr("width", "200"), attr("height", "400"))
        );
    }

    private void endSVG(SVGParser.SVGHandler svgHandler) throws SAXException {
        svgHandler.endElement("", "svg", "svg");
    }

    private SVGParser.SVGHandler startElement(SVGParser.SVGHandler svgHandler, AttributesMock attr, String element) throws SAXException {
        svgHandler.startElement("", element, element, attr);
        return svgHandler;
    }

    private SVGParser.SVGHandler endElement(SVGParser.SVGHandler svgHandler, String element) throws SAXException {
        svgHandler.endElement("", element, element);
        return svgHandler;
    }

    private AttributesMock.Pair attr(String name, String value) {
        return new AttributesMock.Pair(name, value);
    }

    private AttributesMock attributes(AttributesMock.Pair... params) {
        return new AttributesMock(params);
    }

    @Before
    public void setUp() {
        picture = mock(Picture.class);
        canvas = mock(Canvas.class);
        when(picture.beginRecording(anyInt(), anyInt())).thenReturn(canvas);
        paint = mock(Paint.class);
        parserHandler = new SVGParser.SVGHandler(picture, paint, 10, 20);
    }

    @Test
    public void testScale() throws SAXException {
        //given
        when(canvas.getWidth()).thenReturn(2000);
        when(canvas.getHeight()).thenReturn(500);

        //when
        parserHandler.startElement("", "svg", "svg", new AttributesMock(attr("width", "100px"), attr("height", "50px")));

        //then
        verify(canvas).translate(eq(500f), eq(0.0f));
        verify(canvas).scale(eq(10f), eq(10f));
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
        verify(canvas).drawRect(eq(0.0f), eq(0.0f), eq(100.0f), eq(200.0f), eq(paint));
    }

    @Test
    public void testParseGroup_boundsMode() throws SAXException {
        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("id", "bounds"), attr("x", "0"), attr("y", "0"), attr("width", "400"), attr("height", "400")), "g");

        startElement(parserHandler, attributes(attr("width", "100"), attr("height", "200"), attr("style", "stroke:#ff0000")), "rect");
        endElement(parserHandler, "rect");
        endElement(parserHandler, "g");
        endSVG(parserHandler);

        //then
        verify(canvas, never()).drawRect(eq(0.0f), eq(0.0f), eq(100.0f), eq(200.0f), eq(paint));
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
        verify(canvas, never()).drawRect(eq(0.0f), eq(0.0f), eq(400.0f), eq(400.0f), eq(paint));
        verify(canvas).drawRect(eq(0.0f), eq(0.0f), eq(50.0f), eq(50.0f), eq(paint));
    }

    @Test
    public void testParseLine() throws SAXException {
        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("x1", "0.0"), attr("y1", "0.0"), attr("x2", "10.0"), attr("y2", "10.0"), attr("style", "stroke:#ff0000")), "line");
        endElement(parserHandler, "line");
        endSVG(parserHandler);

        //then
        verify(canvas).drawLine(eq(0.0f), eq(0.0f), eq(10.0f), eq(10.0f), eq(paint));
    }

    @Test
    public void testParseCircle() throws SAXException {
        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("cx", "10.0"), attr("cy", "10.0"), attr("r", "5"), attr("style", "fill:#ff0000;stroke:#ff0000")), "circle");
        endElement(parserHandler, "circle");
        endSVG(parserHandler);

        //then
        verify(canvas, times(2)).drawCircle(eq(10.0f), eq(10.0f), eq(5.0f), eq(paint));
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
        verify(rectF).set(eq(5.0f), eq(5.0f), eq(15.0f), eq(15.0f));
        verify(canvas, times(2)).drawOval(eq(rectF), eq(paint));
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
        verify(canvas, times(2)).drawRect(eq(0.0f), eq(0.0f), eq(100.0f), eq(200.0f), eq(paint));
    }

    @Test
    public void testPushTransofrm() throws SAXException {
        //given
        SVGParser.SVGHandler parserHandler = spy(this.parserHandler);
        Matrix matrix = mock(Matrix.class);
        doReturn(matrix).when(parserHandler).createMatrix();

        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("width", "100"), attr("height", "200"), attr("transform", "skewY(50)")), "rect");
        endElement(parserHandler, "rect");
        endSVG(parserHandler);

        //then
        verify(canvas).drawRect(eq(0.0f), eq(0.0f), eq(100.0f), eq(200.0f), eq(paint));
        verify(canvas).save();
        verify(canvas).concat(eq(matrix));
        verify(canvas).restore();

    }

    @Test
    public void testParsePolygon() throws SAXException {
        //given
        SVGParser.SVGHandler parserHandler = spy(this.parserHandler);
        Path path = mock(Path.class);
        doReturn(path).when(parserHandler).createPath();

        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("points", "220,10 300,210 170,250 123,234"), attr("style", "fill:#ff0000;stroke:#ff0000;stroke-width:1")), "polygon");
        endElement(parserHandler, "polygon");
        endSVG(parserHandler);

        //then
        verify(path).moveTo(eq(220.0f), eq(10.0f));
        verify(path).lineTo(eq(300.0f), eq(210.0f));
        verify(path).lineTo(eq(170.0f), eq(250.0f));
        verify(path).lineTo(eq(123.0f), eq(234.0f));
        verify(path).close();
        verify(canvas, times(2)).drawPath(eq(path), eq(paint));
    }

    @Test
    public void testParsePath() throws SAXException {
        //given
        SVGParser.SVGHandler parserHandler = Mockito.spy(this.parserHandler);
        Path path = mock(Path.class);
        doReturn(path).when(parserHandler).createPath();

        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("d", "M150 0 L75 200 L225 200 Z"), attr("style", "fill:#ff0000;stroke:#ff0000;stroke-width:1")), "path");
        endElement(parserHandler, "path");
        endSVG(parserHandler);

        //then
        verify(canvas, times(2)).drawPath(eq(path), eq(paint));
    }

    @Test
    public void testParseLinearGradient() throws SAXException {
        //given
        when(picture.beginRecording(anyInt(), anyInt())).thenReturn(canvas);
        SVGParser.SVGHandler parserHandler = spy(this.parserHandler);
        LinearGradient gradient = mock(LinearGradient.class);
        doReturn(gradient).when(parserHandler).createLinearGradient(anyInt(), anyFloat(), anyFloat(), anyFloat(), any(int[].class), any(float[].class), any(Shader.TileMode.class));
        Matrix matrix = mock(Matrix.class);
        doReturn(matrix).when(parserHandler).createMatrix();

        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("id", "g1"), attr("x1", "10.1"), attr("y1", "4.1"), attr("x2", "11.1"), attr("y2", "12.2")), "linearGradient");
        startElement(parserHandler, attributes(attr("offset", "10.1"), attr("style", "stop-color:#ff0000;stop-opacity:0.5")), "stop");
        endElement(parserHandler, "stop");
        endElement(parserHandler, "linearGradient");
        endSVG(parserHandler);

        //then
        verify(parserHandler).createLinearGradient(eq(10.1f), eq(4.1f), eq(11.1f), eq(12.2f), eq(new int[]{-2130771968}), eq(new float[]{10.1f}), eq(Shader.TileMode.CLAMP));
    }

    @Test
    public void testParseLinearGradient_xlink() throws SAXException {
        //given
        when(picture.beginRecording(anyInt(), anyInt())).thenReturn(canvas);
        SVGParser.SVGHandler parserHandler = spy(this.parserHandler);
        LinearGradient gradient = mock(LinearGradient.class);
        doReturn(gradient).when(parserHandler).createLinearGradient(anyInt(), anyFloat(), anyFloat(), anyFloat(), any(int[].class), any(float[].class), any(Shader.TileMode.class));
        Matrix matrix = mock(Matrix.class);
        doReturn(matrix).when(parserHandler).createMatrix();
        doNothing().when(parserHandler).logDebug(anyString(), anyString());

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
        verify(parserHandler).createLinearGradient(eq(10.1f), eq(4.1f), eq(11.1f), eq(0.0f), eq(new int[0]), eq(new float[0]), eq(Shader.TileMode.CLAMP));
        verify(parserHandler).createLinearGradient(eq(5.1f), eq(1.1f), eq(20.1f), eq(25.0f), eq(new int[0]), eq(new float[0]), eq(Shader.TileMode.CLAMP));
        verify(gradient, times(2)).setLocalMatrix(eq(matrix));
    }

    @Test
    public void testParseRadialGradient() throws SAXException {
        //given
        when(picture.beginRecording(anyInt(), anyInt())).thenReturn(canvas);
        SVGParser.SVGHandler parserHandler = spy(this.parserHandler);
        RadialGradient gradient = mock(RadialGradient.class);
        doReturn(gradient).when(parserHandler).createRadialGradient(anyInt(), anyFloat(), anyFloat(), any(int[].class), any(float[].class), any(Shader.TileMode.class));

        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("id", "gr1"), attr("cx", "10.1"), attr("cy", "4.1"), attr("r", "5.0")), "radialGradient");
        startElement(parserHandler, attributes(attr("offset", "10.1"), attr("style", "stop-color:ff0000")), "stop");
        endElement(parserHandler, "stop");
        endElement(parserHandler, "radialGradient");
        endSVG(parserHandler);

        //then
        verify(parserHandler).createRadialGradient(eq(10.1f), eq(4.1f), eq(5.0f), eq(new int[]{-65536}), eq(new float[]{10.1f}), eq(Shader.TileMode.CLAMP));
    }

    @Test
    public void testParseRadialGradient_xlink() throws SAXException {
        //given
        when(picture.beginRecording(anyInt(), anyInt())).thenReturn(canvas);
        SVGParser.SVGHandler parserHandler = spy(this.parserHandler);
        RadialGradient gradient = mock(RadialGradient.class);
        doReturn(gradient).when(parserHandler).createRadialGradient(anyInt(), anyFloat(), anyFloat(), any(int[].class), any(float[].class), any(Shader.TileMode.class));
        Matrix matrix = mock(Matrix.class);
        Matrix subMatrix = mock(Matrix.class);
        doReturn(matrix).when(parserHandler).createMatrix();
        doReturn(subMatrix).when(parserHandler).createMatrix(eq(matrix));

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
        verify(parserHandler).createRadialGradient(eq(10.1f), eq(4.1f), eq(5.0f), eq(new int[0]), eq(new float[0]), eq(Shader.TileMode.CLAMP));
        verify(parserHandler).createRadialGradient(eq(5.0f), eq(5.0f), eq(2.0f), eq(new int[0]), eq(new float[0]), eq(Shader.TileMode.CLAMP));
        verify(gradient, times(1)).setLocalMatrix(eq(matrix));
        verify(gradient, times(1)).setLocalMatrix(eq(subMatrix));
        verify(matrix).setValues(eq(new float[]{0.2883f, 0.0f, 153.3307f, 0.0f, 0.2865f, 265.0264f, 0.0f, 0.0f, 1.0f}));
    }

    private void testGradientTransform(String val, Matrix matrix) throws SAXException {
        //given
        when(picture.beginRecording(anyInt(), anyInt())).thenReturn(canvas);
        SVGParser.SVGHandler parserHandler = spy(this.parserHandler);
        doReturn(matrix).when(parserHandler).createMatrix();
        RadialGradient radialGradient = mock(RadialGradient.class);
        doReturn(radialGradient).when(parserHandler).createRadialGradient(eq(10.0f), eq(10.0f), eq(5.0f), any(int[].class), any(float[].class), eq(Shader.TileMode.CLAMP));

        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("id", "gr1"), attr("cx", "10.0"), attr("cy", "10.0"), attr("r", "5.0"), attr("gradientTransform", val)), "radialGradient");
        endElement(parserHandler, "radialGradient");
        endSVG(parserHandler);
    }

    @Test
    public void testGradientTransform_Matrix() throws SAXException {
        Matrix matrix = mock(Matrix.class);
        testGradientTransform("matrix(0.2883 0 0 0.2865 153.3307 265.0264)", matrix);

        verify(matrix).setValues(eq(new float[]{0.2883f, 0.0f, 153.3307f, 0.0f, 0.2865f, 265.0264f, 0.0f, 0.0f, 1.0f}));
    }

    @Test
    public void testGradientTransform_Translate() throws SAXException {
        Matrix matrix = mock(Matrix.class);
        testGradientTransform("translate(0,-924.36218)", matrix);

        verify(matrix).postTranslate(eq(0.0f), eq(-924.36218f));
    }

    @Test
    public void testGradientTransform_Scale() throws SAXException {
        Matrix matrix = mock(Matrix.class);
        testGradientTransform("scale(100.2,120.34)", matrix);

        verify(matrix).postScale(eq(100.2f), eq(120.34f));
    }

    @Test
    public void testGradientTransform_SkewX() throws SAXException {
        Matrix matrix = mock(Matrix.class);
        testGradientTransform("skewX(240.23)", matrix);

        verify(matrix).postSkew(eq((float) Math.tan(240.23f)), eq(0.0f));
    }

    @Test
    public void testGradientTransform_SkewY() throws SAXException {
        Matrix matrix = mock(Matrix.class);
        testGradientTransform("skewY(240.23)", matrix);

        verify(matrix).postSkew(eq(0.0f), eq((float) Math.tan(240.23f)));
    }

    @Test
    public void testGradientTransform_Rotate() throws SAXException {
        Matrix matrix = mock(Matrix.class);
        testGradientTransform("rotate(120.2, 240.23, 123.11)", matrix);

        verify(matrix).postTranslate(eq(240.23f), eq(123.11f));
        verify(matrix).postRotate(eq(120.2f));
        verify(matrix).postTranslate(eq(-240.23f), eq(-123.11f));
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

        verify(paint).setStyle(eq(Paint.Style.FILL));
        verify(paint).setColor(eq(0xFFFFFFFF));
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
        verify(paint).setShader(eq(shader));
        verify(paint).setStyle(eq(Paint.Style.FILL));
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
        verify(paint).setStyle(eq(Paint.Style.FILL));
        Integer hexColor = properties.getHex("fill");
        verify(paint).setColor(eq((0xFFFFFF & hexColor) | 0xFF000000));
        assertThat(res, is(true));
    }

    @Test
    public void testDoFill_default() {
        boolean res = parserHandler.doFill(
                new SVGParser.Properties(attributes()),
                new HashMap<String, Shader>());

        verify(paint).setStyle(eq(Paint.Style.FILL));
        verify(paint).setColor(eq(0xFF000000));
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
        verify(paint).setStyle(eq(Paint.Style.STROKE));
    }


    private void testDoStroke_strokeLinecap(String val) {
        assertThat(parserHandler.doStroke(new SVGParser.Properties(attributes(attr("stroke", "#ff0000"), attr("stroke-linecap", val)))), is(true));
        verify(paint).setStyle(eq(Paint.Style.STROKE));
    }

    @Test
    public void testDoStroke_strokeLinecapRound() {
        testDoStroke_strokeLinecap("round");
        verify(paint).setStrokeCap(eq(Paint.Cap.ROUND));
    }

    @Test
    public void testDoStroke_strokeLinecapSquare() {
        testDoStroke_strokeLinecap("square");
        verify(paint).setStrokeCap(eq(Paint.Cap.SQUARE));
    }

    @Test
    public void testDoStroke_strokeLinecapButt() {
        testDoStroke_strokeLinecap("butt");
        verify(paint).setStrokeCap(eq(Paint.Cap.BUTT));
    }

    private void testDoStroke_linejoin(String val) {
        assertThat(parserHandler.doStroke(new SVGParser.Properties(attributes(attr("stroke", "#ff0000"), attr("stroke-linejoin", val)))), is(true));
        verify(paint).setStyle(eq(Paint.Style.STROKE));
    }

    @Test
    public void testDoStroke_linejoinMiter() {
        testDoStroke_linejoin("miter");
        verify(paint).setStrokeJoin(eq(Paint.Join.MITER));
    }

    @Test
    public void testDoStroke_linejoinRound() {
        testDoStroke_linejoin("round");
        verify(paint).setStrokeJoin(eq(Paint.Join.ROUND));
    }

    @Test
    public void testDoStroke_linejoinBevel() {
        testDoStroke_linejoin("bevel");
        verify(paint).setStrokeJoin(any(Paint.Join.class));
    }


}