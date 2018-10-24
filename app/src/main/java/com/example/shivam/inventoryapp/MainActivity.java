package com.example.shivam.inventoryapp;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shivam.inventoryapp.Data.BookStoreContract.BookStoreEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOOK_LOADER = 0;
    private BookInventoryCursorAdapter bookInventoryCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InsertProduct.class);
                startActivity(intent);
            }
        });

        View emptyView = findViewById(R.id.empty_view);
        ListView listView = findViewById(R.id.listview);
        listView.setEmptyView(emptyView);

        bookInventoryCursorAdapter = new BookInventoryCursorAdapter(this, null);
        listView.setAdapter(bookInventoryCursorAdapter);
        getSupportLoaderManager().initLoader(BOOK_LOADER, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EditProductDetails.class);
                Uri currentUri = ContentUris.withAppendedId(BookStoreEntry.CONTENT_URI, id);
                intent.setData(currentUri);
                startActivity(intent);
            }
        });
    }

    private void delete_record(){
        int id = getContentResolver().delete(BookStoreEntry.CONTENT_URI, null, null);
        if (id == -1) {
            Toast.makeText(this, R.string.editor_delete_data_failed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.editor_delete_data_successful, Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_data);
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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {BookStoreEntry._ID, BookStoreEntry.COLUMN_PRODUCT_NAME, BookStoreEntry.COLUMN_PRODUCT_PRICE};
        return new CursorLoader(this, BookStoreEntry.CONTENT_URI, projection,
                null, null, null);
    }
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        bookInventoryCursorAdapter.swapCursor(cursor);
    }
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        bookInventoryCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_screen_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.deleteAllRecord:
                showDeleteConfirmationDialog();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
