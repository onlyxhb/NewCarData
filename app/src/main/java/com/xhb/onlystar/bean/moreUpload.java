package com.xhb.onlystar.bean;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by onlystar on 2016/4/5.
 */
public class moreUpload {
    private Rwmxb task;
    private ArrayList<Bitmap> pic;

    public void setTask(Rwmxb task) {
        this.task = task;
    }
    public void setPic(ArrayList<Bitmap> pic) {
        this.pic = pic;
    }
    public Rwmxb getTask() {

        return task;
    }
    public ArrayList<Bitmap> getPic() {
        return pic;
    }

    public moreUpload(Rwmxb task, ArrayList<Bitmap> pic) {
        this.task = task;
        this.pic = pic;
    }
}
