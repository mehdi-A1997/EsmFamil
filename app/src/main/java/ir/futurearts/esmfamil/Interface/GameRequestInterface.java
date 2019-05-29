package ir.futurearts.esmfamil.Interface;

import ir.futurearts.esmfamil.Module.GameM;

public interface GameRequestInterface {
    void Accept(GameM g, int pos);
    void Decline(GameM g, int pos);
}
