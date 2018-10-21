package es.source.code.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import es.source.code.utils.MailUtil;

/**
 * @author taoye
 * @date 2018/10/18.
 * @classname SendMail.java
 * @description 编辑发送帮助邮件的界面
 */
public class SendMail extends AppCompatActivity {

    private Context mContext;

    private EditText et_subject,et_content;
    private Button btn_send;

    private Runnable runnable;
    private SendMailHandler sendMailHandler; // 自定义Handler实例

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_mail);
        mContext = SendMail.this;

        initViews();
    }

    private void initViews(){
        et_subject = (EditText) findViewById(R.id.et_sendmail_subject);
        et_content = (EditText) findViewById(R.id.et_sendmail_content);
        btn_send = (Button) findViewById(R.id.btn_sendmail_send);

        sendMailHandler = new SendMailHandler(SendMail.this);
        runnable = new Runnable() {
            @Override
            public void run() {
                boolean b = MailUtil.autoSendMail(et_subject.getText().toString(),et_content.getText().toString());
                // 从全局池中返回一个message实例，避免多次创建message（加new Message）
                Message msg = Message.obtain();
//                msg.obj = object //object是某个类对象
                if(b){ // 发送成功
                    msg.what = 1;
                }else{ // 发送失败
                    msg.what = 0;
                }
                sendMailHandler.sendMessage(msg);
            }
        };

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(runnable).start();
            }
        });
    }

    /**
     * 为了避免handler造成的内存泄漏
     * 1.使用静态的handler，对外部类不保持对象的引用
     * 2.但Handler需要与Activity通信，所以需要增加一个对Activity的弱引用
     */
    private static class SendMailHandler extends Handler{
        private final WeakReference<Activity> mActivityReference;

        SendMailHandler(Activity activity){
            this.mActivityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            // 获取弱引用队列中的activity
            SendMail activity_sendmail = (SendMail) mActivityReference.get();
            switch(msg.what){// 获取消息，更新UI
                case 1:
                    Toast.makeText(activity_sendmail,"求助邮件发送成功",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(activity_sendmail,"求助邮件发送失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        // 避免activity销毁时，messageQueue中的消息未处理完，故此时应将对应的message给清除出队列
        sendMailHandler.removeCallbacks(runnable);// 清除runnable对应的message
        // myhandler.removeMessage(what) // 消除what对应的message
    }
}
