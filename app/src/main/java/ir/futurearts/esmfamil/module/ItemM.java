package ir.futurearts.esmfamil.module;

import java.io.Serializable;

public class ItemM implements Serializable {
    private String name;
    private int active;

    public ItemM(String name, boolean active) {
        this.name = name;
        if(active)
            this.active= 0;
        else
            this.active= -1;
    }

    public ItemM(String name, int active) {
        this.name = name;
        this.active= active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getActive() {
        return active == 0;
    }

    public int getActive1() {
        return active;
    }

    public void setActive(boolean active) {
      if(active)
          this.active= 0;
      else
          this.active= -1;
    }
}
