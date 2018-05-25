package com.example.root.recyclertutorial.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    static final String dbName = "reportAbuseDB";
    private static DatabaseHelper mInstance = null;

    public DatabaseHelper(Context context) {
        super(context, dbName, null, 1);
    }

    public static synchronized DatabaseHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx);
        }
        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_LOGIN_TABLE = "CREATE TABLE " + DataContract.LoginDetails.TABLE_NAME + " (" +
                DataContract.LoginDetails.USER_ID + " TEXT UNIQUE NOT NULL, " +
                DataContract.LoginDetails.USER_NAME + " TEXT NOT NULL, " +
                DataContract.LoginDetails.EMAIL + " TEXT NOT NULL, " +
                DataContract.LoginDetails.COMPANY_NAME + " TEXT NOT NULL, " +
                DataContract.LoginDetails.email_confirmed + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_CANDIDATE_TABLE = "CREATE TABLE " + DataContract.CandidateDetails.TABLE_NAME + " (" +
                "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DataContract.CandidateDetails.CANDIDATE_ID + " TEXT UNIQUE NOT NULL, " +
                DataContract.CandidateDetails.CANDIDATE_NAME + " TEXT NOT NULL, " +
                DataContract.CandidateDetails.TO_SAVE_IN_DATABASE + " TEXT NOT NULL, " +
                DataContract.CandidateDetails.NATURE_OF_ABUSE + " TEXT NOT NULL, " +
                DataContract.CandidateDetails.DATE_OF_JOINING + " TEXT NOT NULL, " +
                DataContract.CandidateDetails.USER_EMAIL + " TEXT NOT NULL, " +
                DataContract.CandidateDetails.CURRENT_ORGANIZATION + " TEXT NOT NULL, " +
                DataContract.CandidateDetails.REPORT_ABUSED_BY + " TEXT NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_LOGIN_TABLE);
        db.execSQL(SQL_CREATE_CANDIDATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.LoginDetails.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.CandidateDetails.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.query(
                DataContract.LoginDetails.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        return res;
    }
    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}