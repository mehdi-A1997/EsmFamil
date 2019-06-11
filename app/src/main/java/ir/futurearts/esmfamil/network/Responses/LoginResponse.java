package ir.futurearts.esmfamil.network.Responses;

import ir.futurearts.esmfamil.module.UserM;

public class LoginResponse {

    private boolean error;
    private String message;
    private UserM user;

    public LoginResponse() {
    }

    public LoginResponse(boolean error, String message, UserM user) {
        this.error = error;
        this.message = message;
        this.user = user;
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

    public UserM getUser() {
        return user;
    }

    public void setUser(UserM user) {
        this.user = user;
    }
}
