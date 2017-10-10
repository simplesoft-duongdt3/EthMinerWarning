package duongmh3.bittrexmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.OnClick;
import duongmh3.bittrexmanager.model.WarningSettingModel;
import duongmh3.bittrexmanager.service.MinerCheckInfoIntentService;
import duongmh3.bittrexmanager.service.ServiceUtil;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    CheckBox cbPlaySound;
    CheckBox cbVibrate;
    TextView tvConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvConfig = (TextView) findViewById(R.id.tvConfig);
        cbPlaySound = (CheckBox) findViewById(R.id.cbPlaySound);
        cbPlaySound.setOnClickListener(view -> ServiceUtil.setPlaySoundWhenWarning(cbPlaySound.isChecked()));
        cbVibrate = (CheckBox) findViewById(R.id.cbVibrate);
        cbVibrate.setOnClickListener(view -> ServiceUtil.setVibrateWhenWarning(cbVibrate.isChecked()));

        View btStartWarningService = findViewById(R.id.btStartWarningUiService);
        btStartWarningService.setOnClickListener(view -> ServiceUtil.startServiceWarning(MainActivity.this));
        View btUpdateConfigWarning = findViewById(R.id.btUpdateConfigWarning);
        btUpdateConfigWarning.setOnClickListener(view -> ServiceUtil.getConfigWarningFromCloudAsync(success -> runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    displayConfig();
                    Toasty.success(getApplicationContext(), "Lấy cấu hình thành công.", Toast.LENGTH_SHORT, true).show();
                    ServiceUtil.startServiceWarning(MainActivity.this);
                } else {
                    Toasty.error(getApplicationContext(), "Lấy cấu hình chưa thành công. Vui lòng thử lại.", Toast.LENGTH_SHORT, true).show();
                }
            }
        })));
    }

    void displayConfig() {
        WarningSettingModel warningSettingModel = ServiceUtil.loadConfig();
        tvConfig.setText("Số lượng máy: " + warningSettingModel.getMinNumberWorker() + "\nSản lượng thấp nhất: " + warningSettingModel.getMinHashRate() / 1000000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayConfig();
        cbPlaySound.setChecked(ServiceUtil.isPlaySoundWhenWarning());
        cbVibrate.setChecked(ServiceUtil.isVibrateWhenWarning());
    }
}
