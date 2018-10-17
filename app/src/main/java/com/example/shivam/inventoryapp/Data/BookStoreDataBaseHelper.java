package com.example.shivam.inventoryapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.shivam.inventoryapp.Data.BookStoreContract.BookStoreEntry;
import android.support.annotation.Nullable;


public class BookStoreDataBaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "BookStoreInformation.db";
    private static final Integer DATABASE_VERSION = 3;

    public BookStoreDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + BookStoreContract.BookStoreEntry.TABLE_NAME +  " ( "
                + BookStoreEntry.COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookStoreEntry.COLUMN_PRODUCT_NAME  + " TEXT NOT NULL , "
                + BookStoreEntry.COLUMN_PRODUCT_PRICE + " DOUBLE NOT NULL , "
                + BookStoreEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER DEFAULT 0 NOT NULL , "
                + BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " TEXT NOT NULL ,"
                + BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER + " TEXT DEFAULT NULL );" ;

        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + BookStoreContract.BookStoreEntry.TABLE_NAME +  " ( "
                + BookStoreEntry.COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookStoreEntry.COLUMN_PRODUCT_NAME  + " TEXT NOT NULL , "
                + BookStoreEntry.COLUMN_PRODUCT_PRICE + " DOUBLE NOT NULL , "
                + BookStoreEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER DEFAULT 0 NOT NULL , "
                + BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " TEXT NOT NULL ,"
                + BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER + " TEXT DEFAULT NULL );" ;

        String SQL_DELETE_ENTRIES = "DROP TABLE " + BookStoreContract.BookStoreEntry.TABLE_NAME;

        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
