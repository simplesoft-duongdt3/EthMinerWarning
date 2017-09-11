package duongmh3.bittrexmanager.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by admin on 6/24/17.
 */
@Getter
@Setter
public class MarketSummaryModel implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;

    public boolean isSuccess() {
        return "ok".equalsIgnoreCase(status);
    }

    @Getter
    @Setter
    public static class Data {
        @SerializedName("time")
        @Expose
        private long time;
        @SerializedName("lastSeen")
        @Expose
        private long lastSeen;
        @SerializedName("reportedHashrate")
        @Expose
        private String reportedHashrate;
        @SerializedName("currentHashrate")
        @Expose
        private String currentHashrate;
        @SerializedName("validShares")
        @Expose
        private String validShares;
        @SerializedName("invalidShares")
        @Expose
        private String invalidShares;
        @SerializedName("staleShares")
        @Expose
        private String staleShares;
        @SerializedName("averageHashrate")
        @Expose
        private String averageHashrate;
        @SerializedName("activeWorkers")
        @Expose
        private int activeWorkers;
        @SerializedName("unpaid")
        @Expose
        private String unpaid;
        @SerializedName("unconfirmed")
        @Expose
        private String unconfirmed;
        @SerializedName("coinsPerMin")
        @Expose
        private String coinsPerMin;
        @SerializedName("usdPerMin")
        @Expose
        private String usdPerMin;
        @SerializedName("btcPerMin")
        @Expose
        private String btcPerMin;
    }
}