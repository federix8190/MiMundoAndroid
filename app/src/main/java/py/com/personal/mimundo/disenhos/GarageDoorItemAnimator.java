//package py.com.personal.mimundo.disenhos;
//
//import android.support.v4.view.ViewCompat;
//import android.support.v4.view.ViewPropertyAnimatorCompat;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.RecyclerView.ItemAnimator;
//import android.support.v7.widget.RecyclerView.ViewHolder;
//
///**
// * Created by Usuario on 11/9/2014.
// */
//public class GarageDoorItemAnimator extends PendingItemAnimator {
//
//    public GarageDoorItemAnimator() {
//        setAddDuration(300);
//        setRemoveDuration(300);
//    }
//
////    @Override
//    public boolean animateChange(ViewHolder viewHolder, ViewHolder viewHolder2, int i, int i2, int i3, int i4) {
//        return false;
//    }
//
//    @Override
//    protected boolean prepHolderForAnimateRemove(RecyclerView.ViewHolder holder) {
//        return true;
//    }
//
//    @Override
//    protected ViewPropertyAnimatorCompat animateRemoveImpl(RecyclerView.ViewHolder holder) {
//        return ViewCompat.animate(holder.itemView)
//                .rotationX(90)
//                .translationY( - (holder.itemView.getMeasuredHeight() / 2));
//    }
//
//    @Override
//    protected void onRemoveCanceled(RecyclerView.ViewHolder holder) {
//        ViewCompat.setRotationX(holder.itemView, 0);
//        ViewCompat.setTranslationY(holder.itemView, 0);
//    }
//
//    @Override
//    protected boolean prepHolderForAnimateAdd(ViewHolder holder) {
//        ViewCompat.setRotationX(holder.itemView, 90);
//        ViewCompat.setTranslationY(holder.itemView, - (holder.itemView.getMeasuredHeight() / 2));
//        return true;
//    }
//
//    @Override
//    protected ViewPropertyAnimatorCompat animateAddImpl(ViewHolder holder) {
//        return ViewCompat.animate(holder.itemView)
//                .rotationX(0)
//                .translationY(0);
//    }
//
//    @Override
//    protected void onAddCanceled(ViewHolder holder) {
//        ViewCompat.setRotationX(holder.itemView, 0);
//        ViewCompat.setTranslationY(holder.itemView, 0);
//    }
//}
