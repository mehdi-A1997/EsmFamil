package ir.futurearts.esmfamil.Interface;

import ir.futurearts.esmfamil.Module.GameM;

public interface GameInterface {
    void Play(GameM g , int pos);
    void ManageRequest(GameM g, int pos);
    void CompleteDetail(GameM g);
}
