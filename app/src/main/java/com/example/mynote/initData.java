package com.example.mynote;


//初始化数据类，用作每个activity 获取布局样式
public class initData {
    private static int Bgstyle=R.style.AppTheme;

    public static int getBgstyle() {
        return Bgstyle;
    }

    public static void setBgstyle(int bgstyle) {
        Bgstyle = bgstyle;
    }
}
