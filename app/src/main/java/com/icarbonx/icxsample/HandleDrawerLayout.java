package com.icarbonx.icxsample;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class HandleDrawerLayout extends ViewGroup {

    //内容view
    private View mContentView;
    //当侧滑菜单展开时，显示在mContentView上面的半透明view
    private View mScrimView;
    //侧滑菜单和手柄组成的布局，因为侧滑菜单和手柄是同时移动的，所以套了一层父布局，方便一起移动
    private ViewGroup mDrawerGroup;
    //mDrawerGroup中的菜单view
    private View mMenuView;
    //mDrawerGroup中的手柄view
    private View mHandleView;

    //强大的帮助类
    private ViewDragHelper mHelper;

    //菜单展开的程度。取值在0到1之间，0表示侧滑菜单隐藏，1表示侧滑菜单完全展开
    private float mOpenPercent = 0;

    private boolean mInLayout = false;
    private boolean mFirstLayout = true;

    public HandleDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                // 限定child横向的坐标范围：
                // 参数left是child想要移动到left位置（仅仅是想移动，但还没移动）
                // 如果不想child在X轴上被移动，返回0
                if(child == mDrawerGroup){
                    // 这里代表-mMenuView.getMeasuredWidth() <= left <= 0
                    // 即child的横向坐标只能在这个范围内
                    return Math.max(-mMenuView.getMeasuredWidth(), Math.min(left, 0));
                }
                return 0;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                // 限定child纵向的坐标范围，此处暂没用到
                return super.clampViewPositionVertical(child, top, dy);
            }

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                // 粗碰child时对child进行尝试捕获，返回true代表该view可以被捕获，false则相反
                // 比如手指按下child这个view时，如果需要交互操作则返回true
                return child == mDrawerGroup;
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                // 边缘检测，可通过下面的mHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT)设置左右上下边缘检测
                // 以下代表屏幕左边缘划进来，强制捕获到mDrawerGroup，相当于人工的tryCaptureView
                mHelper.captureChildView(mDrawerGroup, pointerId);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                // 松开手的时候，释放view，根据当前速度做相应处理
                if(releasedChild == mDrawerGroup){
                    int menuViewWidth = mMenuView.getWidth();
                    float offset = (menuViewWidth + releasedChild.getLeft()) * 1.0f / menuViewWidth;
                    // 设置释放后的view慢慢移动到指定位置
                    mHelper.settleCapturedViewAt(xvel > 0 || xvel == 0 && offset > 0.5f ? 0 : -menuViewWidth, releasedChild.getTop());
                    // 要调用invalidate()才会开始移动
                    invalidate();
                }
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                // 被捕获的view位置改变的回调，left和top为changedView即将移动到的位置
                if(changedView == mDrawerGroup){
                    int menuViewWidth = mMenuView.getWidth();
                    mOpenPercent = (float) (menuViewWidth + left) / menuViewWidth;

                    mScrimView.setAlpha(mOpenPercent);
                    if(floatCompare(mOpenPercent, 0f)){
                        mScrimView.setVisibility(GONE);
                    }else{
                        mScrimView.setVisibility(VISIBLE);
                    }

                    if(mDrawerListener != null){
                        mDrawerListener.onDrawer(mOpenPercent);
                    }
                }
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                //指定child横向可拖拽的范围
                return child == mDrawerGroup ? mMenuView.getWidth() : 0;
            }
        });
        // 设置左边缘检测，即从屏幕左边划进屏幕时，会回调onEdgeDragStarted
        mHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置各个子view的大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);

        // 按照xml文件的布局，获取各个子view
        mContentView = getChildAt(0);
        mScrimView = getChildAt(1);
        mDrawerGroup = (ViewGroup) getChildAt(2);
        mMenuView = mDrawerGroup.getChildAt(0);
        mHandleView = mDrawerGroup.getChildAt(1);

        MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
        int contentWidthSpec = MeasureSpec.makeMeasureSpec(
                widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
        int contentHeightSpec = MeasureSpec.makeMeasureSpec(
                heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
        mContentView.measure(contentWidthSpec, contentHeightSpec);

        lp = (MarginLayoutParams) mScrimView.getLayoutParams();
        int bgWidthSpec = MeasureSpec.makeMeasureSpec(
                widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
        int bgHeightSpec = MeasureSpec.makeMeasureSpec(
                heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
        mScrimView.measure(bgWidthSpec, bgHeightSpec);

        lp = (MarginLayoutParams) mDrawerGroup.getLayoutParams();
        int menuGroupWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                lp.leftMargin + lp.rightMargin, lp.width);
        int menuGroupHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                lp.topMargin + lp.bottomMargin, lp.height);
        mDrawerGroup.measure(menuGroupWidthSpec, menuGroupHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 设置各子view的位置，注意第一次初始化位置和以后设置的位置略有区别

        mInLayout = true;
        MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
        mContentView.layout(lp.leftMargin, lp.topMargin,
                lp.leftMargin + mContentView.getMeasuredWidth(),
                lp.topMargin + mContentView.getMeasuredHeight());

        lp = (MarginLayoutParams) mScrimView.getLayoutParams();
        mScrimView.layout(lp.leftMargin, lp.topMargin,
                lp.leftMargin + mScrimView.getMeasuredWidth(),
                lp.topMargin + mScrimView.getMeasuredHeight());

        lp = (MarginLayoutParams) mDrawerGroup.getLayoutParams();
        int groupLeft;// = - mMenuView.getMeasuredWidth() + lp.leftMargin;
        if(mFirstLayout){
            groupLeft = - mMenuView.getMeasuredWidth() + lp.leftMargin;
        }else{
            groupLeft = mDrawerGroup.getLeft();
        }
        mDrawerGroup.layout(groupLeft, lp.topMargin,
                groupLeft + mDrawerGroup.getMeasuredWidth(),
                lp.topMargin + mDrawerGroup.getMeasuredHeight());

        initScrimView();

        mInLayout = false;
        mFirstLayout = false;
    }

    @Override
    public void requestLayout() {
        if(!mInLayout) {
            super.requestLayout();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFirstLayout = true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mFirstLayout = true;
    }

    private void initScrimView(){
        if(mFirstLayout && mScrimView != null){
            mScrimView.setAlpha(mOpenPercent);
            if(floatCompare(mOpenPercent, 0f)){
                mScrimView.setVisibility(GONE);
            }else{
                mScrimView.setVisibility(VISIBLE);
            }
            mScrimView.setOnClickListener(onClickListener);
        }
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isDrawerOpen()){
                closeDrawer();
            }
        }
    };

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 把事件处理交给ViewDragHelper
        return mHelper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 把事件处理交给ViewDragHelper
        mHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        // 滚动过程的计算，也是交给ViewDragHelper，按以下这么写就好了
        if (mHelper.continueSettling(true)) {
            invalidate();
        }
    }

    // 以下三个方法需要重写，没啥特殊要求直接返回MarginLayoutParams就可以了

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


    /**
     * 两个float类型的大小比较
     * @return true 相等，false 不相等
     */
    private boolean floatCompare(float f1, float f2){
        return Math.abs(f1 - f2) < Float.MIN_VALUE;
    }

    /**
     * 是否drawer已打开。float相等直接比较可能有存在问题
     */
    public boolean isDrawerOpen(){
        return floatCompare(mOpenPercent, 1f);
    }

    /**
     * 打开drawer
     */
    public void openDrawer() {
        mOpenPercent = 1.0f;
        mHelper.smoothSlideViewTo(mDrawerGroup, 0, mDrawerGroup.getTop());
        invalidate();
    }

    /**
     * 关闭drawer
     */
    public void closeDrawer() {
        mOpenPercent = 0.0f;
        mHelper.smoothSlideViewTo(mDrawerGroup, -mMenuView.getWidth(), mDrawerGroup.getTop());
        invalidate();
    }

    private OnDrawerListener mDrawerListener = null;

    /**
     * 外部可设置监听drawer的位置回调
     * @param listener
     */
    public void setOnDrawerListener(OnDrawerListener listener) {
        mDrawerListener = listener;
    }

    public interface OnDrawerListener{
        /**
         * 抽屉打开关闭过程监听
         * @param percent 取值区间[0, 1]，0代表完全关闭，1代表完全打开
         */
        void onDrawer(float percent);
    }

}