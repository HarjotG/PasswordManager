package com.example.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import static java.security.AccessController.getContext;

public class DB implements BaseColumns {
    private DBhelper dbHelper;

    private Auth auth;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "passwords.db";
    private static final String SQL_TABLE_NAME ="accounts";

    public DB(Context context) {
        dbHelper = new DBhelper(context);
        auth = new Auth();
    }

    public void close() {
        dbHelper.close();
    }

    // create a new account and return true if successful
    // The provided account's id is not used and a new one is generated and set instead
    // Returns -1 on error, or the new ID of the account on success
    public long createAccount(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Username", account.username);
        values.put("Password", account.password);
        values.put("Email", account.email);
        values.put("Site", account.site);
        values.put("Tag", account.tag);
        return db.insert("accounts", null, values);

    }

    // return all accounts in the database ordered in descending order by TAG
    public Account[] getAccounts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(SQL_TABLE_NAME, null, null, null, null, null, "Tag DESC", null);
        Account[] accounts = new Account[cursor.getCount()];
        int i = 0;
        while(cursor.moveToNext()) {
            accounts[i] = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            Log.d("ACCOUNT_DB", accounts[i].toString());
            i++;
        }

        cursor.close();
        return accounts;
    }

    // update the specified account and return true if successful
    public boolean updateAccount(Account oldAcc, Account newAcc){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("Username", newAcc.username);
        values.put("Password", newAcc.password);
        values.put("Email", newAcc.email);
        values.put("Site", newAcc.site);
        values.put("Tag", newAcc.tag);
        int rows = db.update(SQL_TABLE_NAME, values, _ID + " = ?", new String[]{oldAcc.id});
        return rows == 1;
    }

    // delete the specified account and return true if successful
    public boolean deleteAccount(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.delete(SQL_TABLE_NAME, _ID + " = ?", new String[]{account.id});
        return rows == 1;
    }


    private static class DBhelper extends SQLiteOpenHelper{

        public DBhelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS "  + SQL_TABLE_NAME + " ( " + _ID + " INTEGER PRIMARY KEY, Username TEXT, Password TEXT, Email TEXT, Site TEXT, Tag TEXT)");
            Log.d("DB_CREATE", "onCreate called");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            // TODO: Change
            db.execSQL("DROP TABLE IF EXISTS " + SQL_TABLE_NAME);
            onCreate(db);
        }

    }
}
