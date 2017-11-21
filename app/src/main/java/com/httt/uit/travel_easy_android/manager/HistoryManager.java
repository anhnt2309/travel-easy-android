package com.httt.uit.travel_easy_android.manager;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import android.database.DatabaseUtils;
import com.httt.uit.travel_easy_android.model.AutoCompleteAirport;
import com.httt.uit.travel_easy_android.model.History;

import java.util.ArrayList;
import java.util.List;
 /* Created by hoqua on 11/19/2017.
 */

public class HistoryManager extends SQLiteOpenHelper {
    public static final String DATABASE_NAME ="HistoryName";
    private static final String TABLE_NAME ="History";
    private static final String ID ="id";
    private static final String VALUE ="value";
    private static final String AIRPORT ="airport";
    private static final String CITY ="city";
    private Context context;

    public HistoryManager(Context context) {
        super(context, DATABASE_NAME,null, 1);
        Log.d("HistoryManager", "HistoryManager: ");
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE "+TABLE_NAME +" (" +
                ID +" integer primary key, "+
                VALUE + " TEXT, "+
                AIRPORT +" TEXT, "+
                CITY +" TEXT)";
        db.execSQL(sqlQuery);
        Toast.makeText(context, "Create successfylly", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        Toast.makeText(context, "Drop successfylly", Toast.LENGTH_SHORT).show();
    }
    //Add new a history
    public void addHistory(AutoCompleteAirport history){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VALUE, history.getValue());
        values.put(AIRPORT, history.getAirport_name());
        values.put(CITY, history.getCity_name());
        //Neu de null thi khi value bang null thi loi

        db.insert(TABLE_NAME,null,values);

    }

    /*
    Select a History by ID
     */

    public History getHistoryById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { ID,
                        VALUE, AIRPORT,CITY }, ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        History history = new History(cursor.getString(1),cursor.getString(2),cursor.getString(3));
        cursor.close();
        db.close();
        return history;
    }
   /* Get Count History in Table Student
   */
    public int getHistoryCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int row=(int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return row;
    }
}
