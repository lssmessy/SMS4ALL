package com.example.pritesh.sms4all;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pritesh.sms4all.apis.MyDB_SQLite;

/**
 * Created by PRITESH on 10-06-2015.
 */
public class Sent_SMS_Dialog extends Dialog {


    /*public EditText changeLink;
    public Button change,cancel;*/
    public Sent_SMS_Dialog(Context context, int theme) {
        super(context, theme);

        View v = getLayoutInflater().inflate(R.layout.sent_sms, null);
        //data = (TextView) v.findViewById(R.id.textView4);

        setContentView(v);
        //printDatabase();

    }

}