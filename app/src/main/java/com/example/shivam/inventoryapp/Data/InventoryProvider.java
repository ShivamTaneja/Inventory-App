package com.example.shivam.inventoryapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.example.shivam.inventoryapp.Data.BookStoreContract.BookStoreEntry;
import com.example.shivam.inventoryapp.R;

public class InventoryProvider extends ContentProvider {

    private BookStoreDataBaseHelper bookStoreDataBaseHelper = new BookStoreDataBaseHelper(getContext());
    private static final int inventory = 100;
    private static final int inventory_Id = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    static {
        sUriMatcher.addURI(BookStoreContract.CONTENT_AUTHORITY, BookStoreContract.PATH_PRODUCT_INFORMATION, inventory);
        sUriMatcher.addURI(BookStoreContract.CONTENT_AUTHORITY, BookStoreContract.PATH_PRODUCT_INFORMATION + "/#", inventory_Id);
    }

    public InventoryProvider() {

    }

    public InventoryProvider(BookStoreDataBaseHelper bookStoreDataBaseHelper) {
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
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case inventory:
                return BookStoreContract.BookStoreEntry.CONTENT_LIST_TYPE;
            case inventory_Id:
                return BookStoreContract.BookStoreEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues){
        final int match = sUriMatcher.match(uri);
        switch(match)
        {
            case inventory:
                return insert_data(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported with uri " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase sqLiteDatabase = bookStoreDataBaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int del_count=0;
        switch(match)
        {
            case inventory:
                del_count = sqLiteDatabase.delete(BookStoreEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case inventory_Id:
                selection = BookStoreContract.BookStoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                del_count = sqLiteDatabase.delete(BookStoreEntry.TABLE_NAME, selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported with uri " + uri );
        }
        if(del_count > 0)
            getContext().getContentResolver().notifyChange(uri , null);
        return del_count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case inventory:
                return update_data(uri, contentValues, selection, selectionArgs);
            case inventory_Id:
                selection = BookStoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return update_data(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Updation is not supported for uri " + uri);
        }
    }

    private Uri insert_data(Uri uri, ContentValues contentValues){
        String productName = contentValues.getAsString(BookStoreEntry.COLUMN_PRODUCT_NAME);
        if( TextUtils.isEmpty(productName))
        {
            throw new IllegalArgumentException("Product name is missing");
        }

        int productPrice = contentValues.getAsInteger(BookStoreEntry.COLUMN_PRODUCT_PRICE);
        if(productPrice <=0 )
            throw new IllegalArgumentException("Product requires valid price");


        int productQuantity = contentValues.getAsInteger(BookStoreEntry.COLUMN_PRODUCT_QUANTITY);
        if(productQuantity <= 0 )
            throw new IllegalArgumentException("Product requires valid quantity");


        String productSupplierName = contentValues.getAsString(BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
        if(TextUtils.isEmpty(productSupplierName))
        {
            throw new IllegalArgumentException("Product Supplier name is missing");
        }

        String productSupplierPhoneNumber = contentValues.getAsString(BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);
        if(!TextUtils.isEmpty(productSupplierPhoneNumber))
        {
        long temp = Long.parseLong(productSupplierPhoneNumber);
        long min_range = Long.parseLong("1000000000");
        long max_range = Long.parseLong("9999999999");
        if(min_range > temp || temp > max_range)
            throw new IllegalArgumentException("Product requires valid phone number");
        }

        SQLiteDatabase msqLiteDatabase = bookStoreDataBaseHelper.getWritableDatabase();
        long id = msqLiteDatabase.insert(BookStoreEntry.TABLE_NAME,null,contentValues);
        if (id == -1) {
            Log.e("twitter", "Failed to insert row for " + uri);
            return null;
        }
        Toast.makeText(getContext(),R.string.data_inserted, Toast.LENGTH_SHORT).show();
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private int update_data(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if(contentValues.size() == 0)
            return 0;
        if(contentValues.containsKey(BookStoreEntry.COLUMN_PRODUCT_NAME))
        {
            String productName = contentValues.getAsString(BookStoreEntry.COLUMN_PRODUCT_NAME);
            if(TextUtils.isEmpty(productName))
                throw new IllegalArgumentException("Product name is missing");
        }
        if(contentValues.containsKey(BookStoreEntry.COLUMN_PRODUCT_PRICE))
        {
            int productPrice = contentValues.getAsInteger(BookStoreEntry.COLUMN_PRODUCT_PRICE);
            if(productPrice < 0 )
                throw new IllegalArgumentException("Product requires valid price");
        }
        if(contentValues.containsKey(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_QUANTITY))
        {
            int productQuantity = contentValues.getAsInteger(BookStoreEntry.COLUMN_PRODUCT_QUANTITY);
            if(productQuantity < 0 )
                throw new IllegalArgumentException("Product requires valid quantity");
        }
        if(contentValues.containsKey(BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_NAME))
        {
            String productSupplierName = contentValues.getAsString(BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            if( TextUtils.isEmpty(productSupplierName))
                throw new IllegalArgumentException("Product Supplier name is missing");

        }

        SQLiteDatabase msqLiteDatabase = bookStoreDataBaseHelper.getWritableDatabase();
        int id = msqLiteDatabase.update(BookStoreEntry.TABLE_NAME, contentValues,
                selection, selectionArgs);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to update row for " + uri);
            return 0;
        }
        Toast.makeText(getContext(),R.string.data_changed_successfully, Toast.LENGTH_SHORT).show();
        if(id>0)
            getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }
}