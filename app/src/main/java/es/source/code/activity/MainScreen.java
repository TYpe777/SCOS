package es.source.code.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import es.source.code.utils.Const;

/**
 * Author: taoye
 * Classname: MainScreen.java
 * Description: 主界面，含导航按钮
 * Date: 2018/9/26.
 */
public class MainScreen extends ActionBarActivity implements OnClickListener{

    private Button btn_order,btn_vieworders,btn_loginorup,btn_help; // 导航Button控件
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_acti);

        init();
    }

    /**
     * Author; taoye
     * Description: 初始化控件
     */
    private void init(){
        btn_order = (Button) findViewById(R.id.btn_order);
        btn_vieworders = (Button) findViewById(R.id.btn_vieworders);
        btn_loginorup = (Button) findViewById(R.id.btn_loginorup);
        btn_help = (Button) findViewById(R.id.btn_help);

        btn_loginorup.setOnClickListener(this);

        handleIntentMsg();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_loginorup:
                intent = new Intent(MainScreen.this, LoginOrRegister.class);
                startActivityForResult(intent, Const.RequestCode.LOGINORPEGISTER);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        switch(resultCode){
            case Const.RespondCode.RETURN:
                handleIntentMsg();
                break;
            case Const.RespondCode.LOGINSUCC:
                break;
        }
    }

    /**
     * Author: taoye
     * Description: 处理Intent携带的信息
     */
    private void handleIntentMsg(){
        intent = getIntent();
        String message = intent.getStringExtra(Const.IntentMsg.MESSAGE);
        if(message != null){
            if(!message.equals(Const.IntentMsg.FROM_ENTRY) && !message.equals(Const.IntentMsg.RETURN)){
                btn_order.setVisibility(View.GONE);
                btn_vieworders.setVisibility(View.GONE);
            }
            if(message.equals(Const.IntentMsg.LOGIN_SUCC)){
                if(btn_order.getVisibility() != View.VISIBLE){
                    btn_order.setVisibility(View.VISIBLE);
                }
                if(btn_vieworders.getVisibility() != View.VISIBLE){
                    btn_vieworders.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}
