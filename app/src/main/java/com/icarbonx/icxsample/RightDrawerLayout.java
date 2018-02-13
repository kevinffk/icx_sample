package com.icarbonx.icxsample;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/2/13
 * Description:
 */
public class RightDrawerLayout extends ViewGroup {

    private static final int MIN_FLING_VELOCITY = 400; // dips per second


    private ViewDragHelper mViewDragHelper;

    private int mViewWidth = 0;

    private float mTab1DragOffset = 0f;

    /**
     * View.
     */
    private View mLeftView;
    private View mRightView;

    private View mDragView;
    private View mTab1View;
    private View mTab2view;

    private Animation rightInAnim;
    private Animation leftInAnim;

    public RightDrawerLayout(Context context) {
        super(context);
        initView();
    }

    public RightDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RightDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;


        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mDragView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (child == mDragView) {
                    return left;
                }
                return 0;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (child == mTab1View) {
                    return top;
                }
                return 0;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if (changedView == mDragView) {
                    if (left == - mTab1View.getWidth()) {
                        if (mTab1View.getVisibility() != View.INVISIBLE) {
                            mTab1View.setVisibility(View.INVISIBLE);
                            mTab2view.setVisibility(View.VISIBLE);

                            mTab2view.startAnimation(leftInAnim);
                        }

                    } else if (left == mViewWidth - mTab1View.getWidth()) {

                        if (mTab2view.getVisibility() != View.INVISIBLE) {
                            mTab1View.setVisibility(View.VISIBLE);
                            mTab2view.setVisibility(View.INVISIBLE);

                            mTab1View.startAnimation(rightInAnim);
                        }
                    }
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == mDragView) {
                    int tab1Width = mTab1View.getWidth();
                    float offset = (mViewWidth - releasedChild.getLeft() - tab1Width) * 1.0f / mViewWidth;

                    if (xvel < 0 || xvel == 0 && offset > 0.5f) {
                        mViewDragHelper.settleCapturedViewAt(-tab1Width, releasedChild.getTop());
                    } else {
                        mViewDragHelper.settleCapturedViewAt((mViewWidth - tab1Width), releasedChild.getTop());
                    }
                    invalidate();
                }
            }
        });
        mViewDragHelper.setMinVelocity(minVel);

        leftInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.left_in);
        rightInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.right_in);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View leftView = mLeftView;
        View dragView = mDragView;
        View rightView = mRightView;
        View tab1View = mTab1View;

        //left view
        MarginLayoutParams lp = (MarginLayoutParams) leftView.getLayoutParams();
        leftView.layout(lp.leftMargin, lp.topMargin, lp.leftMargin + leftView.getMeasuredWidth(), lp.topMargin + leftView.getMeasuredHeight());

        //drag view
        lp = (MarginLayoutParams) dragView.getLayoutParams();
        final int tab1ViewWidth = tab1View.getMeasuredWidth();
        int dragViewLeft = mViewWidth - tab1ViewWidth - (int) (mViewWidth * mTab1DragOffset);
        dragView.layout(lp.leftMargin + dragViewLeft, lp.topMargin, lp.leftMargin + dragViewLeft + dragView.getMeasuredWidth(), lp.topMargin + dragView.getMeasuredHeight());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        mViewWidth = widthSize;
        setMeasuredDimension(widthSize, heightSize);

        //left view
        View leftView = getChildAt(0);
        MarginLayoutParams lp = (MarginLayoutParams) leftView.getLayoutParams();
        final int leftViewWidthSpec = MeasureSpec.makeMeasureSpec(widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
        final int contentHeightSpec = MeasureSpec.makeMeasureSpec(heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
        leftView.measure(leftViewWidthSpec, contentHeightSpec);

        //drag view
        ViewGroup dragView = (ViewGroup) getChildAt(1);
        View tab1View = dragView.getChildAt(0);
        View tab2View  = dragView.getChildAt(2);
        View rightView = dragView.getChildAt(1);

        lp = (MarginLayoutParams) dragView.getLayoutParams();

        final int drawViewWidthSpec = MeasureSpec.makeMeasureSpec(widthSize + tab1View.getLayoutParams().width - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
        final int drawViewHeightSpec = MeasureSpec.makeMeasureSpec(heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
        dragView.measure(drawViewWidthSpec, drawViewHeightSpec);

        mLeftView = leftView;
        mDragView = dragView;
        mRightView = rightView;
        mTab1View = tab1View;
        mTab2view = tab2View;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean shouldInterceptTouchEvent = mViewDragHelper.shouldInterceptTouchEvent(ev);
        return shouldInterceptTouchEvent;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }


    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }


    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}
