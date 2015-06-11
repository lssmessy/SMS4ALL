package com.example.pritesh.sms4all;

/**
 * Created by PRITESH on 09-06-2015.
 */
public class Msg_DB {
    private int _id;
    private String _username;
    private String _mobile;
    private String _message;
    private String _time;
    private String _status;

    public Msg_DB(){

    }

    public Msg_DB(String _username, String _mobile, String _message, String _time, String _status) {

        this._username = _username;
        this._mobile = _mobile;
        this._message = _message;
        this._time = _time;
        this._status = _status;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public String get_username() {
        return _username;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_mobile(String _mobile) {
        this._mobile = _mobile;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public int get_id() {
        return _id;
    }

    public String get_mobile() {
        return _mobile;
    }

    public String get_message() {
        return _message;
    }

    public String get_time() {
        return _time;
    }

    public String get_status() {
        return _status;
    }
}


