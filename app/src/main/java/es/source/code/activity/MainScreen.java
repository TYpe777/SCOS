package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import es.source.code.model.User;
import es.source.code.utils.Const;
import es.source.code.utils.myAdapter;

/**
 * Author: taoye
 * Classname: MainScreen.java
 * Description: 主界面，含导航按钮
 * Date: 2018/9/26.
 */
public class MainScreen extends ActionBarActivity {
    private Context mContext;
    private Intent intent;
    private GridView gv_navigation;

    private User loginUser = null;
    private boolean isShowAll = false; // 标记导航项是否完全显示
    private int[] images = {R.drawable.login,R.drawable.help,R.drawable.order,R.drawable.vieworder};
    private String[] texts = {"登录/注册","系统帮助","点菜","查看订单"};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_acti);
        mContext = MainScreen.this;

        initUI();
    }

    /**
     * Author; taoye
     * Description: 初始化布局
     */
    private void initUI(){
        gv_navigation = (GridView) findViewById(R.id.gridView_navigation);

        intent = getIntent();
        handleIntentMsg(intent);

        gv_navigation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (images[i]) {
                    case R.drawable.login:
                        intent = new Intent(MainScreen.this, LoginOrRegister.class);
                        startActivityForResult(intent, Const.RequestCode.LOGINORPEGISTER);
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        handleIntentMsg(intent);
    }

    /**
     * Author: taoye
     * Description: 处理Intent携带的信息,绘制导航项
     */
    private void handleIntentMsg(Intent intent){
        String message = intent.getStringExtra(Const.IntentMsg.MESSAGE);
        if(message == null){
            message = Const.IntentMsg.FROM_THIRDPARTY;
        }

        switch(message){
            case Const.IntentMsg.FROM_ENTRY:
                gv_navigation.setAdapter(new myAdapter(this, images, texts));
                isShowAll = true;
                break;

            case Const.IntentMsg.LOGIN_SUCC:
                if(!isShowAll){
                    gv_navigation.setAdapter(new myAdapter(this, images, texts));
                    isShowAll = true;
                }
                loginUser = (User) intent.getSerializableExtra(Const.IntentMsg.USER);
                break;

            case Const.IntentMsg.REGISTER_SUCC:
                if(!isShowAll){
                    gv_navigation.setAdapter(new myAdapter(this, images, texts));
                    isShowAll = true;
                }
                loginUser = (User) intent.getSerializableExtra(Const.IntentMsg.USER);
                Toast.makeText(mContext,"欢迎您成为 SCOS 新用户",Toast.LENGTH_SHORT).show();
                break;

            case Const.IntentMsg.FROM_THIRDPARTY:
                int[] images1 = {R.drawable.login,R.drawable.help};
                String[] texts1 = {"登录/注册","系统帮助"};
                gv_navigation.setAdapter(new myAdapter(this, images1, texts1));
                isShowAll = false;
                break;

            default:
                break;
        }
    }

}
