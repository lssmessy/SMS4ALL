package com.example.pritesh.sms4all;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.example.pritesh.sms4all.apis.APIL_LINKS.*;


public class MainActivity extends ActionBarActivity {

    EditText username,password,mobile,message;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=(EditText)findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText2);
        mobile=(EditText)findViewById(R.id.editText3);
        message=(EditText)findViewById(R.id.editText4);
    }
public void sendMessage(View view){
    new Login_background().execute();
}
    private class Login_background extends AsyncTask<Void,Void,Void> {
        ProgressDialog dialog;
        static final String p="MyLog";
        boolean res=false;
        String user="";
        String response_message;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            dialog=new ProgressDialog(MainActivity.this);
            dialog.setMessage("Sending SMS...");
            dialog.setTitle("SMS4ALL");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();


            Log.i(p, "onPreExecute");
        }


        @Override
        protected Void doInBackground(Void... params) {

            String uname=username.getText().toString();
            String pwd=password.getText().toString();

            List<NameValuePair> data=new ArrayList<NameValuePair>();

            data.add(new BasicNameValuePair("username",uname));
            data.add(new BasicNameValuePair("password",pwd));
            data.add(new BasicNameValuePair("mobile",mobile.getText().toString()));
            data.add(new BasicNameValuePair("message",message.getText().toString()));


            try{
                HttpClient client=new DefaultHttpClient();
                HttpPost post=new HttpPost("http://4a520d35.ngrok.io/3space_local/apis/curl_text.php");
                post.setEntity(new UrlEncodedFormEntity(data));
                HttpResponse response=client.execute(post);

                if(response!=null){
                    InputStream is=response.getEntity().getContent();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb=new StringBuilder();
                    String line=null;
                    while((line=bufferedReader.readLine())!=null){

                        sb.append(line+"\n");
                    }
                    //this.res=sb.toString();
                    JSONObject jsonObject=new JSONObject(sb.toString());
                    jsonObject.get("is_sent");
                    if(jsonObject.get("is_sent")==true)
                    {
                        this.res=true;
                    }
                    else
                        this.res=false;

                    response_message=jsonObject.get("message").toString();

                }




            }catch (Exception e){e.getMessage();}


            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i(p, "onPostExecute");
            dialog.dismiss();

            AlertDialog.Builder aBuilder=new AlertDialog.Builder(MainActivity.this);
            aBuilder.setTitle("Success");

            if(res==true)
                aBuilder.setMessage(response_message);
            else if(res==false)
                aBuilder.setMessage("Failed to send");

            aBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    }
            });
            aBuilder.show();

            //Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.i(p,"onProgressUpdate");
            dialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
