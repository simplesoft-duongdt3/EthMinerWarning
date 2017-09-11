package duongmh3.bittrexmanager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by admin on 6/25/17.
 */
@Getter
@Setter
public class WarningSettingModel implements Serializable {
    private String wallet;
    private int minNumberWorker;
    private int minHashRate;
}
