package com.example.pritesh.sms4all;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.example.pritesh.sms4all.apis.MyDB_SQLite;

import java.util.List;

/**
 * Created by PRITESH on 10-06-2015.
 */
public class Sent_SMS extends ActionBarActivity {
    MyDB_SQLite myDB_sqLite;
    //TextView mobile,message,time,status;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sent_sms);
        myDB_sqLite=new MyDB_SQLite(this,null,null,1);
        List<Msg_DB> s=myDB_sqLite.getMessages();

        lv= (ListView) findViewById(R.id.listview);
        Custom_Adapter myadapter=new Custom_Adapter(this,s);

        lv.setAdapter(myadapter);


    /*    mobile = (TextView) findViewById(R.id.mobileText);
        message = (TextView) findViewById(R.id.msgText);
        time = (TextView) findViewById(R.id.timeText);
        status = (TextView) findViewById(R.id.statusText);*/
        //printDatabase();


    }

}
