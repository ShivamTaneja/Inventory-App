package com.example.shivam.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.shivam.inventoryapp.Data.BookStoreContract;


import java.util.zip.Inflater;

public class BookInventoryCursorAdapter extends CursorAdapter {

    public BookInventoryCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name_list_item = view.findViewById(R.id.name);
        TextView price_list_item = view.findViewById(R.id.price);

        int nameColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_PRICE);

        String name = cursor.getString(nameColumnIndex);
        String price = cursor.getString(priceColumnIndex);

        name_list_item.setText(name);
        price_list_item.setText(price);
    }
}
