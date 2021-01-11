package py.com.personal.mimundo.disenhos;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * Basado en la implementacion de IOsched:
 * A custom ScrollView that can accept a scroll listener.
 */
public class ObservableScrollView extends ScrollView {


    private ArrayList<Callbacks> mCallbacks = new ArrayList<Callbacks>();
    private boolean primerTopDelScroll = true;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addCallbacks(Callbacks listener) {
        if (!mCallbacks.contains(listener)) {
            mCallbacks.add(listener);
        }
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        for (Callbacks c : mCallbacks) {
            c.onScrollChanged(x - oldx, y - oldy);
        }
    }

    public boolean isPrimerTopDelScroll() {
        return primerTopDelScroll;
    }

    public void setPrimerTopDelScroll(boolean primerTopDelScroll) {
        this.primerTopDelScroll = primerTopDelScroll;
    }

    public interface Callbacks {

        public void onScrollChanged(int deltaX, int deltaY);
    }
}
