package ir.futurearts.esmfamil.interfaces;

import ir.futurearts.esmfamil.module.GameM;

public interface GameRequestInterface {
    void Accept(GameM g, int pos);
    void Decline(GameM g, int pos);
}
