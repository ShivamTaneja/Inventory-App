package com.example.shivam.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
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
            case R.id.editDetails:
                Intent intent = new Intent(this, EditProductDetails.class);
                startActivity(intent);
                break;
            case R.id.deleteAllRecord:
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
