package com.example.shivam.inventoryapp;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shivam.inventoryapp.Data.BookStoreContract;

public class InsertProduct extends AppCompatActivity {

    EditText editProductName, editPrice, editQuantity, editSupplierName, editSupplierPhoneNumber;
    String productName, supplierName, supplierPhoneNumber;
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
        try {
            price = Double.parseDouble(editPrice.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), R.string.invalid_price, Toast.LENGTH_LONG).show();
            return;
        }

        try{
        quantity = Integer.parseInt(editQuantity.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), R.string.invlid_quantity, Toast.LENGTH_LONG).show();
            return;
        }

        supplierName = editSupplierName.getText().toString().trim();
        supplierPhoneNumber = editSupplierPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(productName)) {
            Toast.makeText(this, R.string.missing_name, Toast.LENGTH_SHORT).show();
        } else if (price < 0) {
            Toast.makeText(this, R.string.missing_price, Toast.LENGTH_SHORT).show();
        } else if (quantity < 0) {
            Toast.makeText(this, R.string.missing_quantity, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(supplierName)) {
            Toast.makeText(this, R.string.missing_supplier_name, Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(supplierPhoneNumber)) {
            Toast.makeText(this, R.string.missing_phone_number, Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isEmpty(supplierPhoneNumber)) {
            long temp = Long.parseLong(supplierPhoneNumber);
            long min_range = Long.parseLong("1000000000");
            long max_range = Long.parseLong("9999999999");
            if (min_range > temp || temp > max_range)
                Toast.makeText(this, R.string.missing_supplier_phone_number, Toast.LENGTH_SHORT).show();
            else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME, productName);
                contentValues.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_PRICE, price);
                contentValues.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_QUANTITY, quantity);
                contentValues.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierName);
                contentValues.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

                Uri uri = getContentResolver().insert(BookStoreContract.BookStoreEntry.CONTENT_URI, contentValues);
                if (uri == null)
                    Toast.makeText(getApplicationContext(), R.string.data_not_inserted, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), R.string.data_inserted, Toast.LENGTH_SHORT).show();

                finish();
            }
        }
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
