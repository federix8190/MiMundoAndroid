package py.com.personal.mimundo.disenhos;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import py.com.personal.mimundo.activities.R;

/**
 * Created by Usuario on 20-Nov-14.
 */
public class RelativeLayout4 extends RelativeLayout {

    private Paint linePaint;

    public RelativeLayout4(Context context) {
        super(context);
        init();
    }

    public RelativeLayout4(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RelativeLayout4(Context context, AttributeSet attrs, int defStyle) {
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
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), linePaint);
        canvas.drawLine(0, 0, getWidth(), 0, linePaint);
        canvas.drawLine(0, 0, 0, getHeight(), linePaint);
        canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), linePaint);
        canvas.save();
        super.onDraw(canvas);
        canvas.restore();
    }
}

