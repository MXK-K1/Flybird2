package cn.cast.flybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Bird {
    public Bitmap[] bitmaps;
    private int screenW,screenH;
    public int x,y;
    public int speed=45;
    public int i=2;
    private boolean isUp=false;//判断鸟的上下方向

    public Bird(Bitmap[] bitmaps, int screenW, int screenH) {
        this.bitmaps = bitmaps;
        this.screenW = screenW;
        this.screenH = screenH;
        x=screenW/6;
        y=screenH/2;
    }
    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(bitmaps[i],x,y,paint);
    }
    public void logic(){
        if (i==2){//上
            isUp=false;
        }else if(i==0){//下
            isUp=true;
        }
        if(isUp){
            i++;
        }else {
            i--;
        }
        if(y<=screenH-100){
            y+=5;
        }
        if(x>screenW/6){
            x-=10;
        }
        if(x+bitmaps[1].getWidth()>screenW){
            x=screenW;
        }
    }
    public void Toup(){
        if (y>=0) {
            y = y - speed;
        }

    }
    public void restart(){
        x=screenW/6;
        y=screenH/2;
    }

}

