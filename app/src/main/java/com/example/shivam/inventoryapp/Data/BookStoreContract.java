package com.example.shivam.inventoryapp.Data;

import android.provider.BaseColumns;

public final class BookStoreContract {

    private BookStoreContract() {
    }

    public final static class BookStoreEntry implements BaseColumns{

        public static final String TABLE_NAME = "ProductInformation";

        public static final String COLUMN_PRODUCT_ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "ProductName";
        public static final String COLUMN_PRODUCT_PRICE = "Price";
        public static final String COLUMN_PRODUCT_QUANTITY = "Quantity";
        public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "SupplierName";
        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER = "SupplierPhoneNumber";
    }
}
