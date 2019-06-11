package ir.futurearts.esmfamil.network.Responses;

import ir.futurearts.esmfamil.module.GameM;

public class CreateGameResponse {
    private boolean error;
    private String message;
    private GameM game;

    public CreateGameResponse(boolean error, String message, GameM game) {
        this.error = error;
        this.message = message;
        this.game = game;
    }

    public CreateGameResponse() {
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

    public GameM getGame() {
        return game;
    }

    public void setGame(GameM game) {
        this.game = game;
    }
}
