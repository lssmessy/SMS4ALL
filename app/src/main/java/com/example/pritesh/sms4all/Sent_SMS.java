package com.example.pritesh.sms4all;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.example.pritesh.sms4all.apis.MyDB_SQLite;

/**
 * Created by PRITESH on 10-06-2015.
 */
public class Sent_SMS extends ActionBarActivity {
    MyDB_SQLite myDB_sqLite;
    TextView data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sent_sms);
        myDB_sqLite=new MyDB_SQLite(this,null,null,1);
        data = (TextView) findViewById(R.id.textView4);
        printDatabase();


    }
    private void printDatabase() {
        String dbstring=myDB_sqLite.databaseToString();
        data.setText(dbstring);
    }
}
