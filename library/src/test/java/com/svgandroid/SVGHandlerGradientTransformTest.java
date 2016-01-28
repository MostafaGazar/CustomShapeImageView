package com.svgandroid;

import android.graphics.Matrix;
import android.graphics.RadialGradient;
import android.graphics.Shader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Vlad Medvedev on 28.01.2016.
 * vladislav.medvedev@devfactory.com
 */
@RunWith(PowerMockRunner.class)
public class SVGHandlerGradientTransformTest extends SVGHandlerTestSupport {
    private void testGradientTransform(String val, Matrix matrix) throws Exception {
        //given
        when(picture.beginRecording(anyInt(), anyInt())).thenReturn(canvas);
        SVGParser.SVGHandler parserHandler = spy(this.parserHandler);
        PowerMockito.whenNew(Matrix.class).withArguments(matrix).thenReturn(matrix);
        RadialGradient radialGradient = mock(RadialGradient.class);
        PowerMockito.whenNew(RadialGradient.class).withArguments(
                eq(10.0f), eq(10.0f), eq(5.0f), any(int[].class), any(float[].class), eq(Shader.TileMode.CLAMP)
        ).thenReturn(radialGradient);

        //when
        startSVG(parserHandler);
        startElement(parserHandler, attributes(attr("id", "gr1"), attr("cx", "10.0"), attr("cy", "10.0"), attr("r", "5.0"), attr("gradientTransform", val)), "radialGradient");
        endElement(parserHandler, "radialGradient");
        endSVG(parserHandler);
    }

    @Test
    public void testGradientTransform_Matrix() throws Exception {
        testGradientTransform("matrix(0.2883 0 0 0.2865 153.3307 265.0264)", matrix);
        verify(matrix).setValues(new float[]{0.2883f, 0.0f, 153.3307f, 0.0f, 0.2865f, 265.0264f, 0.0f, 0.0f, 1.0f});
    }

    @Test
    public void testGradientTransform_Translate() throws Exception {
        testGradientTransform("translate(0,-924.36218)", matrix);
        verify(matrix).postTranslate(0.0f, -924.36218f);
    }

    @Test
    public void testGradientTransform_Scale() throws Exception {
        testGradientTransform("scale(100.2,120.34)", matrix);
        verify(matrix).postScale(100.2f, 120.34f);
    }

    @Test
    public void testGradientTransform_SkewX() throws Exception {
        testGradientTransform("skewX(240.23)", matrix);
        verify(matrix).postSkew((float) Math.tan(240.23f), 0.0f);
    }

    @Test
    public void testGradientTransform_SkewY() throws Exception {
        testGradientTransform("skewY(240.23)", matrix);
        verify(matrix).postSkew(0.0f, (float) Math.tan(240.23f));
    }

    @Test
    public void testGradientTransform_Rotate() throws Exception {
        testGradientTransform("rotate(120.2, 240.23, 123.11)", matrix);
        verify(matrix).postTranslate(240.23f, 123.11f);
        verify(matrix).postRotate(120.2f);
        verify(matrix).postTranslate(-240.23f, -123.11f);
    }
}
