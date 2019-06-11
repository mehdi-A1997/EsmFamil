package ir.futurearts.esmfamil.interfaces;

import ir.futurearts.esmfamil.module.GameM;

public interface GameInterface {
    void Play(GameM g , int pos);
    void ManageRequest(GameM g, int pos);
    void CompleteDetail(GameM g);
}
