package com.meg7.widget.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by Mostafa Gazar on 11/2/13.
 */
public abstract class BaseImageView extends ImageView {
    private static final String TAG = BaseImageView.class.getSimpleName();

    protected Context mContext;

    private BitmapShader mBitmapShader;
    private Bitmap mBitmap;
    private Paint mPaint;
    private WeakReference<Bitmap> mWeakBitmap;

    public BaseImageView(Context context) {
        super(context);
        sharedConstructor(context);
    }

    public BaseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructor(context);
    }

    public BaseImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        sharedConstructor(context);
    }

    private void sharedConstructor(Context context) {
        mContext = context;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public void invalidate() {
        mWeakBitmap = null;
        super.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInEditMode()) {
            int i = canvas.saveLayer(0.0F, 0.0F, getWidth(), getHeight(),
                    null, Canvas.ALL_SAVE_FLAG);
            try {
                Bitmap bitmap = mWeakBitmap != null ? mWeakBitmap.get() : null;
                // Bitmap not loaded.
                if (bitmap == null || bitmap.isRecycled()) {
                    Drawable drawable = getDrawable();
                    if (drawable != null) {
                        bitmap = Bitmap.createBitmap(getWidth(),
                                getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas bitmapCanvas = new Canvas(bitmap);
                        drawable.setBounds(0, 0, getWidth(), getHeight());
                        drawable.draw(bitmapCanvas);

                        mBitmap = getBitmap();

                        // Draw Bitmap.
                        mPaint.reset();
                        mBitmapShader = new BitmapShader(bitmap,
                                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                        mPaint.setShader(mBitmapShader);
                        canvas.drawBitmap(mBitmap, 0, 0, mPaint);

                        mWeakBitmap = new WeakReference<Bitmap>(bitmap);
                    }
                }

                // Bitmap already loaded.
                if (bitmap != null) {
                    mPaint.setShader(null);
                    canvas.drawBitmap(bitmap, 0, 0, mPaint);
                    return;
                }
            } catch (Exception e) {
                System.gc();

                Log.e(TAG, String.format("Failed to draw, Id :: %s. Error occurred :: %s", getId(), e.toString()));
            } finally {
                canvas.restoreToCount(i);
            }
        } else {
            super.onDraw(canvas);
        }
    }

    public abstract Bitmap getBitmap();

}
