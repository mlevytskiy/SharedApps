package wumf.com.sharedapps.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maks on 8/5/16.
 * email: m.levytskiy@gmail.com
 *
 * SpannableString spannableString = SpannableString.valueOf(getText());
 * x1=getLayout().getPrimaryHorizontal(spannableString.getSpanStart(span));
 *
 * width = textView.getPaint().measureText(text);
 */
public class RoundedBackgroundSpan extends ReplacementSpan {

    private int color;
    private List<Rect> rects = new ArrayList<>();
    private float roundCorner;

    public RoundedBackgroundSpan(int color, float roundCorner) {
        this.color = color;
        this.roundCorner = roundCorner;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return 0;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

        for (Rect rect : rects) {
            RectF rectF = new RectF(rect.x1, top, rect.x2, bottom);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundCorner, roundCorner, paint);
        }

    }

    public void setColor(int color) {
        this.color = color;
    }

    public void addRect(float x1, float x2) {
        rects.add(new Rect(x1, x2));
    }

    private void drawRandomShapeInsideRect(Canvas canvas, RectF rectF, Paint paint) {
        Path path = new Path();
        PointF point1 = new PointF(rectF.left, rectF.top);
        PointF point2 = new PointF(rectF.left, rectF.right);
        PointF point3 = new PointF(rectF.right, rectF.bottom);
        PointF point4 = new PointF(rectF.left, rectF.bottom);
        path.lineTo((int) point1.x, (int) point1.y);
        path.lineTo((int) point2.x, (int) point2.y);
        path.lineTo((int) point3.x, (int) point3.y);
        path.lineTo((int) point4.x, (int) point4.y);
        canvas.drawPath(path, paint);
    }

    private static class Rect {

        public final float x1;
        public final float x2;

        public Rect(float x1, float x2) {
            this.x1 = x1;
            this.x2 = x2;
        }

    }

}
