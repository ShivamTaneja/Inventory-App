package com.example.shivam.inventoryapp;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shivam.inventoryapp.Data.BookStoreContract;

public class EditProductDetails extends AppCompatActivity {

    EditText editProductName, editPrice, editQuantity, editSupplierName, editSupplierPhoneNumber,
             editIncreaseQuantity, editDecreaseQuantity;
    Button buttonSupplierPhoneNumber;

    String productName="", supplierName="", supplierPhoneNumber="";
    int quantity=0, increaseQuantityBy=0, decreaseQuantityBy=0;
    Double price= 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_details);

        editProductName = findViewById(R.id.editProductName);
        editPrice = findViewById(R.id.editPrice);
        editQuantity = findViewById(R.id.editQuantity);
        editSupplierName = findViewById(R.id.editSupplierName);
        editSupplierPhoneNumber = findViewById(R.id.editSupplierPhoneNumber);
        editIncreaseQuantity = findViewById(R.id.editIncreaseQuantity);
        editDecreaseQuantity = findViewById(R.id.editDecreaseQuantity);

        buttonSupplierPhoneNumber = findViewById(R.id.buttonSupplierPhoneNumber);

    }
    private void save_changes() {

        productName = editProductName.getText().toString().trim();
        try
        {
            price = Double.parseDouble(editPrice.getText().toString().trim());
            quantity = Integer.parseInt(editQuantity.getText().toString().trim());
            increaseQuantityBy = Integer.parseInt(editIncreaseQuantity.getText().toString().trim());
            decreaseQuantityBy = Integer.parseInt(editDecreaseQuantity.getText().toString().trim());
        }
        catch (NumberFormatException e){}
        supplierName  = editSupplierName.getText().toString().trim();
        supplierPhoneNumber = editSupplierPhoneNumber.getText().toString().trim();

        quantity = quantity + increaseQuantityBy - decreaseQuantityBy;
        ContentValues contentValues = new ContentValues();
        contentValues.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_NAME, productName);
        contentValues.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_PRICE, price);
        contentValues.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        contentValues.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierName);
        contentValues.put(BookStoreContract.BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

        Uri uri = getContentResolver().insert(BookStoreContract.BookStoreEntry.CONTENT_URI, contentValues );

        if (uri == null) {
            Toast.makeText(this, R.string.data_not_changed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.data_changed_successfully, Toast.LENGTH_SHORT).show();
        }

    }
    private void delete_record()
    {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_screen_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.saveChanges:
                save_changes();
                break;
            case R.id.discardChanges:
                finish();
                break;
            case R.id.deleteRecord:
                delete_record();
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
