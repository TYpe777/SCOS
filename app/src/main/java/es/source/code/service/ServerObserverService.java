package es.source.code.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import java.io.Serializable;

import es.source.code.utils.HotdishesProvider;

/**
 * @author taoye
 * @date 2018/10/24.
 * @classname ServerObserverService.java
 * @description 实现菜单实时更新的服务
 */
public class ServerObserverService extends Service {

    // 在主线程直接new Handler 使用的是主线程Looper.如果handler工作频率很高，会影响主线程的效率。
    // 所以某些UI操作 （对，ui操作）使用线程来执行。
    private cMessengerHandler mHandler = new cMessengerHandler();

    private final Messenger mMessenger = new Messenger(mHandler); // 服务自己的信使
    private Messenger cMessenger; // 客户端的信使

    private boolean exit = false; // 判断是否退出子线程中的循环

    private Runnable runnable = new Runnable() { // 创建一个线程执行耗时任务
        @Override
        public void run() {
            /**
             * 值为0 或1
             * 标记读的是哪一个Json文件中的菜单。0表示foods.json，1表示simulation.json
             * 每300ms切换一次，以实现模拟实时更新
             */
            int fileno  = 1;

            Message message = Message.obtain(); // 要发送的消息对象
            while(!exit){
                try{
                    Bundle bundle = new Bundle();

                    // 这段代码是为了模拟服务器实时更新效果
                    {
                        if(fileno == 0){
                            bundle.putSerializable("Hotdishes", (Serializable) HotdishesProvider.getList(getApplicationContext()));
                        }
                        else {
                            bundle.putSerializable("Hotdishes", (Serializable)HotdishesProvider.getList_simu(getApplicationContext()));
                        }
                        fileno = 1 - fileno; // 下一秒钟换一个Json文件读取
                    }

                    message.what = 10;
                    message.setData(bundle);
                    // 使用客户端信使发送消息给客户端，由客户端的Handler对象接收
                    cMessenger.send(message);
                    Thread.sleep(1000); // 休眠1s，作业要求休眠300ms
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 客户端与服务端绑定成功后回调
     * RemoteService通过Messenger对象返回binder
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent){
        // 返回服务信使的binder，客户端通过binder得到服务端的信使
        return mMessenger.getBinder();
    }

    /**
     * 执行耗时任务，startedService的生命周期中才会回调
     * boundService生命周期中不会回调
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 当最后一个客户端(Activity)解绑后，回调这个方法
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent){
        return super.onUnbind(intent);
    }

    /**
     * 做资源的释放
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        exit = true; // 退出子线程中的循环
        mHandler.removeCallbacksAndMessages(null); // Service销毁时，清除消息队列中未执行完的任务
    }

    // 自定义Handler
    private class cMessengerHandler extends Handler{

        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case 0:
                    exit = true; // 退出子线程中的循环
                    break;
                case 1:
                    exit = false;
                    cMessenger = msg.replyTo; // 通过msg.replyTo域获取客户端的信使对象
                    new Thread(runnable).start(); // 开启线程
                    // 销毁线程
                    mHandler.removeCallbacks(runnable);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
