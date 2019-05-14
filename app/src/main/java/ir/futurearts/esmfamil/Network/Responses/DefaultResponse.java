package ir.futurearts.esmfamil.Network.Responses;

import org.json.JSONException;
import org.json.JSONObject;

public class DefaultResponse {

    private boolean error;
    private String message;

    public DefaultResponse(String body) {
        try {
            JSONObject object= new JSONObject(body);
            error= object.getBoolean("error");
            message= object.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
