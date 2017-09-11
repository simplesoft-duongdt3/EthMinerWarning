package duongmh3.bittrexmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.OnClick;
import duongmh3.bittrexmanager.service.MinerCheckInfoIntentService;
import duongmh3.bittrexmanager.service.ServiceUtil;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    CheckBox cbPlaySound;
    CheckBox cbVibrate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cbPlaySound = (CheckBox) findViewById(R.id.cbPlaySound);
        cbPlaySound.setOnClickListener(view -> ServiceUtil.setPlaySoundWhenWarning(cbPlaySound.isChecked()));
        cbVibrate = (CheckBox) findViewById(R.id.cbVibrate);
        cbVibrate.setOnClickListener(view -> ServiceUtil.setVibrateWhenWarning(cbVibrate.isChecked()));

        View btStartWarningService = findViewById(R.id.btStartWarningUiService);
        btStartWarningService.setOnClickListener(view -> ServiceUtil.startServiceWarning(MainActivity.this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cbPlaySound.setChecked(ServiceUtil.isPlaySoundWhenWarning());
        cbVibrate.setChecked(ServiceUtil.isVibrateWhenWarning());
    }
}
