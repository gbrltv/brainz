package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

/**
 * Created by Matheus on 15/08/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Brainz_DB";
    // Table name
    private static final String TABLE_TRACKS = "Tracks";
    // Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_trackName = "trackName";
    private static final String KEY_albumName = "albumName";
    private static final String KEY_artistName = "artistName";
    private static final String KEY_artistID = "artistID";
    private static final String KEY_albumImage = "albumImage";
    private static final String KEY_previewUrl = "previewUrl";
    private static final String KEY_status = "status";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TRACKS + "("
        + KEY_ID + " TEXT PRIMARY KEY," + KEY_trackName + " TEXT," + KEY_albumName + " TEXT,"+ KEY_artistName + " TEXT," + KEY_albumImage + " TEXT,"+ KEY_previewUrl + " TEXT," + KEY_status + " INTEGER,"+ KEY_artistID + " TEXT" +")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKS);
        // Creating tables again
        onCreate(db);
    }

    // Adding new shop
    public void addTrack(Track track) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, track.getId());
        values.put(KEY_trackName, track.getTrackName());
        values.put(KEY_albumName, track.getAlbumName());
        values.put(KEY_artistName, track.getArtistName());
        values.put(KEY_albumImage, track.getAlbumImage());
        values.put(KEY_previewUrl, track.getPreviewUrl());
        values.put(KEY_status, track.getStatus());
        values.put(KEY_artistID, track.getArtistID());
        // Inserting Row
        db.insert(TABLE_TRACKS, null, values);
        db.close(); // Closing database connection
    }

    public List<Track> getTracks(int status) {
        List<Track> trackList = new ArrayList<Track>();

        String selectQuery = "SELECT * FROM " + TABLE_TRACKS + " WHERE " + KEY_status + " = " + status + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Track track = new Track();
                track.setId(cursor.getString(0));
                track.setTrackName(cursor.getString(1));
                track.setAlbumName(cursor.getString(2));
                track.setArtistName(cursor.getString(3));
                track.setAlbumImage(cursor.getString(4));
                track.setPreviewUrl(cursor.getString(5));
                track.setStatus(Integer.parseInt(cursor.getString(6)));
                // Adding contact to list
                trackList.add(track);
                //Log.d("escreveparadas","Nome: "+cursor.getString(1));
            } while (cursor.moveToNext());
        }
        /*for(Track t : trackList){
            Log.d("Tbefore: ", t.getTrackName());
        }
        Collections.shuffle(trackList);
        for(Track t : trackList){
            Log.d("Tafter: ", t.getTrackName());
        }*/
        return trackList;
    }

    public void updateStatus(String id, int status){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_status, status);

        String where = "id=?";
        String[] whereArgs = {id};

        db.update(TABLE_TRACKS, values, where, whereArgs);
    }

    public String searchArtistID(String id){
        String selectartistID = "SELECT "+KEY_artistID+" FROM " + TABLE_TRACKS + " WHERE " + KEY_ID + " = '" + id + "';";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectartistID, null);
        cursor.moveToFirst();
        Log.d("DB","Cursor"+cursor.getString(0));

        return cursor.getString(0);

    }

    public void deleteRows(int status) {
        String selectQuery = "DELETE FROM " + TABLE_TRACKS + " WHERE " + KEY_status + " = " + status + ";";
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_TRACKS, KEY_status + "=" + status, null);
    }

    public boolean trackCheck(String id){
        String selectrack = "SELECT COUNT(1) FROM " + TABLE_TRACKS + " WHERE " + KEY_ID + " = '" + id + "';";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectrack, null);
        cursor.moveToFirst();
        if(cursor.getString(0) == "1")
            return true;
        return false;
    }

}
