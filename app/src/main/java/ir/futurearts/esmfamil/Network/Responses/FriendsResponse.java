package ir.futurearts.esmfamil.Network.Responses;

import java.util.List;

import ir.futurearts.esmfamil.Module.UserM;

public class FriendsResponse {
    private boolean error;
    private String message;
    private List<UserM> users;


    public FriendsResponse(boolean error, String message, List<UserM> users) {
        this.error = error;
        this.message = message;
        this.users = users;
    }

    public FriendsResponse() {
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

    public List<UserM> getUsers() {
        return users;
    }

    public void setUsers(List<UserM> users) {
        this.users = users;
    }
}
