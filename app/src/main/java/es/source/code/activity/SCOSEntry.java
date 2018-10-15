package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

import es.source.code.utils.Const;

/**
 * @author taoye
 * @date 2018/9/17.
 * @classname SCOSEntry.java
 * @description: APP入口，展示LOGO
 */

public class SCOSEntry extends AppCompatActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private float x1 = 0 , x2 = 0; // 记录手指按下和离开屏幕时的横坐标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.entry);

        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    // 重写onTouchEvent方法，监听屏幕点击事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            // 当手指按下的时候的横坐标
            x1 = event.getX();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            // 当手指离开的时候的横坐标
            x2 = event.getX();
            if(x1 - x2 > 50){
                // 显示启动
                // Intent intent = new Intent(SCOSEntry.this,MainScreen.class);
                // 隐式启动
                Intent intent = new Intent("scos.intent.action.SCOSMAIN");
                intent.addCategory("scos.intent.category.SCOSLAUNCHER");
                if(sp.getInt("loginState",0) == 1){
                    intent.putExtra(Const.IntentMsg.MESSAGE, Const.IntentMsg.MSG_FROM_ENTRY);
                }else{
                    intent.putExtra(Const.IntentMsg.MESSAGE,Const.IntentMsg.MSG_NOT_LOGIN);
                }
                startActivity(intent);
                finish();
            }
        }
        return super.onTouchEvent(event);
    }
}




