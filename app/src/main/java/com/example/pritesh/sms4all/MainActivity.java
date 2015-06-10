package com.example.pritesh.sms4all;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pritesh.sms4all.apis.MyDB_SQLite;

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
import java.util.Calendar;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    EditText username, password, mobile, message;
    String api_url;
    Button save, change;
    SharedPreferences link_pref, is_saved;
    ImageView contacts;
    String alertmsg;
    MyDB_SQLite myDB_sqLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        link_pref = PreferenceManager.getDefaultSharedPreferences(this);
        String imgSett = link_pref.getString("api_url", "");
        SharedPreferences.Editor url_link = link_pref.edit();
        if (imgSett != "") {
            url_link.putString("api_url", imgSett);
            this.api_url = imgSett;
            url_link.apply();
        } else {
            String url = "http://f680b1e1.ngrok.io/3space_local/apis/curl_text.php";
            url_link.putString("api_url", url);
            this.api_url = url;
            url_link.apply();
        }

        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        mobile = (EditText) findViewById(R.id.editText3);
        message = (EditText) findViewById(R.id.editText4);
        myDB_sqLite=new MyDB_SQLite(this,null,null,1);
        message.setImeActionLabel("SEND", EditorInfo.IME_ACTION_SEND);
        save = (Button) findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails(v);
            }
        });
        change = (Button) findViewById(R.id.changeButton);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEdittexts(v);
            }
        });
        contacts = (ImageView) findViewById(R.id.contactsImage);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectContact();
            }
        });
        is_saved = PreferenceManager.getDefaultSharedPreferences(this);
        String pref = is_saved.getString("saved", "");
        String uname = is_saved.getString("username", "");
        String pwd = is_saved.getString("password", "");
        if (pref.contains("yes")) {
            username.setText(uname);
            password.setText(pwd);
            username.setEnabled(false);
            password.setEnabled(false);
            save.setEnabled(false);
            change.setEnabled(true);
        }
        message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage(v);
                    return true;
                }
                return false;
            }
        });
        Log.i("myLog", "onCreate" + pref);
        Bundle bun=getIntent().getExtras();
        if(bun!=null)
        {
            mobile.setText(bun.getString("mobile"));
            message.setText(bun.getString("message"));
        }
    }

    private void selectContact() {
        Log.i("myLog", "selectContact");
        mobile.setText("");
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, 1);

    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent
            data) {
        super.onActivityResult(reqCode, resultCode, data);
        Log.i("myLog", "onActivityResult");
        switch (reqCode) {
            case (1):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id =
                                c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =
                                c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            if (cNumber.startsWith("+91")) {
                                cNumber = cNumber.replace("+91", "");
                                cNumber = cNumber.replace(" ", "");
                                mobile.setText(cNumber);
                            } else if (cNumber.startsWith("0")) {

                                cNumber = cNumber.replaceFirst("0", "");
                                cNumber = cNumber.replace(" ", "");

                                mobile.setText(cNumber);
                            }
                            if (cNumber.contains("-")) {

                                cNumber = cNumber.replace("-", "");
                                mobile.setText(cNumber);
                            }
                            if (cNumber.contains("(")) {

                                cNumber = cNumber.replace("(", "");
                                mobile.setText(cNumber);
                            }
                            if (cNumber.contains(")")) {

                                cNumber = cNumber.replace(")", "");
                                mobile.setText(cNumber);
                            }
                            if (cNumber.contains(" ")) {
                                cNumber = cNumber.replace(" ", "");
                                mobile.setText(cNumber);
                            }
                            if (cNumber.length() > 0 && cNumber.length() < 10) {
                                Toast.makeText(getBaseContext(), "Contact don't have 10 digit mobile number", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                mobile.setText(cNumber);
                            }
                        }
                        else {
                            Toast.makeText(getBaseContext(), "Contact don't have a valid mobile number", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        }

    }


    public void sendMessage(View view) {
        Log.i("myLog", "sendMessage");
        if (isAnythingEmpty() == false) {
            new Login_background().execute();

        } else {
            AlertDialog.Builder aleBuilder = new AlertDialog.Builder(this);
            aleBuilder.setTitle("SMS4ALL says...");
            aleBuilder.setMessage(alertmsg);
            aleBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            aleBuilder.show();
        }
    }

    private boolean isAnythingEmpty() {
        Log.i("myLog", "isAnythingEmpty");
        if (username.getText().toString().trim().length() <= 0) {
            username.requestFocus();
            this.alertmsg = "Username can't be blank";
            return true;
        } else if (password.getText().toString().trim().length() <= 0) {
            password.requestFocus();
            this.alertmsg = "Password can't be blank";
            return true;
        } else if (mobile.getText().toString().trim().length() <= 0) {
            mobile.requestFocus();
            this.alertmsg = "Mobile number can't be blank";
            return true;
        } else if (mobile.getText().toString().trim().length() > 0 && mobile.getText().toString().trim().length() < 10) {
            mobile.requestFocus();
            this.alertmsg = "Mobile number must have 10 digits";
            return true;
        } else if (message.getText().toString().trim().length() <= 0) {
            message.requestFocus();
            this.alertmsg = "Message field shouldn't be blank";
            return true;
        } else
            return false;
    }

    private class Login_background extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        static final String p = "MyLog";
        boolean res = false;
        String user = "";
        String response_message;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            Log.i("myLog", "onPreExecute");
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Sending SMS...");
            dialog.setTitle("SMS4ALL");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            Log.i(p, "onPreExecute");
        }


        @Override
        protected Void doInBackground(Void... params) {
            Log.i("myLog", "doInBackground");
            String uname = username.getText().toString();
            String pwd = password.getText().toString();

            List<NameValuePair> data = new ArrayList<NameValuePair>();

            data.add(new BasicNameValuePair("username", uname));
            data.add(new BasicNameValuePair("password", pwd));
            data.add(new BasicNameValuePair("mobile", mobile.getText().toString()));
            data.add(new BasicNameValuePair("message", message.getText().toString()));


            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(api_url);
                post.setEntity(new UrlEncodedFormEntity(data));
                HttpResponse response = client.execute(post);

                if (response != null) {
                    InputStream is = response.getEntity().getContent();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {

                        sb.append(line + "\n");
                    }
                    //this.res=sb.toString();
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    jsonObject.get("is_sent");
                    String status;
                    if (jsonObject.get("is_sent") == true) {
                        this.res = true;
                        status="Sent";
                    } else
                    {
                        this.res = false;
                        status="Failed";
                    }

                    response_message = jsonObject.get("message").toString();
                    Calendar c=Calendar.getInstance();

                    String time=c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                    Msg_DB msg_db=new Msg_DB(mobile.getText().toString(),message.getText().toString(),time,status);
                    myDB_sqLite.addData(msg_db);


                }


            } catch (Exception e) {
                e.getMessage();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("myLog", "onPostExecute");
            dialog.dismiss();

            AlertDialog.Builder aBuilder = new AlertDialog.Builder(MainActivity.this);
            aBuilder.setTitle("Success");

            if (res == true)
                aBuilder.setMessage(response_message);
            else if (res == false)
                aBuilder.setMessage("Failed to send");

            aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    message.setText("");
                }
            });
            aBuilder.show();

            //Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.i("myLog", "onProgressUpdate");
            dialog.dismiss();
        }
    }

    public void showSentSMS(View view){
        Intent i=new Intent(this,Sent_SMS.class);
        startActivity(i);


    }

    public void changeLink(View view) {

        ChangeLinkDialog dialog = new ChangeLinkDialog(this, R.style.Base_V11_Theme_AppCompat);
        dialog.setTitle("Change Link");
        dialog.show();

    }

    public void saveDetails(View view) {

        Log.i("myLog", "saveDetails");

        username.setEnabled(false);
        password.setEnabled(false);
        save.setEnabled(false);
        change.setEnabled(true);
        SharedPreferences.Editor editor = is_saved.edit();
        editor.putString("saved", "yes");
        editor.putString("username", username.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.apply();

    }

    public void enableEdittexts(View view) {
        Log.i("myLog", "enableEdittexts");
        username.setEnabled(true);
        password.setEnabled(true);
        username.setFocusable(true);
        password.setFocusable(true);
        save.setEnabled(true);
        change.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.i("myLog", "onCreateOptionsMenu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.i("myLog", "onOptionsItemSelected");
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
