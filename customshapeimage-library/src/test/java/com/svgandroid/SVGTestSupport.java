package com.svgandroid;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.util.Log;

import org.junit.Before;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * Created by Vlad Medvedev on 28.01.2016.
 * vladislav.medvedev@devfactory.com
 */

@PrepareForTest({SVGParser.class, SVGParser.SVGHandler.class, Log.class, Picture.class, Canvas.class, Paint.class, Matrix.class})
public class SVGTestSupport {
    protected Canvas canvas;
    protected Paint paint;
    protected Picture picture;
    protected Matrix matrix;
    protected Path path;

    @Before
    public void setUp() throws Exception {
        canvas = PowerMockito.mock(Canvas.class);
        PowerMockito.whenNew(Canvas.class).withNoArguments().thenReturn(canvas);

        picture = PowerMockito.mock(Picture.class);
        PowerMockito.whenNew(Picture.class).withNoArguments().thenReturn(picture);
        when(picture.beginRecording(anyInt(), anyInt())).thenReturn(canvas);

        paint = PowerMockito.mock(Paint.class);
        PowerMockito.whenNew(Paint.class).withNoArguments().thenReturn(paint);

        PowerMockito.mockStatic(Log.class);

        matrix = PowerMockito.mock(android.graphics.Matrix.class);
        PowerMockito.whenNew(android.graphics.Matrix.class).withNoArguments().thenReturn(matrix);

        path = PowerMockito.mock(Path.class);
        PowerMockito.whenNew(Path.class).withNoArguments().thenReturn(path);
    }
}
