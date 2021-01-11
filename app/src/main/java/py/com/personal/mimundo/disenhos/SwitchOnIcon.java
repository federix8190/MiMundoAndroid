package py.com.personal.mimundo.disenhos;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import py.com.personal.mimundo.activities.R;


public class SwitchOnIcon extends androidx.appcompat.widget.AppCompatImageView {

    private Paint linePaint;

    public SwitchOnIcon(Context context, AttributeSet ats, int ds) {
        super(context, ats, ds);
        init();
    }
    public SwitchOnIcon(Context context) {
        super(context);
        init();
    }
    public SwitchOnIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
    }

    @Override
    public void onDraw(Canvas canvas) {
        this.setColorFilter(R.color.teal800);
        super.onDraw(canvas);
        canvas.restore();
    }
}

