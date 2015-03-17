package com.zc.mask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.mask.maskimage.R;

/**
 * this image .9 mask image
 * 
 * @author zhaocheng
 * 
 */
public class MaskImage extends ImageView {

    private static final String TAG = "MaskImage";
    private Drawable maskDrawble = null;
    private Bitmap maskBitmap = null;
    public Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private PorterDuffXfermode mDuffXfermodeIN;

    public MaskImage(Context context) {
        super(context);
    }

    public MaskImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MaskImage);
        int resourceId = a.getResourceId(R.styleable.MaskImage_mask_image, -1);
        if (resourceId != -1) {
            setMaskImg(resourceId);
        }
        a.recycle();
    }

    public MaskImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setMaskImg(int resId) {
        maskDrawble = getResources().getDrawable(resId);
        if (maskDrawble == null) {
            throw new RuntimeException("resid not find ,this resid is error");
        }
    }

    public void setMaskImg(Bitmap b) {
        if (b == null) {
            throw new RuntimeException("this mask bitmap can't null");
        }
        this.maskBitmap = b;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (null != drawable && drawable instanceof BitmapDrawable) {
            Bitmap orginal = ((BitmapDrawable) (drawable)).getBitmap();
            if (orginal != null) {
                drawMask(canvas, orginal);
            }
        }
    }

    /**
     * draw mask image begin
     * 
     * @param canvas
     * @param orginal
     */
    private void drawMask(Canvas canvas, Bitmap orginal) {
        float width = orginal.getWidth();
        float height = orginal.getHeight();
        int x = 0;
        int y = 0;
        int sc = canvas.saveLayer(x, y, width, height, null,
                Canvas.ALL_SAVE_FLAG);
        drawSource(canvas, orginal);
        canvas.restoreToCount(sc);
    }

    /**
     * this source .9 or bitmap or drawble
     * 
     */
    private void drawSource(Canvas canvas, Bitmap bitmap) {
        if (maskBitmap != null) {
            drawBitmap(canvas);
        } else if (maskDrawble != null) {
            if (maskDrawble instanceof NinePatchDrawable)
                drawNinePath(canvas);
            else
                drawDrawble(canvas);
        } else
            Log.e(TAG, "socurce must be .9 or bitmap or drawble");
        if (mDuffXfermodeIN == null) {
            mDuffXfermodeIN = new PorterDuffXfermode(Mode.SRC_IN);
        }
        paint.setXfermode(mDuffXfermodeIN);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    private void drawBitmap(Canvas canvas) {
        canvas.drawBitmap(maskBitmap, 0, 0, null);
    }

    private void drawDrawble(Canvas canvas) {
        maskDrawble.setBounds(new Rect(0, 0, maskDrawble.getMinimumWidth(),
                maskDrawble.getMinimumWidth()));
        maskDrawble.draw(canvas);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.ImageView#onMeasure(int, int) if mask image is .9
     * width height no change else width height is mask image width height
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (maskDrawble != null) {
            if (maskDrawble instanceof NinePatchDrawable) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }
            this.setMeasuredDimension(maskDrawble.getMinimumWidth(),
                    maskDrawble.getMinimumHeight());
        } else if (maskBitmap != null) {
            this.setMeasuredDimension(maskBitmap.getWidth(),
                    maskBitmap.getHeight());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    /**
     * mask imag is .9
     */
    private void drawNinePath(Canvas canvas) {
        NinePatchDrawable npd = (NinePatchDrawable) maskDrawble;
        npd.setBounds(new Rect(0, 0, getWidth(), getHeight()));
        npd.draw(canvas);
    }

}
