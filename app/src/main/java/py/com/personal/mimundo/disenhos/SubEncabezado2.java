package py.com.personal.mimundo.disenhos;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import py.com.personal.mimundo.activities.R;

/**
 * Created by carlos on 30/10/14.
 */
public class SubEncabezado2 extends androidx.appcompat.widget.AppCompatTextView {

    private Paint linePaint;

    public SubEncabezado2(Context context, AttributeSet ats, int ds) {
        super(context, ats, ds);
        init();
    }
    public SubEncabezado2(Context context) {
        super(context);
        init();
    }
    public SubEncabezado2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Resources myResources = getResources();
        // Se elige el flag del Paint.
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(myResources.getColor(R.color.linea_divisoria));
//        linePaint.setStrokeWidth(getContext().getResources()
//                .getDimensionPixelSize(R.dimen.margin_normal));
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Se dibujan las lineas consecutivas.
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), linePaint);
        canvas.save();
        super.onDraw(canvas);
        canvas.restore();
    }
}
