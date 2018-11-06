package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import es.source.code.adapters.NavigationAdapter;
import es.source.code.model.User;
import es.source.code.utils.Const;

/**
 * @author taoye
 * @date 2018/9/26.
 * @classname MainScreen.java
 * @description 主界面，含导航按钮
 */
public class MainScreen extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private Context mContext; // 上下文
    private Intent intent;
    private GridView gv_navigation;

    private SharedPreferences sp;

    private User loginUser = null; // 当前登录的用户对象
    private User user; // User的temp对象
    private boolean isShowAll = false; // 标记导航项是否完全显示

    private int[] images = {R.mipmap.ic_login_navigation,R.mipmap.ic_help_navigation,R.mipmap.ic_order_navigation,R.mipmap.ic_vieworder_navigation};
    private String[] texts = {"登录/注册","系统帮助","点菜","查看订单"};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        mContext = MainScreen.this;

        initViews();
    }

    /**
     * Author; taoye
     * Description: 初始化布局
     */
    private void initViews(){
        gv_navigation = (GridView) findViewById(R.id.gridView_navigation);
        gv_navigation.setOnItemClickListener(this);

        sp = getSharedPreferences("User",Context.MODE_PRIVATE);

        intent = getIntent();
        // 处理Intent中携带的信息，并根据信息向GridView中添加不同数量的导航按钮
        handleIntentMsg(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.gridView_navigation) {
            switch (images[i]) {
                case R.mipmap.ic_login_navigation: // 登录/注册
                    intent = new Intent(mContext, LoginOrRegister.class);
                    startActivityForResult(intent, Const.RequestCode.MAINSCREEN);
                    break;
                case R.mipmap.ic_order_navigation: // 点菜
                    if (loginUser != null) {
                        intent = new Intent(mContext, FoodView.class);
                        intent.putExtra(Const.IntentMsg.USER, loginUser);// 将当前的用户对象传递给FoodView
                        startActivityForResult(intent, Const.RequestCode.MAINSCREEN);
                    }
                    break;
                case R.mipmap.ic_vieworder_navigation: // 查看订单
                    if (loginUser != null) {
                        intent = new Intent(mContext, FoodOrderView.class);
                        intent.putExtra(Const.IntentMsg.USER, loginUser);// 将当前的用户对象传递给FoodOrderView
                        intent.putExtra(Const.IntentMsg.DEFAULTPAGE, Const.IntentMsg.PAGE_UNORDERED);// 默认初始页为“未下单菜”
                        startActivityForResult(intent, Const.RequestCode.MAINSCREEN);
                    }
                    break;
                case R.mipmap.ic_help_navigation: // 系统帮助
                    if (loginUser != null) {
                        intent = new Intent(mContext, SCOSHelper.class);
                        intent.putExtra(Const.IntentMsg.USER, loginUser);// 将当前的用户对象传递给SCOSHelper
                        startActivityForResult(intent, Const.RequestCode.MAINSCREEN);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        switch(resultCode){
            case Const.ResultCode.FROM_LOGINORREGISTER:
                // 若intent中携带的是"Return"，则不做变化
                // 若intent中携带的是"LoginSuccess"或"RegisterSuccess"，则需要检查导航按钮是否显示完全，若没有则重新显示完全。
                handleIntentMsg(intent);
                break;
            case Const.ResultCode.FROM_FOODVIEW:
                user = (User) intent.getSerializableExtra(Const.IntentMsg.USER);
                if(user != null){
                    loginUser.setOrderList(user.getOrderList());// 更新loginUser对象的订单列表
                }
                break;
            case Const.ResultCode.FROM_FOODORDERVIEW:
                user = (User) intent.getSerializableExtra(Const.IntentMsg.USER);
                if(user != null){
                    loginUser.setOrderList(user.getOrderList());
                }
                break;
        }
    }

    /**
     * Author: taoye
     * Description: 处理Intent携带的信息,绘制导航项
     */
    private void handleIntentMsg(Intent intent){
        String message = intent.getStringExtra(Const.IntentMsg.MESSAGE);
        if(message == null){ // 从外部APP跳转,根据当前用户的loginState判断是否显示所有的导航按钮
//            if(sp.getInt("loginState",0) == 0){
                message = Const.IntentMsg.MSG_NOT_LOGIN;
//            }else{
//                message = Const.IntentMsg.MSG_FROM_ENTRY;
//            }
        }

        switch(message){
            case Const.IntentMsg.MSG_FROM_ENTRY: // 从SCOSEntry
                // 为GridView控件绑定适配器
                gv_navigation.setAdapter(new NavigationAdapter(this, images, texts));
                isShowAll = true;
                break;

            case Const.IntentMsg.MSG_LOGIN_SUCC: // 从LoginOrRegister， 登录成功
                if(!isShowAll){
                    gv_navigation.setAdapter(new NavigationAdapter(this, images, texts));
                    isShowAll = true;
                }
                loginUser = (User) intent.getSerializableExtra(Const.IntentMsg.USER);
                break;

            case Const.IntentMsg.MSG_REGISTER_SUCC: // 从LoginOrRegister， 注册成功
                if(!isShowAll){
                    gv_navigation.setAdapter(new NavigationAdapter(this, images, texts));
                    isShowAll = true;
                }
                loginUser = (User) intent.getSerializableExtra(Const.IntentMsg.USER);
                Toast.makeText(mContext, "欢迎您成为 SCOS 新用户", Toast.LENGTH_SHORT).show();
                break;

            case Const.IntentMsg.MSG_NOT_LOGIN: // 未登录
                int[] images1 = {R.mipmap.ic_login_navigation,R.mipmap.ic_help_navigation};
                String[] texts1 = {"登录/注册","系统帮助"};
                gv_navigation.setAdapter(new NavigationAdapter(this, images1, texts1));
                isShowAll = false;
                break;

            case Const.IntentMsg.MSG_FROM_FOODUPDATE_NOTIFICATION: // 从FoodUpdate的通知，已登陆
                // 为GridView控件绑定适配器
                gv_navigation.setAdapter(new NavigationAdapter(this, images, texts));
                isShowAll = true;
                break;

            default:
                break;
        }
    }
}
