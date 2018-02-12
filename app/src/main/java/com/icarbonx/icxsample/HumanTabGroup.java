package com.icarbonx.icxsample;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/2/12
 * Description:
 */
public class HumanTabGroup extends ViewGroup {


    /**
     * child view
     */
    private View mContentView1;

    private View mTabView1;

    private View mContentView2;

    private View mTabView2;


    private ViewDragHelper mViewDragHelper;

    public HumanTabGroup(Context context) {
        super(context);
        initView();
    }

    public HumanTabGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HumanTabGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return false;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return super.clampViewPositionHorizontal(child, left, dx);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return super.clampViewPositionVertical(child, top, dy);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //排列，只有两个容器, 只有两个tab, 固共4个子的

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);

        //measure left view.
        View leftContView = getChildAt(0);

        MarginLayoutParams lp = (MarginLayoutParams) leftContView.getLayoutParams();
        final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec, lp.leftMargin + lp.rightMargin, lp.width);
        final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,lp.topMargin + lp.bottomMargin, lp.height);
        leftContView.measure(drawerWidthSpec, drawerHeightSpec);

        //measure right view.
        View rightContView = getChildAt(2);
        lp = (MarginLayoutParams) rightContView.getLayoutParams();
        final int contentWidthSpec = MeasureSpec.makeMeasureSpec(widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
        final int contentHeightSpec = MeasureSpec.makeMeasureSpec(heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
        rightContView.measure(contentWidthSpec, contentHeightSpec);

        mContentView1 = leftContView;
        mContentView2 = rightContView;

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
