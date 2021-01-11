//package py.com.personal.mimundo.disenhos;
//
//import android.graphics.Point;
//import android.support.v4.view.ViewCompat;
//import android.support.v4.view.ViewPropertyAnimatorCompat;
//import android.support.v7.widget.RecyclerView;
//import android.view.animation.AccelerateInterpolator;
//import android.view.animation.OvershootInterpolator;
//
///**
// * Created by carlos on 24/11/14.
// */
//public class FromTopItemAnimator extends PendingItemAnimator {
//
//    public FromTopItemAnimator() {
//        setMoveDuration(200);
//        setRemoveDuration(500);
//        setAddDuration(300);
//    }
//
//    public boolean animateChange(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, int i, int i2, int i3, int i4) {
//        return false;
//    }
//    @Override
//    protected boolean prepHolderForAnimateRemove(RecyclerView.ViewHolder holder) {
//        return true;
//    }
//
//    @Override
//    protected ViewPropertyAnimatorCompat animateRemoveImpl(RecyclerView.ViewHolder holder) {
//        Point screen = DisplayUtils.getScreenDimensions(holder.itemView.getContext());
//        int top = holder.itemView.getTop();
//        return ViewCompat.animate(holder.itemView)
//                .rotation(80)
//                .translationY(screen.y - top)
//                .setInterpolator(new AccelerateInterpolator());
//    }
//
//    @Override
//    protected void onRemoveCanceled(RecyclerView.ViewHolder holder) {
//        ViewCompat.setTranslationY(holder.itemView, 0);
//    }
//
//    @Override
//    protected boolean prepHolderForAnimateAdd(RecyclerView.ViewHolder holder) {
//        int bottom = holder.itemView.getBottom();
//        ViewCompat.setTranslationY(holder.itemView, - bottom);
//        return true;
//    }
//
//    @Override
//    protected ViewPropertyAnimatorCompat animateAddImpl(RecyclerView.ViewHolder holder) {
//        return ViewCompat.animate(holder.itemView)
//                .translationY(0)
//                .setInterpolator(new OvershootInterpolator());
//    }
//
//    @Override
//    protected void onAddCanceled(RecyclerView.ViewHolder holder) {
//        ViewCompat.setTranslationY(holder.itemView, 0);
//    }
//}
