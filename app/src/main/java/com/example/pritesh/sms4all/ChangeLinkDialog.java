package com.example.pritesh.sms4all;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by pritesh.patel on 08-06-15.
 */
public class ChangeLinkDialog extends Dialog implements View.OnClickListener {
    public TextView link;
    public EditText changeLink;
    public Button change,cancel;
    public ChangeLinkDialog(Context context, int theme) {
        super(context, theme);
        View v= getLayoutInflater().inflate(R.layout.cutom_changelink,null);
        link=(TextView)v.findViewById(R.id.textView3);
        changeLink=(EditText)v.findViewById(R.id.editText5);
        change=(Button)v.findViewById(R.id.changelink_Button);
        cancel=(Button)v.findViewById(R.id.cancelButton);
        change.setOnClickListener(this);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(v);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String imgSett = prefs.getString("api_url","");
        link.setText(imgSett);


    }

    @Override
    public void onClick(View v) {
        if(isAnythingEmpty()){
            AlertDialog.Builder aleBuilder = new AlertDialog.Builder(getContext());
            aleBuilder.setMessage("Field can not be blank!");
            aleBuilder.setTitle("SMS4ALL");
            aleBuilder.setPositiveButton("Ok", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            aleBuilder.show();

        }
        else {
            SharedPreferences link_pref = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor url_link = link_pref.edit();
            url_link.putString("api_url", "http://" + changeLink.getText().toString() + ".ngrok.io/3space_local/apis/curl_text.php");
            url_link.apply();

            AlertDialog.Builder aleBuilder = new AlertDialog.Builder(getContext());
            aleBuilder.setMessage("Link has been changed successfully");
            aleBuilder.setTitle("SMS4ALL");
            aleBuilder.setPositiveButton("Ok", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    dismiss();
                }
            });
            aleBuilder.show();
        }

    }
    private boolean isAnythingEmpty() {
        Log.i("myLog", "isAnythingEmpty");
        if(changeLink.getText().toString().trim().length()<=0) {
            changeLink.requestFocus();
            return true;
        }
        else
            return false;
    }

}
