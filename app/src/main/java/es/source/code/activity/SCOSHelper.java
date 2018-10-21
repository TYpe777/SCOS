package es.source.code.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

import es.source.code.adapters.NavigationAdapter;
import es.source.code.utils.Const;

/**
 * Created by taoye on 2018/10/15.
 */
public class SCOSHelper extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private Context mContext;
    private Intent intent;
    private GridView gv_helper;

    private int[] images = {R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
    private String[] titles = {"用户使用协议","关于系统","电话人工帮助","短信帮助","邮件帮助"};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoshelper);
        mContext = SCOSHelper.this;

        myRequestPermissions(); // 申请权限（电话，短信）。Android6.0增加了动动态权限
        initViews();
    }

    private void initViews(){
        gv_helper = (GridView) findViewById(R.id.gridView_helper);

        NavigationAdapter helperAdapter = new NavigationAdapter(mContext,images,titles);
        gv_helper.setAdapter(helperAdapter);
        gv_helper.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView,View view,int i,long l){
        if(adapterView.getId() == R.id.gridView_helper){
            switch(titles[i]){
                case "电话人工帮助":
                    doCall("5554");
                    break;
                case "短信帮助":
                    sendMessage("13856255775");
                    break;
                case "邮件帮助":
                    intent = new Intent(mContext,SendMail.class);
                    startActivity(intent);
                    break;
                default:
                    Toast.makeText(mContext,"点击了第"+ i +"个按钮",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onBackPressed(){
        intent = new Intent();
        setResult(Const.ResultCode.FROM_SCOSHELPER,intent);
        finish();
    }

    /**
     * 动态申请权限
     */
    private void myRequestPermissions(){
        String[] permissions = {"android.permission.CALL_PHONE","android.permission.SEND_SMS"};
        ActivityCompat.requestPermissions(SCOSHelper.this, permissions, 0x11); // 不能用mContext
    }

    /**
     * 申请权限的回调函数
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0x11){
            // 如果请求被拒绝，grantResults就会是空
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.i("CMCC","权限被允许");
            }else{
                Log.i("CMCC","权限被拒绝");
            }
        }
    }

    /**
     * 使用Intent打电话（Service）
     * @param phoneNum
     */
    private void doCall(String phoneNum){
        intent  = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNum));
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    /**
     * 使用SmsManager发送短信，并且监听是否发送成功
     * @param phoneNum
     */
    private void sendMessage(String phoneNum){
        String msg = "test SCOS helper";
        SmsManager smsManager = SmsManager.getDefault();
        // 处理系统返回的发送状态
        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        intent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(mContext,0,intent,0);
        // 注册发送短信的广播接收者
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()) {
                    case Activity.RESULT_OK: // 发送成功
                        Toast.makeText(mContext, "短信发送成功!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(mContext, "短信发送失败!",Toast.LENGTH_SHORT).show();
                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE: // 普通错误
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF: // 无线广播被明确关闭
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE: // 当前服务不可用
//                        break;
                }
            }
        },new IntentFilter(SENT_SMS_ACTION));
        // 短信长度大于70时自动拆分
        List<String> divideContents = smsManager.divideMessage(msg);
        for(String text : divideContents){
            smsManager.sendTextMessage(phoneNum,null,text,sentPI,null);
        }
    }
}
