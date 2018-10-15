package es.source.code.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.source.code.model.User;
import es.source.code.utils.Const;

/**
 * @author taoye
 * @date 2018/9/26.
 * @classname LoginOrRegister.java
 * @description 登录和注册
 */

public class LoginOrRegister extends AppCompatActivity implements OnClickListener {

    private Context mContext; // 上下文
    private Intent intent;
    private Button btn_loginorregister,btn_quit;
    private EditText et_uid,et_pwd;
    private ProgressDialog pd_load; // ProgressDialog对象
    private SharedPreferences sp; //  SharedPreference对象
    private SharedPreferences.Editor editor; // SharedPreference.Editor对象

    private Boolean isVaildUid = false; // 标记uid输入是否合法
    private Boolean isVaildPwd = false; // 标记pwd输入是否合法
    private User loginUser = null; // 登录或注册成功后创建的User实例

    private String name,password;
    private boolean oldUser;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.loginorregister);
        mContext = this;

        initViews();
    }

    /**
     * Author; taoye
     * Description: 初始化控件
     */
    private void initViews(){
        btn_loginorregister = (Button) findViewById(R.id.btn_login_loginorregister);
        btn_quit = (Button) findViewById(R.id.btn_login_quit);
        et_uid = (EditText) findViewById(R.id.et_uid);
        et_pwd = (EditText) findViewById(R.id.et_pwd);

        sp = getSharedPreferences("User",Context.MODE_PRIVATE); // 初始化SharedPreference对象，私有模式
        editor = sp.edit(); // 获取Editor对象
        name = sp.getString("userName","#norecord");
        if(!"#norecord".equals(name)){
            btn_loginorregister.setText(getString(R.string.login_login)); // 判断SharedPreference中是否有userName的记录，若有则显示“登录”，否则显示“注册”
            et_uid.setText(name); // 将保存的userName显示在et_uid上
            isVaildUid = true; // 设置用户名格式为合法
            et_pwd.requestFocus(); // 初始光标在密码输入栏
        }

        btn_loginorregister.setOnClickListener(this);
        btn_quit.setOnClickListener(this);
        et_uid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!"".equals(et_uid.getText().toString())) {
                    if (et_uid.length() > 50) { // 登录名不得多于50个字符
                        et_uid.setError(Const.SetError.UID_NUMBER_ERROR);
                        isVaildUid = false;
                    }
                    // 登录名要满足正则表达式
                    if (!charSequence.toString().matches(Const.InputRegularExpr.REGULAR_UID)) {
                        et_uid.setError(Const.SetError.UID_FROMAT_ERROR);
                        isVaildUid = false;
                    }
                    if (et_uid.length() <= 50 && charSequence.toString().matches(Const.InputRegularExpr.REGULAR_UID)) {
                        et_uid.setError(null);
                        isVaildUid = true; // boolean值，标记登录名是否合法
                    }
                } else {
                    et_uid.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

        });
        et_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!"".equals(et_pwd.getText().toString())) {
                    if (et_pwd.length() < 6) {
                        et_pwd.setError(Const.SetError.PWD_NUMBER_ERROR);
                        isVaildPwd = false;
                    }
                    if (!charSequence.toString().matches(Const.InputRegularExpr.REGULAR_PWD)) {
                        et_pwd.setError(Const.SetError.PWD_FROMAT_ERROR);
                        isVaildPwd = false;
                    }
                    if (et_pwd.length() >= 6 && charSequence.toString().matches(Const.InputRegularExpr.REGULAR_PWD)) {
                        et_pwd.setError(null);
                        isVaildPwd = true;
                    }
                } else {
                    et_pwd.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        pd_load = new ProgressDialog(this); // 创建一个新的ProgressDialog
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.btn_login_loginorregister: // 登录/注册
                // 如果按钮上显示的是“登录”
                if(getString(R.string.login_login).equals(btn_loginorregister.getText().toString())){
                    loadProgressDialog("login");
                }else{
                    loadProgressDialog("register"); // 如果按钮上显示的是“注册”
                }
                break;
            case R.id.btn_login_quit: // 退出
                if(!"#norecord".equals(name)){ // 判断是否有userName记录
                    editor.putInt("loginState",0);
                    editor.commit();
                }
                intent = new Intent();
                intent.putExtra(Const.IntentMsg.MESSAGE,Const.IntentMsg.MSG_RETURN);
                setResult(Const.ResultCode.FROM_LOGINORREGISTER , intent);

                finish();
                break;
        }
    }

    @Override
    public void onBackPressed(){
        intent = new Intent();// setResult方法中携带的intent对象可以不使用显示Intent或则隐式Intent
//        intent = new Intent(mContext,MainScreen.class);
//        intent = new Intent("scos.intent.action.SCOSMAIN");
        intent.putExtra(Const.IntentMsg.MESSAGE,Const.IntentMsg.MSG_RETURN);
        setResult(Const.ResultCode.FROM_LOGINORREGISTER , intent);
        finish();
    }

    /**
     * Author: taoye
     * Description: 绘制一个ProgressDialog，持续2秒钟。同时完成对uid和pwd的验证
     */
    private void loadProgressDialog(String action){
        pd_load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd_load.setCancelable(false);
        pd_load.setCancelable(false);
        if(action.equals("login")){
            pd_load.setMessage("登录中...");
            pd_load.show();
            new Thread(new Runnable(){
                @Override
                public void run(){
                    try{
                        Thread.sleep(2000);
                        pd_load.dismiss();
                        if(isVaildPwd && isVaildUid){ // 登录名和密码都正确
                            name = et_uid.getText().toString();
                            password = et_pwd.getText().toString();
                            oldUser = true;
                            loginUser = new User(name,password,oldUser);

                            editor.putString("userName",name); // 通过Editor对象将userName写入SharedPrefrence
                            editor.putInt("loginState",1);
                            editor.commit(); // 将数据保存到xml文件中

                            intent = new Intent();
                            // 返回“登录成功”的信息给MainScreen
                            intent.putExtra(Const.IntentMsg.MESSAGE, Const.IntentMsg.MSG_LOGIN_SUCC);
                            // 返回一个User类实例loginUser
                            intent.putExtra(Const.IntentMsg.USER, loginUser);
                            setResult(Const.ResultCode.FROM_LOGINORREGISTER ,intent);
                            finish();

                            Looper.prepare();
                            Toast.makeText(LoginOrRegister.this,"登录成功",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }else {
                            Looper.prepare();
                            Toast.makeText(LoginOrRegister.this, "登录失败", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    } catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        else{
            pd_load.setMessage("注册中...");
            pd_load.show();
            new Thread(new Runnable(){
                @Override
                public void run(){
                    try{
                        Thread.sleep(2000);
                        pd_load.dismiss();
                        if(isVaildPwd && isVaildUid){ // 登录名和密码都正确
                            name = et_uid.getText().toString();
                            password = et_pwd.getText().toString();
                            oldUser = false;
                            loginUser = new User(name,password,oldUser);

                            editor.putString("userName",name); // 通过Editor对象将userName写入SharedPrefrence
                            editor.putInt("loginState",1);
                            editor.commit(); // 将数据保存到xml文件中

                            intent = new Intent();
                            // 返回“注册成功”的信息给MainScreen
                            intent.putExtra(Const.IntentMsg.MESSAGE, Const.IntentMsg.MSG_REGISTER_SUCC);
                            // 返回一个User类实例loginUser
                            intent.putExtra(Const.IntentMsg.USER, loginUser);
                            setResult(Const.ResultCode.FROM_LOGINORREGISTER ,intent);
                            finish();

                            Looper.prepare();
                            Toast.makeText(LoginOrRegister.this,"注册成功",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }else {
                            Looper.prepare();
                            Toast.makeText(LoginOrRegister.this, "注册失败", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    } catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
