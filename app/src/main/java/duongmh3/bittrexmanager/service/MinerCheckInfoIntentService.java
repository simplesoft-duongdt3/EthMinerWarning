package duongmh3.bittrexmanager.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;

import duongmh3.bittrexmanager.R;
import duongmh3.bittrexmanager.model.MarketSummaryModel;
import duongmh3.bittrexmanager.model.WarningResultModel;
import duongmh3.bittrexmanager.model.WarningSettingModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MinerCheckInfoIntentService extends IntentService {
    @NonNull
    private static final OkHttpClient client = new OkHttpClient();

    public MinerCheckInfoIntentService() {
        super("MinerCheckInfoIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        WarningSettingModel warningSettingModel = ServiceUtil.loadConfig();

        WarningResultModel warningResultModel = new WarningResultModel();
        warningResultModel.setResult(WarningResultModel.Result.ERROR);
        warningResultModel.setTimeStart(System.currentTimeMillis());
        warningResultModel.setWallet(warningSettingModel.getWallet());
        if (intent != null) {
            Request request = new Request.Builder()
                    .url("https://api.ethermine.org/miner/" + warningSettingModel.getWallet() + " /currentStats")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    MarketSummaryModel marketSummaryModel = new Gson().fromJson(response.body().charStream(), MarketSummaryModel.class);
                    if (marketSummaryModel.isSuccess()) {
                        int minNumberWorker = warningSettingModel.getMinNumberWorker();
                        int minHashRate = warningSettingModel.getMinHashRate();

                        MarketSummaryModel.Data data = marketSummaryModel.getData();
                        boolean isWarning = data.getActiveWorkers() < minNumberWorker || Double.parseDouble(data.getReportedHashrate()) < minHashRate;
                        if (isWarning) {
                            warningResultModel.setResult(WarningResultModel.Result.WARNING);
                        } else {
                            warningResultModel.setResult(WarningResultModel.Result.NORMAL);
                        }
                    }
                }
            } catch (Exception e) {
                warningResultModel.setErrorLog(e.getMessage());
                warningResultModel.setResult(WarningResultModel.Result.ERROR);
            }

            if (warningResultModel.getResult() != WarningResultModel.Result.NORMAL) {
                warningUser();
            }

            warningResultModel.setTimeEnd(System.currentTimeMillis());
            sendWarningBroadcast(warningResultModel);

            SyncWakefulReceiver.completeWakefulIntent(intent);
        }
    }

    private void warningUser() {
        if (ServiceUtil.isPlaySoundWhenWarning()) {
            playWarningShow();
        }

        if (ServiceUtil.isVibrateWhenWarning()) {
            vibrate();
        }
    }

    private void playWarningShow() {
        try {
            MediaPlayer player = MediaPlayer.create(this, R.raw.warning);
            player.start();
            player.setOnCompletionListener(MediaPlayer::release);
            player.setLooping(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void vibrate() {
        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long pattern[] = {60, 120, 180, 240, 300, 360, 420, 480};

        v.vibrate(pattern, 5);
    }

    private void sendWarningBroadcast(WarningResultModel result) {
        Intent intent = new Intent("WarningBroadcast");
        intent.putExtra("result", result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
