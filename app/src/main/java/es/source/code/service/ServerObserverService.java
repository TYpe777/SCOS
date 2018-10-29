package es.source.code.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import java.lang.ref.WeakReference;

/**
 * Created by taoye on 2018/10/24.
 */
public class ServerObserverService extends Service {

    // 在主线程直接new Handler 使用的是主线程Looper.如果handler工作频率很高，会影响主线程的效率。
    // 所以某些UI操作 （对，ui操作）使用线程来执行。
    private MyHandler myHandler = new MyHandler(this);

    private final Messenger mMessenger = new Messenger(myHandler); // 服务自己的信使
//    private Messenger cMessenger; // 客户端的信使

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
        myHandler.removeCallbacksAndMessages(null); // Service销毁时，清除消息队列中未执行完的任务
    }

    /**
     * 声明一个静态的Handle内部类，并持有外部类的弱引用
     */
    private static class MyHandler extends Handler{
        private final WeakReference<ServerObserverService> mService;

        private MyHandler(ServerObserverService mService){
            this.mService = new WeakReference<ServerObserverService>(mService);
        }

        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case 0:
                    try{
                        Message message = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("content", "实时更新已启动");
                        message.setData(bundle);
                        // 通过msg.replyTo域获取客户端的信使对象
                        // 并使用这只信使发送消息给客户端，由客户端的Handler对象接收
                        (msg.replyTo).send(message);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try{
                        Message message = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("content", "实时更新已关闭");
                        message.setData(bundle);
                        (msg.replyTo).send(message);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
