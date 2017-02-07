package com.example.pau.deltacalc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RankingOpenHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "project";
    private static final String RANKING_TABLE_NAME = "Ranking";
    private static final String RANKING_TABLE_CREATE = "CREATE TABLE " + RANKING_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT, cards INTEGER, score INTEGER)";

    public RankingOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RANKING_TABLE_CREATE + ";");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RANKING_TABLE_NAME + ";");
        db.execSQL(RANKING_TABLE_CREATE);
    }

    public Cursor getRanking(int cards){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"user", "score"};
        String[] where = {Integer.toString(cards)};
        return db.query(RANKING_TABLE_NAME, columns, "cards=?", where, null, null, "score ASC");
    }

    public void addScore(ContentValues values, String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(tableName, null, values);
    }

    public void resetTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tableName + ";");
    }
}
