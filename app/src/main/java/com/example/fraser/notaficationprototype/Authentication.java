package com.example.fraser.notaficationprototype;

import java.util.ArrayList;

/**
 * Created by Fraser on 17/01/2017.
 */

public class Authentication {

    protected Long id;
    protected int deviceID;
    protected int authenticatorID;
    protected int emotionID;
    protected String location;
    protected String comments;
    protected ArrayList<Authentication> authenList;

    public ArrayList<Authentication> getAuthenList() {
        return authenList;
    }

    public Authentication(){
        authenList = new ArrayList<>();
    }

    public Authentication(Long id, int devID, int authenID, int emoID, String loc, String comms) {
        this.id = id;
        this.deviceID = devID;
        this.authenticatorID = authenID;
        this.emotionID = emoID;
        this.location = loc;
        this.comments = comms;
    }
}
