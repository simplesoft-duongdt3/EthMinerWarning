package duongmh3.bittrexmanager.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import duongmh3.bittrexmanager.model.WarningSettingModel;
import io.paperdb.Paper;

/**
 * Created by doanthanhduong on 2/3/17.
 */

public class ServiceUtil {
    private static final int REQUEST_CODE_START_SERVICE = 1000;
    private static final long INTERVAL_IN_MILLISECOND = 5 * 60 * 1000L;

    public static void startServiceWarning(Context context) {
        startServiceUiWarning(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = createIntentStartServiceCheckWarning(context);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), INTERVAL_IN_MILLISECOND, pendingIntent);
    }

    public static void stopServiceCheckWarning(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = createIntentStartServiceCheckWarning(context);
        alarmManager.cancel(pendingIntent);
    }

    private static PendingIntent createIntentStartServiceCheckWarning(Context context) {
        Intent intent = new Intent(context, SyncWakefulReceiver.class);
        return PendingIntent.getBroadcast(context, REQUEST_CODE_START_SERVICE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private static void startServiceUiWarning(Context context) {
        context.stopService(new Intent(context, WarningChatHeadService.class));
        context.startService(new Intent(context, WarningChatHeadService.class));
    }

    public static WarningSettingModel loadConfig() {
        WarningSettingModel configWarning = Paper.book().read("config_warning");
        if (configWarning == null) {
            configWarning = new WarningSettingModel();
            configWarning.setWallet("e515DE92b0f8BF6055C13e12693402430BCEa764");
            configWarning.setMinHashRate(650);
            configWarning.setMinNumberWorker(5);
        }
        return configWarning;
    }

    public static boolean isPlaySoundWhenWarning() {
        return Paper.book().read("config_sound_warning", true);
    }

    public static void setPlaySoundWhenWarning(boolean playSound) {
        Paper.book().write("config_sound_warning", playSound);
    }
    public static boolean isVibrateWhenWarning() {
        return Paper.book().read("config_vibrate_warning", true);
    }

    public static void setVibrateWhenWarning(boolean vibrate) {
        Paper.book().write("config_vibrate_warning", vibrate);
    }
}
