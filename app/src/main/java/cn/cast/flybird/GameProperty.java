package cn.cast.flybird;

public class GameProperty {
    public static final int GAME_BG=R.mipmap.bg;
    public static final int BIRD_DOWN = R.mipmap.bird_down;
    public static final int BIRD_NORMAL = R.mipmap.bird_normal;
    public static final int BIRD_UP = R.mipmap.bird_up;
    public static final int BARRIER = R.mipmap.barrier;
    public static final int BARRIER_DOWN = R.mipmap.barrier_down;
    public static final int BARRIER_UP = R.mipmap.barrier_up;
    //管子图片的normal类型似乎是指up和down两种图片，special则是指正常barrier图片
    public static final int BARRIER_NORMALUP=1;
    public static final int BARRIER_NORMALDOWN=2;
    public static final int BARRIER_DYN=3;
    //游戏状态
    public static final int GAME_ING=1;
    public static final int GAME_LOSE=2;
}
