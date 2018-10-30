package es.source.code.br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import es.source.code.service.UpdateService;

/**
 * @author taoye
 * @date 2018/10/30.
 * @classname DeviceStartedListener.java
 * @description 广播接收器，实现开机自启动UpdateService服务
 */
public class DeviceStartedListener extends BroadcastReceiver {
    private final static String TAG = "DeviceStartedListener";

    @Override
    public void onReceive(Context context, Intent intent){
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Intent intent_service = new Intent(context, UpdateService.class);
            context.startService(intent_service);
            Log.i(TAG, "开机自动启动服务成功...");
        }
    }
}
