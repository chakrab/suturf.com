package zen.proverbs.beans;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.micronaut.core.annotation.Introspected;

@Introspected
@JsonRootName("response")
public class ResponseBean {
    private int    responseCode;
    private String responseText;

    public ResponseBean() {

    }

    public ResponseBean(final int code, final String text) {
        this.responseCode = code;
        this.responseText = text;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
}
