package py.com.personal.mimundo.disenhos;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import py.com.personal.mimundo.activities.R;


public class SwicthOn extends androidx.appcompat.widget.AppCompatTextView {

    private Paint linePaint;

    public SwicthOn(Context context, AttributeSet ats, int ds) {
        super(context, ats, ds);
        init();
    }
    public SwicthOn(Context context) {
        super(context);
        init();
    }
    public SwicthOn(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        Resources myResources = getResources();
        // Se elige el flag del Paint.
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(myResources.getColor(R.color.cyan200));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(getContext().getResources()
                .getDimensionPixelSize(R.dimen.margin_normal));
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Se dibujan las lineas consecutivas.
        canvas.drawLine(0, getHeight()/2, getWidth(), getHeight()/2, linePaint);
        canvas.save();
        super.onDraw(canvas);
        canvas.restore();
    }
}

