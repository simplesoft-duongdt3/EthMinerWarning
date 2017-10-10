package duongmh3.bittrexmanager.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;

import duongmh3.bittrexmanager.model.WarningSettingModel;
import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by doanthanhduong on 2/3/17.
 */

public class ServiceUtil {
    private static final int REQUEST_CODE_START_SERVICE = 1000;
    //private static final long INTERVAL_IN_MILLISECOND = 5 * 60 * 1000L;
    private static final long INTERVAL_IN_MILLISECOND = 20 * 1000L;

    public static void startServiceWarning(Context context) {
        stopServiceCheckWarning(context);
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
        context.startService(new Intent(context, WarningChatHeadService.class));
    }

    public static WarningSettingModel loadConfig() {
        WarningSettingModel configWarning = Paper.book().read("config_warning");
        if (configWarning == null) {
            configWarning = new WarningSettingModel();
            configWarning.setWallet("e515DE92b0f8BF6055C13e12693402430BCEa764");
            configWarning.setMinHashRate(650 * 1000000);
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

    public static int getNumNetworkError() {
        return Paper.book().read("config_num_net_error", 0);
    }

    public static void setNumNetworkError(int numNetworkError) {
        Paper.book().write("config_num_net_error", numNetworkError);
    }

    public interface CallbackGetConfig {
        void onDoneEvent(boolean success);
    }

    @NonNull
    private static final OkHttpClient client = new OkHttpClient();

    public static void getConfigWarningFromCloudAsync(@NonNull final CallbackGetConfig callbackGetConfig) {
        Request request = new Request.Builder()
                .url("https://shebeauty.com.vn/eth_warning_config.txt")
                .build();

        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callbackGetConfig.onDoneEvent(false);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        WarningSettingModel warningSettingModel = new Gson().fromJson(response.body().charStream(), WarningSettingModel.class);
                        Paper.book().write("config_warning", warningSettingModel);
                        callbackGetConfig.onDoneEvent(true);
                    } else {
                        callbackGetConfig.onDoneEvent(false);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callbackGetConfig.onDoneEvent(false);
        }
    }
}
