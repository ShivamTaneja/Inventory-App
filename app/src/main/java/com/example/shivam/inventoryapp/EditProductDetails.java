package com.example.shivam.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.shivam.inventoryapp.Data.BookStoreContract.BookStoreEntry;

public class EditProductDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    EditText editProductName, editPrice, editQuantity, editSupplierName, editSupplierPhoneNumber,
             editIncreaseQuantity, editDecreaseQuantity;
    Button buttonSupplierPhoneNumber;

    String productName="", supplierName="", supplierPhoneNumber="";
    int quantity=0, increaseQuantityBy=0, decreaseQuantityBy=0;
    Double price= 0.0;

    Uri currentUri;
    private static final int EXISTING_LOADER = 0;

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

        Intent intent = getIntent();
        currentUri = intent.getData();

        getSupportLoaderManager().initLoader(EXISTING_LOADER, null, this);
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

        ContentValues contentValues = new ContentValues();
        contentValues.put(BookStoreEntry.COLUMN_PRODUCT_NAME, productName);
        contentValues.put(BookStoreEntry.COLUMN_PRODUCT_PRICE, price);
        contentValues.put(BookStoreEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        contentValues.put(BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierName);
        contentValues.put(BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

        int id = getContentResolver().update(currentUri, contentValues, null, null );

        if (id == -1) {
            Toast.makeText(this, R.string.data_not_changed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.data_changed_successfully, Toast.LENGTH_SHORT).show();
        }

    }

    private void delete_record(){

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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader(this, currentUri, null,null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
       int indexName =  cursor.getColumnIndex(BookStoreEntry.COLUMN_PRODUCT_NAME);
       int indexPrice =  cursor.getColumnIndex(BookStoreEntry.COLUMN_PRODUCT_PRICE);
       int indexQuantity = cursor.getColumnIndex(BookStoreEntry.COLUMN_PRODUCT_QUANTITY);
       int indexSupplierName =  cursor.getColumnIndex(BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
       int indexSupplierPhone =  cursor.getColumnIndex(BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

        productName = cursor.getString(indexName);
        try
        {
            price = cursor.getDouble(indexPrice);
            quantity = cursor.getInt(indexQuantity);
            increaseQuantityBy = Integer.parseInt(editIncreaseQuantity.getText().toString().trim());
            decreaseQuantityBy = Integer.parseInt(editDecreaseQuantity.getText().toString().trim());
        }
        catch (NumberFormatException e){}
        supplierName  = cursor.getString(indexSupplierName);
        supplierPhoneNumber = cursor.getString(indexSupplierPhone);

        quantity = quantity + increaseQuantityBy - decreaseQuantityBy;

       editProductName.setText(productName);
       editPrice.setText(price.toString());
       editQuantity.setText(String.valueOf(quantity));
       editSupplierName.setText(supplierName);
       editSupplierPhoneNumber.setText(supplierPhoneNumber);
    }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        editProductName.setText("");
        editPrice.setText("");
        editQuantity.setText("");
        editSupplierName.setText("");
        editSupplierPhoneNumber.setText("");
    }
}
