package components;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class LocationComponent {
    private String title;
    private int x1, x2, y1, y2;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public LocationComponent(String title, int totalSquares, int locationIndex) {
        this.title = title;
        x1 = (locationIndex % 2 == 0 ? 0 : screenWidth / 2);
        x2 = x1 + screenWidth / 2;

        int height = screenHeight * 2 / (totalSquares + 1);
        y1 = (locationIndex / 2) * height;
        y2 = y1 + height;
    }

    public void draw(Canvas canvas) {
//        Borders
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x1, y1, x2, y2, paint);
//        Titles
        paint.setColor(Color.WHITE);
        paint.setTextSize(24);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setTextScaleX((float)1.05);
        canvas.drawText(title, x1 + screenWidth/4, y1 + 30, paint);

    }

    public void update() {
    }


}
