package duongmh3.bittrexmanager.common;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by admin on 7/11/17.
 */
@Setter
@Getter
public class DataResult<T> {
    private T data;
    private boolean success;
    private String errorMsg;

    public DataResult(T data) {
        this.data = data;
    }
}
