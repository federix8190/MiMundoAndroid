package py.com.personal.mimundo.disenhos;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

import py.com.personal.mimundo.activities.R;

/**
 * Created by Usuario on 14-Nov-14.
 */
public class PersistentFlatButton extends Button {
    private Paint linePaint;

    public PersistentFlatButton(Context context) {
        super(context);
        init();
    }

    public PersistentFlatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PersistentFlatButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        Resources myResources = getResources();
        // Se elige el flag del Paint.
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(myResources.getColor(R.color.formulario_en_tarjeta));
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
