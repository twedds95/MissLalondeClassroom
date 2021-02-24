package components;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.Random;

public class StudentComponent extends View {
    private String name;
    private Region mNameRegion;
    boolean canImageMove;
    private Rect mNamePosition;
    private int mTouchSlop;

    private final int w = 50;
    private final int h = 20;
    private int x, y;
    private final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight;


    public StudentComponent(Context context, String name) {
        super(context);

        //        setScreenHeight including status bar
        int statusBarHeight = 0;
        int resource = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resource > 0) {
            statusBarHeight = 3 * context.getResources().getDimensionPixelSize(resource);
        }
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels - statusBarHeight;

        Random random = new Random();
        this.name = name;
        x = random.nextInt(screenWidth - w) + w;
        y = random.nextInt(screenHeight - w) + w;

        mNamePosition = new Rect(x - w, y - h, x + w, y + h);
        mNameRegion = new Region();
        mNameRegion.set(mNamePosition);
        canImageMove = false;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(
                new RectF(mNamePosition.left, mNamePosition.top,
                        mNamePosition.right, mNamePosition.bottom),
                50, 50, paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(18);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawText(name, x, y, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int positionX = (int) event.getX();
        int positionY = (int) event.getY();

        if (mNameRegion.contains(positionX, positionY)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    canImageMove = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (canImageMove) {
                        // Check if we have moved far enough that it looks more like a
                        // scroll than a tap
                        final int distY = Math.abs(positionY - y);
                        final int distX = Math.abs(positionX - x);

                        if (distX > mTouchSlop || distY > mTouchSlop) {
                            int deltaX = positionX - x;
                            int deltaY = positionY - y;
                            // Check if delta is added, is the rectangle is within the visible screen
                            if ((mNamePosition.left + deltaX) > 0 &&
                                    ((mNamePosition.right + deltaX) < screenWidth) &&
                                    (mNamePosition.top + deltaY) > 0 &&
                                    ((mNamePosition.bottom + deltaY) < screenHeight)) {
                                // invalidate current position as we are moving...
                                mNamePosition.left = mNamePosition.left + deltaX;
                                mNamePosition.top = mNamePosition.top + deltaY;
                                mNamePosition.right = mNamePosition.left + 2 * w;
                                mNamePosition.bottom = mNamePosition.top + 2 * h;
                                mNameRegion.set(mNamePosition);
                                x = positionX;
                                y = positionY;
                                invalidate();
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
        return false;
    }

    public void update() {

    }


}
