package com.jacob.viewdraghelper;

/**
 * Created by xuzhili on 16/2/17.
 */
public class Gift {

    private int curShowMount;
    private int mount;
    private int perShowTime;

    public Gift(int curShowMount, int mount, int perShowTime) {
        this.curShowMount = curShowMount;
        this.mount = mount;
        this.perShowTime = perShowTime;
    }

    public int getCurShowMount() {
        return curShowMount;
    }

    public void setCurShowMount(int curShowMount) {
        this.curShowMount = curShowMount;
    }

    public int getMount() {
        return mount;
    }

    public void setMount(int mount) {
        this.mount = mount;
    }

    public int getPerShowTime() {
        return perShowTime;
    }

    public void setPerShowTime(int perShowTime) {
        this.perShowTime = perShowTime;
    }
}
