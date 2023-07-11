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
        String sql = "CREATE TABLE ToDo(\n" +
                "  id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  title text,\t\n" +
                "  content text,\n" +
                "  date text,\n" +
                "  type text,\n" +
                "  status integer\n" +
                "  )";
        db.execSQL(sql);
        String data = "INSERT INTO ToDo VALUES\n" +
                "(1,'Học C++','Học C++ nâng cao','29/03/2023','Khó',0),\n" +
                "(2,'Học Java','Học Java cơ bản','19/04/2023','Dễ',1),\n" +
                "(3,'Học React Native','Học React Native cơ bản','19/04/2023','Dễ',1)";
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
