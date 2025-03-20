package cn.cast.flybird;

import static cn.cast.flybird.GameProperty.BARRIER_DYN;
import static cn.cast.flybird.GameProperty.BARRIER_NORMALDOWN;
import static cn.cast.flybird.GameProperty.BARRIER_NORMALUP;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;


public class Barrier {
    private  Resources resources;
    private Bitmap barrier;
    public Bitmap[] bitmaps;
    int x,y,height,width;
    private int screenW,screenH;//手机屏幕宽度
    int speedx=5;
    int speedy=7;
    public int type;
    public boolean isOver=false;//被超越标签
    public boolean isTake=false;//是否已经加分
    public static Bitmap mapup;
    public static Bitmap mapdown;
    public static Bitmap mapnor;
    static {
        Context context = MyApplication.getInstance().getApplicationContext(); // 假设你有一个单例应用类
        // 加载图片资源
        mapup = BitmapFactory.decodeResource(context.getResources(), R.mipmap.barrier_up);

        // 将Bitmap设置为静态变量
        mapdown = BitmapFactory.decodeResource(context.getResources(), R.mipmap.barrier_down);
        mapnor=BitmapFactory.decodeResource(context.getResources(), R.mipmap.barrier);
    }
    public Barrier(Bitmap barrier,int x, int y,int screenW,int screenH,int type) {
        this.barrier = barrier;
        this.x = x;
        this.y = y;
        this.screenW=screenW;
        this.screenH=screenH;
        this.type=type;
        this.width=mapnor.getWidth();

    }
    public Barrier(int x, int y,int height,int type) {
        this.x = x;
        this.y = y;
        this.height=height;
        this.type=type;
    }
    public void draw(Canvas canvas, Paint paint){//绘制管道
        switch(type){
            case BARRIER_NORMALUP:
                drawtop(canvas,paint);
                break;
            case BARRIER_NORMALDOWN:
                drawdown(canvas,paint);
                break;
            case BARRIER_DYN:
                drawdynamic(canvas,paint);
                break;
        }
    }
    public void drawtop(Canvas canvas, Paint paint){//绘制管道
        int count=(height-mapdown.getHeight())/mapnor.getHeight()+1;
        for(int i=0;i<count;i++){
            canvas.drawBitmap(mapnor,x,y+mapnor.getHeight()*i,paint);
        }
        int Y=height-mapdown.getHeight();
        canvas.drawBitmap(mapdown,x-2,Y,paint);
    }
    public void drawdown(Canvas canvas, Paint paint){//绘制管道
        int count=(height-mapup.getHeight())/mapnor.getHeight()+1;
        for(int i=0;i<count;i++){
            canvas.drawBitmap(mapnor,x,y+mapup.getHeight()+mapnor.getHeight()*i,paint);
        }
        canvas.drawBitmap(mapup,x-2,y,paint);
    }
    public void drawdynamic(Canvas canvas, Paint paint){//绘制管道
        int count=(height-mapup.getHeight()-mapdown.getHeight())/mapnor.getHeight()+1;
        for(int i=0;i<count;i++){
            canvas.drawBitmap(mapnor,x,y+mapup.getHeight()+mapnor.getHeight()*i,paint);
        }
        canvas.drawBitmap(mapup,x-2,y,paint);
        int Y=y+height-mapdown.getHeight();
        canvas.drawBitmap(mapdown,x-2,Y,paint);
    }
    public void logic(){
        x=x-speedx;
        if (type==BARRIER_DYN){
            y+=speedy;
            if(y+height>1300){
                y=1300-height;
                speedy=-speedy;
            }
            else if(y<200){
                y=200;
                speedy=-speedy;
            }
        }

        if (x+width<=screenW/6&&isTake==false) {
                isOver=true;
            }
//
    }


        public boolean isCollision(Bird bird){//碰撞检测
            if(bird.x+bird.bitmaps[bird.i].getWidth()<x){//鸟的右边没有碰撞
                return false;
            }else if (bird.y>y+height){//鸟的上边没有碰撞
                return false;
            }else if (bird.y+ bird.bitmaps[bird.i].getHeight()<y){//鸟的下边没有碰撞
                return false;
            }else if (bird.x>width+x){//鸟的左边没有碰撞
                return false;
            }
            return true;
        }
}
