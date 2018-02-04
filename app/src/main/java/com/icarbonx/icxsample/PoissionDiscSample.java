package com.icarbonx.icxsample;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/2/2
 * Description:
 */
public class PoissionDiscSample {

    public static final void main(String [] args) {
//        int k = 0;
//        boolean isFlag = true;
//        PoissionDiscSample sample = new PoissionDiscSample(500, 500, 50);
//        while (isFlag) {
//            for (int i = 0; i < 10; ++i) {
//                double [] out = sample.execute();
//                if (out != null) {
//                    System.out.print(k + "["+ out[0] + ", " + out[1] + "]");
//                    k++;
//                } else {
//                    isFlag = false;
//                    break;
//                }
//            }
//        }
        double a = 1.99;
        int b = (int) a;
        System.err.println(b);
    }

    int k;
    int radius2;
    int R;

    double cellSize;
    int gridWidth;
    int gridHeight;
    double [] grid;

    List<double[]> queue;
    int queueSize = 0;
    int sampleSize = 0;

    private int mWidth;
    private int mHeight;

    public PoissionDiscSample(int width, int height, int radius) {
        mWidth = width;
        mHeight = height;
        k = 30;
        radius2 = radius * radius;
        R = 3 * radius2;
        cellSize = radius * (1 / Math.sqrt(2));
        gridWidth = (int) Math.ceil(width / cellSize);
        gridHeight = (int) Math.ceil(height / cellSize);
        grid = new double[gridWidth * gridHeight];
        queue = new ArrayList();
    }

    /**
     * 执行
     * @return
     */
    public double [] execute() {
        if (sampleSize == 0) {
            return sample(Math.random() * mWidth, Math.random() * mHeight);
        }
        while (queueSize > 0) {
            int i = (int) (Math.random() * queueSize);
            double[] s = queue.get(i);
            for (int j = 0; j < k; ++j) {
                double a = 2 * Math.PI * Math.random();
                double r = Math.sqrt(Math.random() * R + radius2);
                double x = s[0] + r * Math.cos(a);
                double y = s[1] + r * Math.sin(a);

                if (0 <= x && x < mWidth && 0 <= y && y < mHeight && far(x, y)) {
                    return sample(x, y);
                }
            }
            queue.set(i, queue.get(--queueSize));
            queue.remove(queueSize);
        }
        return null;
    }

    /**
     * 返回采样
     * @param x
     * @param y
     * @return
     */
    public double [] sample(double x, double y) {
        double s[] = {x, y};
        queue.add(s);
        int index = gridWidth * (int)(y / cellSize) + (int)(x / cellSize);
        if (index < grid.length - 1) {
            grid[index] = s[0];
            grid[index + 1] = s[1];
            ++sampleSize;
            ++queueSize;
            return s;
        } else {
            return null;
        }
    }

    /**
     * 判断距离
     * @param x
     * @param y
     * @return
     */
    public boolean far(double x, double y) {
        int i = (int) (x / cellSize);
        int j = (int) (y / cellSize);
        int i0 = Math.max(i - 2, 0);
        int j0 = Math.max(j - 2, 0);
        int i1 = Math.min(i + 3, gridWidth);
        int j1 = Math.min(j + 3, gridHeight);

        for (j = j0; j < j1; ++j) {
            int o = j * gridWidth;
            double s[] = new double[2];
            for (i = i0; i < i1; ++i) {
                if(grid.length > o + i + 1) {
                    s[0] = grid[o + i];
                    s[1] = grid[o + i + 1];
                    double dx = s[0] - x;
                    double dy = s[1] - y;
                    if (dx * dx + dy * dy < radius2) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
