package cn.cast.flybird;

import static cn.cast.flybird.GameProperty.BARRIER_DYN;
import static cn.cast.flybird.GameProperty.BARRIER_NORMALDOWN;
import static cn.cast.flybird.GameProperty.BARRIER_NORMALUP;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;
import java.util.Vector;

public class GameBarrierLayer {
    int screenw,screenh;
    int uph,downh,dyh,dynay;
    int level=0;
    Vector<Barrier> barriers;
    public GameBarrierLayer(int screenw,int screenh){
        barriers = new Vector<>();
        this.screenh=screenh;
        this.screenw=screenw;
    }
    //绘制障碍物
    public void draw(Canvas canvas, Paint paint){
        for(int i=0;i<barriers.size();i++){
            Barrier barrier=barriers.get(i);
            barrier.draw(canvas,paint);
        }
    }
    //逻辑包括1.清除在屏幕外的障碍物，2.添加新的障碍物,3.障碍物移动逻辑4.随时间难度增加
    public void logic(){
        level+=1;
//        System.out.println("level:"+level);
        //障碍物移动
        for(int i=0;i<barriers.size();i++){//管道的逻辑是向左移动，使小鸟看起来右移
            Barrier barrier=barriers.elementAt(i);
            barrier.logic();
            //难度增加
            if(level<250){}
            else if(level<500){
                barrier.x-=5;
            }
            else if(level<750){
                barrier.x-=10;
            }
            else if(level<1000){
                barrier.x-=15;
            }
            else {
                barrier.x-=25;
            }
        }
        if(barriers.size()!=0){
            //增加障碍物
            Barrier last=barriers.get(barriers.size()-1);
           if(isInFrame(last.x, last.width)){
               Random random=new Random();
               int type=random.nextInt(100);
               ran(type);
               if(type>50){
                   Barrier dy=new Barrier(screenw,dynay,dyh,BARRIER_DYN);
                   barriers.add(dy);
               }
               else {
                   Barrier top=new Barrier(screenw,0,uph,BARRIER_NORMALUP);
                   barriers.add(top);
                   Barrier down=new Barrier(screenw,screenh-downh,downh,BARRIER_NORMALDOWN);
                   barriers.add(down);
               }
           }
           //清除障碍物,释放内存
            Barrier first=barriers.get(barriers.size()-1);
           while(first.x+ first.width<0){
               barriers.remove(0);
               first=barriers.get(barriers.size()-1);
           }
        }
    }
    public void ran(int type){
        if(type>50){
            Random random=new Random();
            uph=300+random.nextInt(600);
            downh=400+random.nextInt(600);
            if(uph+downh>screenh-150){
                ran(type);
            }
        }
        else{
            Random random=new Random();
            dyh=400+random.nextInt(400);
            dynay=200+random.nextInt(1100-dyh);
        }
    }

    public boolean isInFrame(int x,int width){
        return (x+width+screenw/3)<screenw;
    }
}
