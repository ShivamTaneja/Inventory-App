package com.example.shivam.inventoryapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class inventoryProvider extends ContentProvider {

    private BookStoreDataBaseHelper bookStoreDataBaseHelper;
    private static final int inventory = 100;
    private static final int inventory_Id = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(BookStoreContract.CONTENT_AUTHORITY, BookStoreContract.PATH_PRODUCT_INFORMATION, inventory);
        sUriMatcher.addURI(BookStoreContract.CONTENT_AUTHORITY, BookStoreContract.PATH_PRODUCT_INFORMATION + "/#", inventory_Id);
    }

    public inventoryProvider()
    {

    }

    public inventoryProvider(BookStoreDataBaseHelper bookStoreDataBaseHelper) {
        this.bookStoreDataBaseHelper = bookStoreDataBaseHelper;
    }

    @Override
    public boolean onCreate() {
        bookStoreDataBaseHelper = new BookStoreDataBaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs ,String sortOrder) {

        bookStoreDataBaseHelper = new BookStoreDataBaseHelper(getContext());
        SQLiteDatabase msqLiteDatabase = bookStoreDataBaseHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match)
        {
            case inventory:
               cursor = msqLiteDatabase.query(BookStoreContract.BookStoreEntry.TABLE_NAME,projection, selection, null,
                    null, null, sortOrder );
                break;
            case inventory_Id:
                selection = BookStoreContract.BookStoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = msqLiteDatabase.query(BookStoreContract.BookStoreEntry.TABLE_NAME,projection, selection, selectionArgs
                            ,null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query illegal Uri " + uri );
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        final int match = sUriMatcher.match(uri);
        switch(match)
        {
         case inventory:
             uri = insert_data(uri, contentValues);
             break;
         default:
             throw new IllegalArgumentException("Insertion is not supported with uri " + uri);
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        int id;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case inventory_Id:
                id = update_data(uri, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Updation is not supported for uri " + uri);
        }
        return id;
    }

    private Uri insert_data(Uri uri, ContentValues contentValues)
    {
        bookStoreDataBaseHelper = new BookStoreDataBaseHelper(getContext());
        SQLiteDatabase msqLiteDatabase = bookStoreDataBaseHelper.getWritableDatabase();
        long id = msqLiteDatabase.insert(BookStoreContract.BookStoreEntry.TABLE_NAME,null,contentValues);
        return ContentUris.withAppendedId(uri, id);
    }
    private int update_data(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs)
    {
        bookStoreDataBaseHelper = new BookStoreDataBaseHelper(getContext());
        SQLiteDatabase msqLiteDatabase = bookStoreDataBaseHelper.getWritableDatabase();
        int id = msqLiteDatabase.update(BookStoreContract.BookStoreEntry.TABLE_NAME, contentValues,
                selection, selectionArgs);
        return id;
    }
}
