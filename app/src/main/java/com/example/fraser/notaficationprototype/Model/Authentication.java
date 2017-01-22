package com.example.fraser.notaficationprototype.Model;

import java.util.ArrayList;

/**
 * Created by Fraser on 17/01/2017.
 */

public class Authentication {

    protected Long id;
    protected String deviceID;
    protected String authenticatorID;
    protected String emotionID;
    protected String timeStamp;
    protected String location;
    protected String comments;
    protected ArrayList<Authentication> authenList;

    public ArrayList<Authentication> getAuthenList() {
        return authenList;
    }

    public Authentication(){
        authenList = new ArrayList<>();
    }

    public Authentication(Long id, String devID, String authenID, String emoID, String timeStamp , String loc, String comms) {
        this.id = id;
        this.deviceID = devID;
        this.authenticatorID = authenID;
        this.emotionID = emoID;
        this.timeStamp = timeStamp;
        this.location = loc;
        this.comments = comms;
    }
}
