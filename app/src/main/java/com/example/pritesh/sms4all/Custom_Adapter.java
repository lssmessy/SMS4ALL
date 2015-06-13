package com.example.pritesh.sms4all;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pritesh.sms4all.apis.MyDB_SQLite;

import java.util.List;

/**
 * Created by pritesh.patel on 10-06-15.
 */
public class Custom_Adapter extends ArrayAdapter<Msg_DB> {
    final MyDB_SQLite myDB_sqLite = new MyDB_SQLite(getContext(), null, null, 1);
    Context mycontext;

    public Custom_Adapter(Context context, List<Msg_DB> objects) {
        super(context, R.layout.cutom_row, objects);
        this.mycontext = context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final TextView mobile, message, time, status;
        Button delete, resend;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View myview = inflater.inflate(R.layout.cutom_row, parent, false);
        final Msg_DB msgob = getItem(position);
        mobile = (TextView) myview.findViewById(R.id.mobileText);
        message = (TextView) myview.findViewById(R.id.msgText);
        message.setMovementMethod(new ScrollingMovementMethod());
        time = (TextView) myview.findViewById(R.id.timeText);
        status = (TextView) myview.findViewById(R.id.statusText);
        delete = (Button) myview.findViewById(R.id.deleteButton);
        resend = (Button) myview.findViewById(R.id.resendButton);
        mobile.setText(msgob.get_mobile());
        message.setText(msgob.get_message());
        time.setText(msgob.get_time());
        if (msgob.get_status().contains("Failed")) {
            status.setTextColor(Color.RED);
            status.setText(msgob.get_status());
        } else if (msgob.get_status().contains("Sent")) {
            status.setTextColor(Color.parseColor("#00FF00"));
            status.setText(msgob.get_status());
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder aBuilder = new AlertDialog.Builder(mycontext);
                aBuilder.setTitle("Delete message?");
                aBuilder.setMessage("Are you sure you want to delete this message?");
                aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDB_sqLite.deleteData(msgob.get_time());
                        remove(msgob);
                        Toast.makeText(mycontext,"One message has been successfully deleted!",Toast.LENGTH_SHORT).show();

                    }
                });
                aBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                aBuilder.show();


            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(mycontext, MainActivity.class);

                i.putExtra("mobile", msgob.get_mobile());
                i.putExtra("message", msgob.get_message());
                mycontext.startActivity(i);
                ((Activity)mycontext).finish();


            }
        });

        return myview;
    }

    @Override
    public void remove(Msg_DB object) {
        super.remove(object);
        this.notifyDataSetChanged();
    }
}
