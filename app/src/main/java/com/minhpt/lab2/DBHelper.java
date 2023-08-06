package com.minhpt.lab2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "ToDoDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE TODO(\n" +
                "  id text PRIMARY KEY,\n" +
                "  title text,\t\n" +
                "  content text,\n" +
                "  date text,\n" +
                "  type text,\n" +
                "  status integer\n" +
                "  )";
        db.execSQL(sql);
        String data = "INSERT INTO TODO VALUES\n" +
                "('1','Học C++','Học C++ nâng cao','29/3/2023','Khó',0),\n" +
                "('2','Học Java','Học Java cơ bản','15/4/2023','Dễ',0),\n" +
                "('3','Học React Native','Học React Native cơ bản','08/6/2023','Dễ',1)";
        db.execSQL(data);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS TODO");
            onCreate(db);
        }
    }
}
