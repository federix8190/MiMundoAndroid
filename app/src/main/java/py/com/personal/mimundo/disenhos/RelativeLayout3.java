package py.com.personal.mimundo.disenhos;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import py.com.personal.mimundo.activities.R;

/**
 * Created by Usuario on 11/9/2014.
 */
public class RelativeLayout3 extends RelativeLayout {

    private Paint linePaint;

    public RelativeLayout3(Context context) {
        super(context);
        init();
    }

    public RelativeLayout3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RelativeLayout3(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        Resources myResources = getResources();
        // Se elige el flag del Paint.
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(myResources.getColor(R.color.linea_divisoria));
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Se dibujan las lineas consecutivas.
        canvas.drawLine(0, 0, getWidth(), 0, linePaint);
        canvas.save();
        super.onDraw(canvas);
        canvas.restore();
    }
}
