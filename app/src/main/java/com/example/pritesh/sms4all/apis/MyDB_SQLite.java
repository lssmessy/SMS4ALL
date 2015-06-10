package com.example.pritesh.sms4all.apis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pritesh.sms4all.Msg_DB;

/**
 * Created by PRITESH on 09-06-2015.
 */
public class MyDB_SQLite extends SQLiteOpenHelper {
    private static final int VERSION= 1;
    private static final String DATABASE_NAME="user_data.db";
    private static final String TABLE_NAME="users";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_MOBILE="mobile";
    public static final String COLUMN_MESSAGE="message";
    public static final String COLUMN_TIME="time";
    public static final String COLUMN_STATUS="status";

    public MyDB_SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    String query="CREATE TABLE "+TABLE_NAME+" ("+
            COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
            COLUMN_MOBILE+" TEXT ,"+
            COLUMN_MESSAGE+" TEXT ,"+
            COLUMN_TIME+" TEXT ,"+
            COLUMN_STATUS+" TEXT"+
            ");";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public void addData(Msg_DB msg_db){
        ContentValues values=new ContentValues();
        values.put(COLUMN_MOBILE,msg_db.get_mobile());
        values.put(COLUMN_MESSAGE,msg_db.get_message());
        values.put(COLUMN_TIME,msg_db.get_time());
        values.put(COLUMN_STATUS,msg_db.get_status());
        SQLiteDatabase db=getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
        Log.i("myLog","Data inserted");
    }
    public void deleteData(String message_id){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+COLUMN_ID+"= '"+message_id+"' ;");
        db.close();
        Log.i("myLog","Data deleted");
    }
    public String databaseToString(){
        String dbString="";
        SQLiteDatabase db=getWritableDatabase();
        String query="SELECT * FROM "+TABLE_NAME;
        Cursor c=db.rawQuery(query,null);
        //c.moveToFirst();
        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("message"))!=null && c.getString(c.getColumnIndex("time"))!=null){
                dbString+=c.getString(c.getColumnIndex("message"))+" time:"+c.getString(c.getColumnIndex("time"));
                dbString+="--------\n";
            }

        }
        db.close();
        return dbString;
    }
}
