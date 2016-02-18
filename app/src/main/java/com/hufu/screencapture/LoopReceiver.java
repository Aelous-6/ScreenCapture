package com.hufu.screencapture;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.util.Log;

/**
 * 通过AlarmManager重复挂起的broadcast receiver。
 * 它的主要功能是在唤醒后检查当前手机中的指定应用进程是否开启。
 * 如果开启了的话，就 通过MediaProjectionManager来创建MediaProjection从而达到录制屏幕的目的
 */
public class LoopReceiver extends BroadcastReceiver {
    private static final String TAG = LoopReceiver.class.getSimpleName();

    public LoopReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : activityManager.getRunningAppProcesses()
                ) {
            if (runningAppProcessInfo.processName.equals("com.hufu.seekbar")) {
                Intent screenCaptureIntent = mediaProjectionManager.createScreenCaptureIntent();
                screenCaptureIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(screenCaptureIntent);
                alarmManager.cancel(MainActivity.pendingIntent);
            }
        }
        Log.i(TAG, intent.getAction());
    }

}
