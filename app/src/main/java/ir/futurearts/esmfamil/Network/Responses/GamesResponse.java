package ir.futurearts.esmfamil.Network.Responses;

import java.util.LinkedList;
import java.util.List;

import ir.futurearts.esmfamil.Module.GameM;

public class GamesResponse {
    private boolean error;
    private String message;
    private LinkedList<GameM> games;

    public GamesResponse(boolean error, String message, LinkedList<GameM> games) {
        this.error = error;
        this.message = message;
        this.games = games;
    }

    public GamesResponse() {
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

    public LinkedList<GameM> getGames() {
        return games;
    }

    public void setGames(LinkedList<GameM> games) {
        this.games = games;
    }
}
