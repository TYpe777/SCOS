package es.source.code.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

import es.source.code.activity.FoodDetailed;
import es.source.code.activity.MainScreen;
import es.source.code.activity.R;
import es.source.code.model.Food;
import es.source.code.utils.Const;
import es.source.code.utils.HttpURLConnectionUtil;

/**
 * @author taoye
 * @date 2018/10/30.
 * @classname UpdateService.java
 * @description 通过IntentService实现发送状态栏通知
 */
public class UpdateService extends IntentService {
    private final static String TAG = "UpdateService";
    private final static int REQUEST_CODE = 0; // 通知的请求码
    private final static int NOTIFICATION_ID = 1; // 通知的ID
    private final static String ACTION_BUTTON = "es.source.code.service.clear"; // 启动广播接收器的Action

    // IntentService的子类的构造方法不能有参数，而且必须调用弗雷德构造方法
    public UpdateService(){
        super("UpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        while(true){
            // Math.random()*(100-1+1) 表示产生1到100的int型随机数
            int flag = (int)(Math.random() * (100 - 1 + 1));
            Log.i(TAG, "-" + flag);
            if(flag > 95){ // 产生随机数，模拟更新通知
//                notifyUpdate();
                notifyUpdateFromHttp();
            }
            try{
                Thread.sleep(500);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试用的菜品对象
     * 作为新更新的菜品
     * 用于作业5的开机自启动
     */
    private final Food food = new Food(2130837605, "糯米团子", (float) 25.0, "糯米的团子", 10);
    /**
     * 发送菜品更新的通知
     * 使用默认的通知
     * 作业5
     */
    private void notifyUpdate(){
        Notification.Builder notiBuilder = new Notification.Builder(UpdateService.this);
        NotificationManager notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = notiBuilder.build();

        notiBuilder.setContentTitle("菜品更新");
        notiBuilder.setContentText("新品上架：" + food.getName() + ",价格：" + food.getPrice() + "备注：" + food.getDescription());
        notiBuilder.setSmallIcon(R.drawable.logo);
        notiBuilder.setWhen(System.currentTimeMillis());

        // 通知的点击响应事件，自动撤销通知
        Intent intent = new Intent(UpdateService.this, FoodDetailed.class);
        // 将菜品对象传递给FoodDetailed
        intent.putExtra(Const.IntentMsg.FOOD, food);
        PendingIntent nextPage_pi = PendingIntent.getActivity(UpdateService.this, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notiBuilder.setContentIntent(nextPage_pi);
        notiBuilder.setAutoCancel(true);

        notification.defaults = Notification.DEFAULT_VIBRATE;

        notiManager.notify(NOTIFICATION_ID, notification); // 通知ID相同，则会更新通知，而不是产生新通知
    }

    /**
     * 发送菜品更新的通知
     * 使用自定义的通知
     * 作业6
     */
    private void notifyUpdateFromHttp(){
        // 获取更新的菜品的信息
        List<Food> foodList = HttpURLConnectionUtil.getUpdateByHttp(Const.URL.FOODUPDATE_URL);
        StringBuilder content = new StringBuilder(); // 通知的内容
        content.append("新品上架");
        for(int i = 0; i < foodList.size(); i++){
            content.append(foodList.get(i).getName()).append(":").append(foodList.get(i).getStock()).append("个;");
        }

        Notification.Builder notiBuilder = new Notification.Builder(UpdateService.this);
        NotificationManager notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = notiBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR; // 设置点击不撤销通知
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);

        notiBuilder.setSmallIcon(R.drawable.logo);
        notiBuilder.setContent(remoteViews);
//        notiBuilder.setStyle(new Notification.BigTextStyle()); // 通知的大视图模式
        notiBuilder.setWhen(System.currentTimeMillis());

        remoteViews.setImageViewResource(R.id.iv_noti_image, R.drawable.logo);
        remoteViews.setTextViewText(R.id.tv_noti_title, "菜品更新");
        remoteViews.setTextViewText(R.id.tv_noti_content, content.toString());
        // 利用BroadcastReceiver为“清除”按钮添加点击事件
        Intent clearIntent = new Intent();
        clearIntent.setAction(ACTION_BUTTON);
        PendingIntent clearIntent_pi = PendingIntent.getBroadcast(UpdateService.this, 1, clearIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_noti_clear, clearIntent_pi);

        // 通知的点击响应事件，自动撤销通知
        Intent intent = new Intent(UpdateService.this, MainScreen.class);
        intent.putExtra(Const.IntentMsg.MESSAGE, Const.IntentMsg.MSG_FROM_FOODUPDATE_NOTIFICATION);
        PendingIntent nextPage_pi = PendingIntent.getActivity(UpdateService.this, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notiBuilder.setContentIntent(nextPage_pi);
        notiBuilder.setAutoCancel(true);

        notiManager.notify(NOTIFICATION_ID, notification);
        // 通知时发出系统提示音
        MediaPlayer mediaPlayer = new MediaPlayer();
        try{
            // 获取系统的通知的铃声的Uri
            Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(UpdateService.this, RingtoneManager.TYPE_NOTIFICATION);
            mediaPlayer.setDataSource(UpdateService.this, ringtoneUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            mediaPlayer.prepare();
        }catch(Exception e){
            e.printStackTrace();
        }
        mediaPlayer.setVolume(1f, 1f);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    /**
     * 启动广播接收器
     * @param intent
     * @param i
     */
    @Override
    public void onStart(Intent intent, int i){
        super.onStart(intent, i);
        // 动态注册广播接收器，绑定意图筛选器
        ButtonClickReceiver buttonClickReceiver = new ButtonClickReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BUTTON);
        registerReceiver(buttonClickReceiver, intentFilter);
    }

    /**
     * Notification中按钮“清除”的点击事件的广播接收器
     */
    public class ButtonClickReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            if(intent.getAction().equals(ACTION_BUTTON)){
                NotificationManager notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                //移除标记为id的通知 (只是针对当前Context下的所有Notification)
                notiManager.cancel(NOTIFICATION_ID);
                //移除所有通知
                //notificationManager.cancelAll();
            }
        }
    }
}
