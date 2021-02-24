package components;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
public final class sample extends View{
    Paint mPaint;
    Rect mRect;
    private int mTouchSlop;
    private int mTouchMode;
    int mScreenHeight;
    int mScreenWidth;
    int prevX;
    int prevY;
    static final int TOUCH_MODE_TAP = 1;
    static final int TOUCH_MODE_DOWN = 2;
    final int mImageWidth = 100;
    final int mImageHeight = 100;
    Rect mImagePosition;
    Region mImageRegion;
    boolean canImageMove;

    public sample(Context context)
    {
        super(context);
        mPaint = new Paint();
        mPaint.setTextSize(25);
        mPaint.setColor(0xFF0000FF);
        //Size for image
        mImagePosition = new Rect(10,10,mImageWidth,mImageHeight);
        mImageRegion = new Region();
        mImageRegion.set(mImagePosition);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mScreenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        mScreenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        canImageMove = false;
    }


    public boolean onTouchEvent(MotionEvent event)
    {
        int positionX = (int)event.getRawX();
        int positionY = (int)event.getRawY();

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN: {
                mTouchMode = TOUCH_MODE_DOWN;

                if(mImageRegion.contains(positionX, positionY))
                {
                    prevX = positionX;
                    prevY = positionY;
                    canImageMove = true;
                }
            }
            break;

            case MotionEvent.ACTION_MOVE:
            {
                if(canImageMove == true)
                {
                    // Check if we have moved far enough that it looks more like a
                    // scroll than a tap
                    final int distY = Math.abs(positionY - prevY);
                    final int distX = Math.abs(positionX - prevX);

                    if (distX > mTouchSlop || distY > mTouchSlop)
                    {
                        int deltaX =  positionX-prevX ;
                        int deltaY =  positionY-prevY;
                        // Check if delta is added, is the rectangle is within the visible screen
                        if((mImagePosition.left+ deltaX) > 0 && ((mImagePosition.right +deltaX) < mScreenWidth )  && (mImagePosition.top +deltaY) >0 && ((mImagePosition.bottom+deltaY) < mScreenHeight))
                        {
                            // invalidate current position as we are moving...
                            mImagePosition.left = mImagePosition.left + deltaX;
                            mImagePosition.top = mImagePosition.top + deltaY;
                            mImagePosition.right = mImagePosition.left + mImageWidth;
                            mImagePosition.bottom = mImagePosition.top + mImageHeight;
                            mImageRegion.set(mImagePosition);
                            prevX = positionX;
                            prevY = positionY;

                            invalidate();
                        }
                    }
                }
            }
            break;
            case MotionEvent.ACTION_UP:
                canImageMove = false;
                break;
        }
        return true;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        // make the entire canvas white
        paint.setColor(Color.CYAN);
        Rect rect = new Rect(0,0,this.getWidth(),this.getHeight());
        canvas.drawRect(mImagePosition, paint);
        //canvas.drawBitmap(bitmap, null,mImagePosition, null);
    }


}