package cn.cast.flybird;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class MyView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    private SurfaceHolder holder;
    private Resources resources;
    private Bitmap bg;
    private Thread th;
    private boolean flag;//游戏标识符
    private Canvas canvas;//画布
    private Paint paint;//画笔
    private Rect bgRect;
    Bird bird;
    private GameBarrierLayer gameBarrierLayer;
//    private GameTime gameTime;
    public static int GameState=GameProperty.GAME_ING;
    public static int score=0;
    private boolean isGamePaused = false;
    File file;//最高纪录
    // 定义一个回调接口
    public interface OnMyViewListener {//
        void hideButton();
        void seeButton();

    }

    // 持有回调接口的实例
    private OnMyViewListener listener;
    public MyView(Context context, AttributeSet attrs, OnMyViewListener listener) {
        super(context, attrs);
        this.listener = listener;

    }
    public MyView(Context context, OnMyViewListener listener) {
        super(context);
        this.listener = listener;
        holder=getHolder();
        resources=getResources();
        holder.addCallback(this);
        setFocusable(true);
    }

    public MyView(Context context) {
        super(context);
        holder=getHolder();
        resources=getResources();
        holder.addCallback(this);
        setFocusable(true);
    }


    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    //创建时启动

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        initGame();//初始化游戏

    }

    private void initGame() {
        bg= BitmapFactory.decodeResource(resources,GameProperty.GAME_BG);//初始化背景
        th=new Thread(this);//初始化线程
        bgRect=new Rect(0,0,getWidth(),getHeight());
        paint=new Paint();
        paint.setAntiAlias(true); // 开启抗锯齿
        paint.setColor(Color.BLACK);
        paint.setTextSize(100);
        initBird();//初始化小鸟
        initBarrier1();//初始化管道

        flag=true;//
        File internalStorageDir = getContext().getFilesDir();// 创建文件对象
        file = new File(internalStorageDir, "data.txt");
//        try (FileOutputStream fos = new FileOutputStream(file)) {
//            // 不需要写入任何数据，只是打开然后立即关闭以截断文件
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        th.start();
    }

    private void initBarrier1(){
        gameBarrierLayer=new GameBarrierLayer(getWidth(),getHeight());
//        barriers=new Vector <>();
        for (int i=0;i<3;i++) {
                Barrier b1 = new Barrier(getWidth() / 2 + getWidth() / 7 * i, 0, 350,GameProperty.BARRIER_NORMALUP);
                gameBarrierLayer.barriers.add(b1);
                Barrier b2 = new Barrier(getWidth() / 2 + getWidth() / 7 * i,getHeight()-350, 350,GameProperty.BARRIER_NORMALDOWN);
                gameBarrierLayer.barriers.add(b2);
        }
    }


    private  void initBird(){
        Bitmap birddown=BitmapFactory.decodeResource(resources,GameProperty.BIRD_DOWN);
        Bitmap birdnormal=BitmapFactory.decodeResource(resources,GameProperty.BIRD_NORMAL);
        Bitmap birdup=BitmapFactory.decodeResource(resources,GameProperty.BIRD_UP);
        Bitmap[] bitmaps=new Bitmap[]{birddown,birdnormal,birdup};
        bird=new Bird(bitmaps,getWidth(),getHeight());

    }



    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void run() {

        while (flag) {
            if(!isGamePaused){
                myDraw();//绘画函数
                logic();//绘画逻辑
                //是否重新游戏
                while(!flag){}
                if (listener != null) {
                    // 假设 listener 是 MainActivity 的实例
                    ((MainActivity) listener).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.seeButton();
                        }
                    });
                }
                try {
                    sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void myDraw() {
        try{
            canvas = holder.lockCanvas();
            canvas.drawBitmap(bg, null, bgRect, paint);

            switch (GameState) {
                case GameProperty.GAME_ING://游戏进行时
                    bird.draw(canvas, paint);
                    gameBarrierLayer.draw(canvas, paint);

                    canvas.drawText("score:" + score, 0, 100, paint);

                    break;
                case GameProperty.GAME_LOSE://游戏失败
                    try {
                        // 打开文件输入流
                        FileInputStream fis = new FileInputStream(file);
                        // 读取数据
                        int size = fis.available();
                        byte[] buffer = new byte[size];
                        fis.read(buffer);
                        fis.close();
                        // 将字节数组转换
                        String content = new String(buffer, "UTF-8");
                        int number = 0;
                        if (!content.isEmpty()) {
                            number = Integer.parseInt(content);
                        }
                        // 如果有更高得分，将score数据写入到文件
                        if (number < score) {
                            FileOutputStream fos = new FileOutputStream(file);
                            String data = String.valueOf(score);
                            fos.write(data.getBytes(StandardCharsets.UTF_8));
                            fos.close();
                            number = score;
                            canvas.drawText("最高纪录:" + number + "分(新)", 0, 100, paint);
                        } else {
                            canvas.drawText("最高纪录:" + number + "分", 0, 100, paint);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    paint.setColor(Color.RED);
                    canvas.drawText("游戏结束", getWidth() / 2 - 200, getHeight() / 2 - 160, paint);
                    paint.setTextSize(70);
                    paint.setColor(Color.BLACK);
                    canvas.drawText("您的分数：" + score + "分", 0, 200, paint);
                    canvas.drawText("点击按钮重新开始", getWidth() / 2 - 300, getHeight() / 2 , paint);
                    paint.setTextSize(100);
                    //您应该确保 hideButton 方法在主线程上执行。您可以使用 Activity 的 runOnUiThread 方法或者 Handler 来将 hideButton 的调用回调到主线程
                    if (listener != null) {
                        // 假设 listener 是 MainActivity 的实例
                        ((MainActivity) listener).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listener.hideButton();
                            }
                        });
                    }
                    flag = false;//当游戏结束时，线程也结束
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void logic(){
        bird.logic();
        gameBarrierLayer.logic();
        logicCollision();//碰撞逻辑
        isOver();//判断管道被超过，添加分数
    }

    private void isOver() {
        for (int i=0;i<gameBarrierLayer.barriers.size();i++) {
            Barrier barrier = gameBarrierLayer.barriers.elementAt(i);
            if (barrier.isOver && (barrier.type == GameProperty.BARRIER_NORMALUP||barrier.type == GameProperty.BARRIER_DYN)) {
                score++;
                barrier.isOver = false;
                barrier.isTake=true;
            }
        }


    }

    private void logicCollision() {
        for(int i=0;i<gameBarrierLayer.barriers.size();i++){
            Barrier barrier=gameBarrierLayer.barriers.elementAt(i);
            if(barrier.isCollision(bird)){
                GameState=GameProperty.GAME_LOSE;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            bird.Toup();//小鸟上升函数
        }
        return true;
    }
    public void pauseGame() {
        isGamePaused = true;
        // 在这里可以添加其他暂停游戏时需要执行的代码
    }
    public void resumeGame() {
        isGamePaused = false;
        // 在这里可以添加其他恢复游戏时需要执行的代码
    }
    public void restart(){
        flag=true;
        GameState=GameProperty.GAME_ING;
        score=0;
        initBarrier1();
        bird.restart();
    }

}
