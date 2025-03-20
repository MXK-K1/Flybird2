package cn.cast.flybird;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MyView.OnMyViewListener{
    private MyView myView;
    private Button skillButton1;
    private Button stopButton;
    private Button resetButton;
    private boolean isPaused = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myView = new MyView(this,this);
        FrameLayout container = findViewById(R.id.container);
        container.addView(myView);
        skillButton1 = findViewById(R.id.skibutton1);//闪现
        stopButton = findViewById(R.id.pause_button);//暂停
        resetButton=findViewById(R.id.resetButton);//重新开始
        resetButton.setEnabled(false);//结束才出现
        skillButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理按钮点击事件，比如调用 MyView 的某个方法来触发技能
                myView.bird.x+=200;
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPaused) {
                    myView.pauseGame();
                    isPaused = true;
                    stopButton.setText("恢复"); // 更改按钮文本以反映当前状态
                } else {
                    myView.resumeGame();
                    isPaused = false;
                    stopButton.setText("暂停游戏"); // 恢复原始按钮文本
                }

            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.restart();
            }
        });
    }
    public void hideButton() {
        // 隐藏按钮
        skillButton1.setVisibility(View.GONE);
        stopButton.setVisibility(View.GONE);//消失
        resetButton.setEnabled(true);//可点击状态
        resetButton.setVisibility(View.VISIBLE);//可见
    }

    @Override
    public void seeButton() {
        skillButton1.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.VISIBLE);//可见
        resetButton.setVisibility(View.GONE);//消失
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}