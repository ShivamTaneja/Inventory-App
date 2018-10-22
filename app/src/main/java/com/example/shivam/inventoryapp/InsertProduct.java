package com.example.shivam.inventoryapp;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import com.example.shivam.inventoryapp.Data.BookStoreContract;

public class InsertProduct extends AppCompatActivity {

    EditText editProductName, editPrice, editQuantity, editSupplierName, editSupplierPhoneNumber;
    String productName="", supplierName="", supplierPhoneNumber="";
    int quantity=0;
    Double price= 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product);

        editProductName = findViewById(R.id.editProductName);
        editPrice = findViewById(R.id.editPrice);
        editQuantity = findViewById(R.id.editQuantity);
        editSupplierName = findViewById(R.id.editSupplierName);
        editSupplierPhoneNumber = findViewById(R.id.editSupplierPhoneNumber);
    }

    private void insert_data()
    {
        productName = editProductName.getText().toString().trim();
        try
        {
            price = Double.parseDouble(editPrice.getText().toString().trim());
            quantity = Integer.parseInt(editQuantity.getText().toString().trim());
        }
        catch (NumberFormatException e){}
        supplierName  = editSupplierName.getText().toString().trim();
        supplierPhoneNumber = editSupplierPhoneNumber.getText().toString().trim();

        ContentValues contentValues = new ContentValues();
        contentValues.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME, productName);
        contentValues.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_PRICE, price);
        contentValues.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        contentValues.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierName);
        contentValues.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

        Uri uri = getContentResolver().insert(BookStoreContract.BookStoreEntry.CONTENT_URI,contentValues);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.insert_product_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.insert_data:
                insert_data();
                break;
            case R.id.discard_changes:
                finish();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
