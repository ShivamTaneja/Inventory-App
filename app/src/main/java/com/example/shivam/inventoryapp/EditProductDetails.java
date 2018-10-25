package com.example.shivam.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shivam.inventoryapp.Data.BookStoreContract.BookStoreEntry;

import javax.security.auth.login.LoginException;

public class EditProductDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    EditText editProductName, editPrice, editQuantity, editSupplierName, editSupplierPhoneNumber;
    Button  buttonIncreaseQuantity, buttonDecreaseQuantity, buttonSupplierPhoneNumber;

    String productName, supplierName, supplierPhoneNumber;
    int quantity = 0;
    Double price = 0.0;

    Uri currentUri;
    private static final int EXISTING_LOADER = 0;
    private boolean dataHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            dataHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_details);

        editProductName = findViewById(R.id.editProductName);
        editPrice = findViewById(R.id.editPrice);
        editQuantity = findViewById(R.id.editQuantity);
        editSupplierName = findViewById(R.id.editSupplierName);
        editSupplierPhoneNumber = findViewById(R.id.editSupplierPhoneNumber);

        buttonIncreaseQuantity = findViewById(R.id.IncreaseQuantity);
        buttonDecreaseQuantity = findViewById(R.id.DecreaseQuantity);
        buttonSupplierPhoneNumber = findViewById(R.id.buttonSupplierPhoneNumber);

        buttonSupplierPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + editSupplierPhoneNumber.getText().toString().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        buttonIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = Integer.valueOf(editQuantity.getText().toString());
                quantity = quantity + 1;
                editQuantity.setText(String.valueOf(quantity));
            }
        });

        buttonDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = Integer.valueOf(editQuantity.getText().toString());
                quantity = quantity - 1;
                if(quantity < 0)
                {
                    quantity = 0;
                }
                editQuantity.setText(String.valueOf(quantity));
            }
        });

        Intent intent = getIntent();
        currentUri = intent.getData();

        getSupportLoaderManager().initLoader(EXISTING_LOADER, null, this);

        editProductName.setOnTouchListener(mTouchListener);
        editPrice.setOnTouchListener(mTouchListener);
        editQuantity.setOnTouchListener(mTouchListener);
        editSupplierName.setOnTouchListener(mTouchListener);
        editSupplierPhoneNumber.setOnTouchListener(mTouchListener);
    }

    @Override
    public void onBackPressed() {
        if (!dataHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void save_changes()
    {
        productName = editProductName.getText().toString().trim();
        try {
            price = Double.parseDouble(editPrice.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), R.string.invalid_price, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            quantity = Integer.parseInt(editQuantity.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), R.string.invlid_quantity, Toast.LENGTH_LONG).show();
            return;
        }

        supplierName = editSupplierName.getText().toString().trim();
        supplierPhoneNumber = editSupplierPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(productName) && TextUtils.isEmpty(price.toString()) &&
                TextUtils.isEmpty(String.valueOf(quantity)) &&
                TextUtils.isEmpty(supplierName) && TextUtils.isEmpty(supplierPhoneNumber))
            return;

        if (TextUtils.isEmpty(productName)) {
            Toast.makeText(this, R.string.missing_name, Toast.LENGTH_SHORT).show();
        } else if (price < 0) {
            Toast.makeText(this, R.string.missing_price, Toast.LENGTH_SHORT).show();
        } else if (quantity < 0) {
            Toast.makeText(this, R.string.missing_quantity, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(supplierName)) {
            Toast.makeText(this, R.string.missing_supplier_name, Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(supplierPhoneNumber)) {
            Toast.makeText(this, R.string.missing_phone_number, Toast.LENGTH_SHORT).show();
        }else if (!TextUtils.isEmpty(supplierPhoneNumber)) {
            long temp = Long.parseLong(supplierPhoneNumber);
            long min_range = Long.parseLong("1000000000");
            long max_range = Long.parseLong("9999999999");
            if (min_range > temp || temp > max_range) {
                Toast.makeText(this, R.string.missing_supplier_phone_number, Toast.LENGTH_SHORT).show();
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(BookStoreEntry.COLUMN_PRODUCT_NAME, productName);
                contentValues.put(BookStoreEntry.COLUMN_PRODUCT_PRICE, price);
                contentValues.put(BookStoreEntry.COLUMN_PRODUCT_QUANTITY, quantity);
                contentValues.put(BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierName);
                contentValues.put(BookStoreEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

                int id = getContentResolver().update(currentUri, contentValues, null, null);
                if (id == -1)
                    Toast.makeText(getApplicationContext(), R.string.data_not_changed, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), R.string.data_changed_successfully, Toast.LENGTH_SHORT).show();

                finish();
            }
        }
    }

    private void delete_record(){
        int id = getContentResolver().delete(currentUri, null, null);
        if (id == -1) {
            Toast.makeText(this, R.string.editor_delete_data_failed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.editor_delete_data_successful, Toast.LENGTH_SHORT).show();
        }
        finish();
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
        }
        catch (NumberFormatException e){}
        supplierName  = cursor.getString(indexSupplierName);
        supplierPhoneNumber = cursor.getString(indexSupplierPhone);

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

    private void showUnsavedChangesDialog( DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete_record();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
                showDeleteConfirmationDialog();
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
