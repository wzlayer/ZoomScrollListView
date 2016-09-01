package com.wzonelayer.zoomlistview;

import android.content.Context;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * <p>滑动缩放ListView<p/>
 * @author zhulingjun
 */
public class ZoomScrollListView extends ListView implements AbsListView.OnScrollListener {
    /**
     * ListView head
     */
    private View mHeadLayout;
    private View mHeadView;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    private float mTranslationY; // 竖向移动距离
    private float mScaleY; // 竖向放缩比例
    private RectF mRect1 = new RectF();
    private RectF mRect2 = new RectF();
    private AccelerateDecelerateInterpolator mSmoothInterpolator = new AccelerateDecelerateInterpolator();

    public ZoomScrollListView(Context context) {
        super(context);
    }

    public ZoomScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoomScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @return
     */
    boolean haveHeadView(){
        return mHeadLayout != null ? true : false;
    }

    /**
     * <p>需要在setAdapter()之前调用<p/>
     * @param layoutView
     * @param headView
     */
    public void setHeadViewBeforeSetAdapter(View layoutView, View headView){
        if(layoutView == null || headView == null){
            return;
        }

        mHeadLayout = layoutView;
        mHeadView = headView;
        mHeaderHeight = mHeadLayout.getLayoutParams().height;
        mMinHeaderTranslation = -mHeaderHeight;
        addHeaderView(mHeadLayout);
        setOnScrollListener(this);
    }

    @Override
    @Deprecated
    public void addHeaderView(View v) {
        super.addHeaderView(v);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!haveHeadView()){
            return super.onTouchEvent(ev);
        }

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if(mHeadView.isShown()){
                if(mScaleY > 0 && mScaleY < 0.5){
                    if(Build.VERSION.SDK_INT >= 11){
                        smoothScrollToPositionFromTop(1, -1, 1000);
                    }else{
                        setSelection(1);
                    }
                }else{
                    if(Build.VERSION.SDK_INT >= 11){
                        smoothScrollToPositionFromTop(0, -1, 1000);
                    }else{
                        setSelection(0);
                    }
                }
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(!haveHeadView()){
            return;
        }

        int scrollY = getScrollYY();
        mTranslationY = Math.max(-scrollY, mMinHeaderTranslation);
        float ratio = Math.max(Math.min(mTranslationY / mMinHeaderTranslation, 1.0f), 0.0f);
        interpolate(mHeadView, mHeadLayout, mSmoothInterpolator.getInterpolation(ratio));
    }

    public int getScrollYY() {
        View c = getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mHeadLayout.getHeight();
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    private void interpolate(View view, View layoutView, float interpolation) {
        mRect1.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        mRect2.set(0, 0, 0, 0);


        float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
        float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
        float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
        float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

//        view1.setTranslationX(translationX);
        view.setTranslationY(translationY - mTranslationY);
        view.setScaleX(scaleX);
        view.setScaleY(scaleY);
        mScaleY = scaleY;
    }
}
