package com.example.shivam.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.shivam.inventoryapp.Data.BookStoreContract.BookStoreEntry;

public class BookInventoryCursorAdapter extends CursorAdapter {

    public BookInventoryCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context,final Cursor cursor) {

        TextView name_list_item = view.findViewById(R.id.name);
        TextView price_list_item = view.findViewById(R.id.price);
        final TextView quantity_list_item = view.findViewById(R.id.quantity);
        Button sale_button = view.findViewById(R.id.saleButton);

        int nameColumnIndex = cursor.getColumnIndex(BookStoreEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookStoreEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookStoreEntry.COLUMN_PRODUCT_QUANTITY);
        final String id = cursor.getString(cursor.getColumnIndex(BookStoreEntry._ID));

        String name = cursor.getString(nameColumnIndex);
        String price = cursor.getString(priceColumnIndex);
        final int quantity = cursor.getInt(quantityColumnIndex);

        name_list_item.setText(name);
        price_list_item.setText(price);
        quantity_list_item.setText(String.valueOf(quantity));

        sale_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (quantity > 0) {
                    Uri currentBookUri = ContentUris.withAppendedId(BookStoreEntry.CONTENT_URI, Long.parseLong(id));
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(BookStoreEntry.COLUMN_PRODUCT_QUANTITY, quantity-1);

                    context.getContentResolver().update(currentBookUri, contentValues, null, null);
                    swapCursor(cursor);
                }
            }
        });
    }
}

