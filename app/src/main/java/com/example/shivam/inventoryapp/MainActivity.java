package com.example.shivam.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.shivam.inventoryapp.Data.BookStoreContract.BookStoreEntry;
import com.example.shivam.inventoryapp.Data.BookStoreDataBaseHelper;

public class MainActivity extends AppCompatActivity {

    EditText  editProductName, editPrice, editQuantity, editSupplierName, editSupplierPhoneNumber;
    TextView textDisplay;
    String productName="", supplierName="", supplierPhoneNumber="";
    int quantity=0;
    Double price= Double.valueOf(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editProductName = findViewById(R.id.editProductName);
        editPrice = findViewById(R.id.editPrice);
        editQuantity = findViewById(R.id.editQuantity);
        editSupplierName = findViewById(R.id.editSupplierName);
        editSupplierPhoneNumber = findViewById(R.id.editSupplierPhoneNumber);
        textDisplay = findViewById(R.id.textDisplay);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DisplayDatabaseInfo();
    }

    private void DisplayDatabaseInfo() {
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
    }

    private void insertData()
    {
        productName = editProductName.getText().toString().trim();
        try
        {
            price = Double.parseDouble(editPrice.getText().toString());
            quantity = Integer.parseInt(editQuantity.getText().toString());
        }
        catch (NumberFormatException e){}
        supplierName  = editSupplierName.getText().toString().trim();
        supplierPhoneNumber = editSupplierPhoneNumber.getText().toString().trim();

        BookStoreDataBaseHelper mBookStoreDataBaseHelper = new BookStoreDataBaseHelper(this);
        SQLiteDatabase msqLiteDatabase = mBookStoreDataBaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(BookStoreEntry.COLUMN_PRODUCT_NAME, productName);
        contentValues.put(BookStoreEntry.COLUMN_PRODUCT_PRICE, price);
        contentValues.put(BookStoreEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        contentValues.put(BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierName);
        contentValues.put(BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

        long newRowId = msqLiteDatabase.insert(BookStoreEntry.TABLE_NAME, null , contentValues);

        if (newRowId == -1) {
            Toast.makeText(this, "Error with saving ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.insert_display,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.insert:
                insertData();
                break;
            case R.id.display:
                DisplayDatabaseInfo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
