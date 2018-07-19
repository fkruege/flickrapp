package com.krueger.flickrfindr.utils;

//https://proandroiddev.com/8-steps-to-implement-paging-library-in-android-d02500f7fffe
public class NetworkState {

    public enum Status{
        RUNNING,
        SUCCESS,
        FAILED
    }


    private final Status status;
    private final String msg;

    public static final NetworkState LOADED;
    public static final NetworkState LOADING;

    public NetworkState(Status status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    static {
        LOADED=new NetworkState(Status.SUCCESS,"Success");
        LOADING=new NetworkState(Status.RUNNING,"Running");

    }

    public Status getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
