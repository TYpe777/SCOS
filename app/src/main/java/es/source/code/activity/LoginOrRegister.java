package es.source.code.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.source.code.utils.Const;
import es.source.code.utils.InputRegularExpr;

/**
 * Author: taoye
 * Classname: LoginOrRegister.java
 * Description: 登录和注册
 * Date: 2018/9/26.
 */

public class LoginOrRegister extends ActionBarActivity implements OnClickListener {

    private Button btn_login,btn_quit;
    private EditText et_uid,et_pwd;
    private TextWatcher tw_uid,tw_pwd;
    private ProgressDialog pd_load;
    private Intent intent;

    private Boolean isVaildUid = false; // 标记uid输入是否合法
    private Boolean isVaildPwd = false; // 标记pwd输入是否合法
    private String beforeContent;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.loginorregister_acti);

        init();
    }

    /**
     * Author; taoye
     * Description: 初始化控件
     */
    private void init(){
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_quit = (Button) findViewById(R.id.btn_login_quit);
        et_uid = (EditText) findViewById(R.id.et_uid);
        et_pwd = (EditText) findViewById(R.id.et_pwd);

        pd_load = new ProgressDialog(this); // 创建一个新的ProgressDialog

        btn_login.setOnClickListener(this);
        btn_quit.setOnClickListener(this);

        // et_uid 的监听器
        tw_uid = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 当对“登录名”EditText输入时，移除对其他EditText的监听，以避免死循环。
                et_pwd.removeTextChangedListener(tw_pwd);

                if(!"".equals(et_uid.getText().toString())) {
                    if(et_uid.length() > 50){
                        et_uid.setError(Const.SetError.UID_NUMBERERROR);
                        isVaildUid = false;
                    }
                    if (!charSequence.toString().matches(InputRegularExpr.REGULAR_UID)){
                        et_uid.setError(Const.SetError.UID_FROMATERROR);
                        isVaildUid = false;
                    }
                    if(et_uid.length() <= 50 && charSequence.toString().matches(InputRegularExpr.REGULAR_UID)){
                        et_uid.setError(null);
                        isVaildUid = true;
                    }
                } else {
                    et_uid.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //  完成对“登录名”EditText的输入后，为其他EditText恢复监听。
                et_pwd.addTextChangedListener(tw_pwd);
            }
        };

        // et_pwd的监听器
        tw_pwd = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 当对“登录密码”EditText输入时，移除对其他EditText的监听，以避免死循环。
                et_uid.removeTextChangedListener(tw_uid);

                if(!"".equals(et_pwd.getText().toString())) {
                    if(et_pwd.length() < 6){
                        et_pwd.setError(Const.SetError.PWD_NUMBERERROR);
                        isVaildPwd = false;
                    }
                    if (!charSequence.toString().matches(InputRegularExpr.REGULAR_PWD)){
                        et_pwd.setError(Const.SetError.PWD_FROMATERROR);
                        isVaildPwd = false;
                    }
                    if(et_pwd.length() >= 6 && charSequence.toString().matches(InputRegularExpr.REGULAR_PWD)){
                        et_pwd.setError(null);
                        isVaildPwd = true;
                    }
                } else {
                    et_pwd.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 完成对“登录密码”EditText的输入后，为其他EditText恢复监听。
                et_uid.addTextChangedListener(tw_uid);
            }
        };

        // 将监听器绑定到相应的控件上
        et_uid.addTextChangedListener(tw_uid);
        et_pwd.addTextChangedListener(tw_pwd);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.btn_login:
                loadProgressDialog();
                break;
            case R.id.btn_login_quit:
                intent = new Intent("scos.intent.action.SCOSMAIN");
                intent.putExtra(Const.IntentMsg.MESSAGE,Const.IntentMsg.RETURN);
                setResult(Const.RespondCode.RETURN , intent);
                finish();
                break;
        }
    }

    /**
     * Author: taoye
     * Description: 绘制一个ProgressDialog，持续2秒钟。同时完成对uid和pwd的验证
     */
    private void loadProgressDialog(){
        pd_load.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd_load.setCancelable(false);
        pd_load.setCancelable(false);
        pd_load.setMessage("登录中...");
        pd_load.show();

        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    Thread.sleep(2000);
                    pd_load.dismiss();
                    if(isVaildPwd && isVaildUid){
                        intent = new Intent("scos.intent.action.SCOSMAIN");
                        intent.putExtra(Const.IntentMsg.MESSAGE, Const.IntentMsg.LOGIN_SUCC);
                        setResult(Const.RespondCode.LOGINSUCC ,intent);
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
}
