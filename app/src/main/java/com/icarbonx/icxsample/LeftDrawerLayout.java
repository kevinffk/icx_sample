package com.icarbonx.icxsample;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/2/12
 * Description:
 */
public class LeftDrawerLayout extends ViewGroup {
    private static final int MIN_DRAWER_MARGIN = 64; // dp
    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400; // dips per second


    private ViewDragHelper mHelper;
    /**
     * drawer显示出来的占自身的百分比
     */
    private float mRightDragOffset = 0f;

    private float mTab1DragOffset = 0f;

    /**
     *  Data.
     */
    private int mViewWidth = 0;

    private int mTab1ViewTop = 0;


    /**
     * View.
     */
    private View mRightView;
    private View mLeftView;
    private View mTab1View;


    public LeftDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setup drawer's minMargin
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;

        mHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (mRightView == child) {
                    //todo right
                    int newLeft = Math.max(mViewWidth - child.getWidth(), Math.min(left, mViewWidth));
                    return newLeft;
                } else if (mTab1View == child) { //tabview
                    int newLeft = Math.max(-child.getWidth(), Math.min(left, mViewWidth - child.getWidth()));
                    return newLeft;
                }
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (mTab1View == child) { //tabview
                    return mTab1ViewTop;
                }
                return 0;
            }

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mRightView || child == mTab1View;
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mHelper.captureChildView(mRightView, pointerId);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (mRightView == releasedChild) {
                    final int childWidth = releasedChild.getWidth();
                    //todo right
                    float offset = (mViewWidth - releasedChild.getLeft()) * 1.0f / childWidth;
                    mHelper.settleCapturedViewAt(xvel < 0 || xvel == 0 && offset > 0.5f ? (mViewWidth - childWidth) : mViewWidth, releasedChild.getTop());
                    invalidate();
                } else if (mTab1View == releasedChild) {

                }

            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {  //设置隐藏显示
                if (changedView == mRightView) {
                    final int childWidth = changedView.getWidth();
                    //todo right
                    float offset = (float) (mViewWidth - left) / childWidth;

                    mRightDragOffset = offset;
                    //offset can callback here
                    changedView.setVisibility(offset == 0 ? View.INVISIBLE : View.VISIBLE);
                    invalidate();
                } else if (changedView == mTab1View) {

                }

            }

            @Override
            public int getViewHorizontalDragRange(View child) { //click事件防冲突
                if (child == mRightView) {
                    return child.getWidth();
                } else if (child == mTab1View) {
                    return mViewWidth;
                }
                return 0;
            }
        });
        //设置edge_left track
        //todo right
//        mHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT);
        //设置minVelocity
        mHelper.setMinVelocity(minVel);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        mViewWidth = widthSize;
        setMeasuredDimension(widthSize, heightSize);
        //left view
        View leftView = getChildAt(0);
        MarginLayoutParams lp = (MarginLayoutParams) leftView.getLayoutParams();
        final int contentWidthSpec = MeasureSpec.makeMeasureSpec(widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
        final int contentHeightSpec = MeasureSpec.makeMeasureSpec(heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
        leftView.measure(contentWidthSpec, contentHeightSpec);

        //right view
        View rightView = getChildAt(1);
        lp = (MarginLayoutParams) rightView.getLayoutParams();
        final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec, +lp.leftMargin + lp.rightMargin, lp.width);
        final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin, lp.height);
        rightView.measure(drawerWidthSpec, drawerHeightSpec);

        //tab 1 view
        View tab1View = getChildAt(2);
        lp = (MarginLayoutParams) tab1View.getLayoutParams();
        final int tab1WidthSpec = getChildMeasureSpec(widthMeasureSpec, +lp.leftMargin + lp.rightMargin, lp.width);
        final int tab1HeightSpec = getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin, lp.height);
        tab1View.measure(tab1WidthSpec, tab1HeightSpec);
        mTab1ViewTop = lp.topMargin;

        mLeftView = leftView;
        mRightView = rightView;
        mTab1View = tab1View;


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View leftView = mLeftView;
        View rightView = mRightView;
        View tab1View  = mTab1View;

        //left view
        MarginLayoutParams lp = (MarginLayoutParams) leftView.getLayoutParams();
        leftView.layout(lp.leftMargin, lp.topMargin, lp.leftMargin + leftView.getMeasuredWidth(), lp.topMargin + leftView.getMeasuredHeight());

        //right view
        lp = (MarginLayoutParams) rightView.getLayoutParams();
        final int rightViewWidth = rightView.getMeasuredWidth();
        int rightViewLeft = mViewWidth - (int) (rightViewWidth * mRightDragOffset);
        rightView.layout(rightViewLeft, lp.topMargin, rightViewLeft + rightViewWidth, lp.topMargin + rightView.getMeasuredHeight());

        //tab 1
        lp = (MarginLayoutParams) tab1View.getLayoutParams();
        final int tab1ViewWidth = tab1View.getMeasuredWidth();
        int tab1ViewLeft = mViewWidth - tab1ViewWidth - (int)(mViewWidth * mTab1DragOffset);
        tab1View.layout(tab1ViewLeft, lp.topMargin, tab1ViewLeft + tab1ViewWidth, lp.topMargin + tab1View.getMeasuredHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean shouldInterceptTouchEvent = mHelper.shouldInterceptTouchEvent(ev);
        return shouldInterceptTouchEvent;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return true;
    }


    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            invalidate();
        }
    }

    public void closeDrawer() {
        View menuView = mRightView;
        mRightDragOffset = 0.f;
        mHelper.smoothSlideViewTo(menuView, -menuView.getWidth(), menuView.getTop());
    }

    public void openDrawer() {
        View menuView = mRightView;
        mRightDragOffset = 1.0f;
        mHelper.smoothSlideViewTo(menuView, 0, menuView.getTop());
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

}
