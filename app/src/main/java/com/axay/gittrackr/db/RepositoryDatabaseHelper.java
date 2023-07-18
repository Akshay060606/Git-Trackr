package com.axay.gittrackr.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RepositoryDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "repository.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "repositories";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_REPO_NAME = "repo_name";
    private static final String COLUMN_DESCRIPTION = "description";

    Context applicationContext;

    public RepositoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        applicationContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_REPO_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertRepository(String url, String repoName, String description) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_URL, url);
        values.put(COLUMN_REPO_NAME, repoName);
        values.put(COLUMN_DESCRIPTION, description);

        long newRowId = db.insert(TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(applicationContext, "Error while Storing Data!", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(applicationContext, "Data Stored Successfully!", Toast.LENGTH_SHORT)
                    .show();
        }

        db.close();
    }

    @SuppressLint("Range")
    public List<String[]> getAllRepositories() {
        List<String[]> repositories = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String[] repositoryData = new String[3];
            repositoryData[0] = cursor.getString(cursor.getColumnIndex(COLUMN_URL));
            repositoryData[1] = cursor.getString(cursor.getColumnIndex(COLUMN_REPO_NAME));
            repositoryData[2] = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            repositories.add(repositoryData);
        }

        cursor.close();
        db.close();
        return repositories;
    }

    public void deleteRepository(String url) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_URL + " = ?", new String[]{url});
        db.close();
    }
}
