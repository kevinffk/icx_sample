package com.icarbonx.icxsample;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Random;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/2/2
 * Description:
 */
public class MyRandom {

    private int mSizeWidth; //宽
    private int mSizeHeight; //高
    private int mRadius;   //半径

    private int k = 4;  //随机比对数

    private Point point;

    private ArrayList<Point> mPointArray;

    private int mRadius2; //距离平方


    public MyRandom(int sizeWidth, int sizeHeight, int radius) {
        this.mSizeWidth = sizeWidth;
        this.mSizeHeight = sizeHeight;
        this.mRadius = radius;
        this.mRadius2 = mRadius * mRadius;
    }

    public void generate() {
        //生成第一个点
        Point point = new Point();
        point.x = (int) (mSizeWidth * Math.random());
        point.y = (int) (mSizeHeight * Math.random());


        //生成这个点等距离的其他点
    }

    public ArrayList<Point> getPointArray() {
        return mPointArray;
    }

    public static final void main(String [] args) {

        /*cellSize = radius * Math.SQRT1_2,
                gridWidth = Math.ceil(width / cellSize),
                gridHeight = Math.ceil(height / cellSize),*/
        int width = 960;
        int height= 500;
        double cellSize =  10 * Math.sqrt(2);

        double gridWidth = Math.ceil(width / cellSize);
        double gridHeight = Math.ceil(height / cellSize);

        System.out.println(gridWidth + " " + gridHeight);
    }
}
