package com.example.patryko.shopping_list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Patryko on 11/24/2017.
 */

public class DatabaseRepository {

    private final SQLiteDatabase database;

    public DatabaseRepository(Context context){
        File mDatabaseFile = context.getDatabasePath("patrykDB.db").getAbsoluteFile();
        database = SQLiteDatabase.openOrCreateDatabase(mDatabaseFile, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS Products(ProductName VARCHAR PRIMARY KEY, Quantity INTEGER, Price NUMERIC, Selected INTEGER);");
    }

    public ArrayList<Product> GetAllItems(){
        Cursor cursor = database.rawQuery("select * from Products", null);
        ArrayList<Product> listResult = new ArrayList<>();

        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                String productName = cursor.getString(cursor.getColumnIndex("ProductName"));
                int quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
                double price = cursor.getDouble(cursor.getColumnIndex("Price"));
                boolean selected = cursor.getInt(cursor.getColumnIndex("Selected")) == 1 ? true : false;
                listResult.add(new Product(productName, quantity, price, selected));
                cursor.moveToNext();
            }
        }

        return listResult;
    }

    public void AddItem(Product product){
        ContentValues insertValues = new ContentValues();
        insertValues.put("ProductName", product.getName());
        insertValues.put("Quantity", product.getQuantity());
        insertValues.put("Price", product.getPrice());
        insertValues.put("Selected", product.isChecked());
        database.insert("Products", null, insertValues);
    }

    public void RemoveProduct(Product productToRemove){
        database.delete("Products", "ProductName = ?", new String[]{productToRemove.getName()});
    }

    public void UpdateProduct(Product product, String productName) {
        ContentValues insertValues = new ContentValues();
        insertValues.put("ProductName", product.getName());
        insertValues.put("Quantity", product.getQuantity());
        insertValues.put("Price", product.getPrice());
        insertValues.put("Selected", product.isChecked());
        database.update("Products", insertValues, "ProductName = ?", new String[]{productName});
    }
}
