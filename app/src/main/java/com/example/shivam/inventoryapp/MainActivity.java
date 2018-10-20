package com.example.shivam.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.shivam.inventoryapp.Data.BookStoreContract.BookStoreEntry;
import com.example.shivam.inventoryapp.Data.BookStoreDataBaseHelper;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InsertProduct.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
       // DisplayDatabaseInfo();
    }

    /*private void DisplayDatabaseInfo() {
        BookStoreDataBaseHelper mBookStoreDataBaseHelper = new BookStoreDataBaseHelper(this);
        SQLiteDatabase mSqLiteDatabase = mBookStoreDataBaseHelper.getReadableDatabase();

        String[] projection ={
                BookStoreEntry._ID,
                BookStoreEntry.COLUMN_PRODUCT_NAME,
                BookStoreEntry.COLUMN_PRODUCT_PRICE,
        };

        Cursor cursor = mSqLiteDatabase.query(BookStoreEntry.TABLE_NAME, projection, null,null, null, null, null);

        textDisplay.setText(getString(R.string.the_bookstore_contains) + " " + cursor.getCount() + " "+ getString(R.string.items) + " \n");
        textDisplay.append(BookStoreEntry.COLUMN_PRODUCT_ID + " - " + BookStoreEntry.COLUMN_PRODUCT_NAME + " - " + BookStoreEntry.COLUMN_PRODUCT_PRICE + "\n");

        int idColumnIndex = cursor.getColumnIndex(BookStoreEntry.COLUMN_PRODUCT_ID);
        int nameColumnIndex = cursor.getColumnIndex(BookStoreEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookStoreEntry.COLUMN_PRODUCT_PRICE);

        try {
            while(cursor.moveToNext())
            {
                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                Double currentPrice = cursor.getDouble(priceColumnIndex);

                textDisplay.append( currentId + "    -   " + currentName + "    -    " + currentPrice );
                textDisplay.append("\n");
            }
        }catch (Exception e) {  }
        finally {
            cursor.close();
        }
    }*/

}
