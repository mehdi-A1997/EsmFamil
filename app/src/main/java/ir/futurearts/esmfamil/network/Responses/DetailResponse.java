package ir.futurearts.esmfamil.network.Responses;

import ir.futurearts.esmfamil.module.ItemsM;

public class DetailResponse {
    private ItemsM uitem;
    private ItemsM oitem;

    public DetailResponse(ItemsM uitem, ItemsM oitem) {
        this.uitem = uitem;
        this.oitem = oitem;
    }

    public DetailResponse() {
    }

    public ItemsM getUitem() {
        return uitem;
    }

    public void setUitem(ItemsM uitem) {
        this.uitem = uitem;
    }

    public ItemsM getOitem() {
        return oitem;
    }

    public void setOitem(ItemsM oitem) {
        this.oitem = oitem;
    }
}
