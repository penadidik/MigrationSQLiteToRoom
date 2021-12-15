package info.penadidik.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SqlHandler {

    // static variable
    private static final int DATABASE_VERSION = 1;

    private SqlHelper dbHelper;

    private String TABLE_NAME_TODO = "todo";

    private Context context;

    private SQLiteDatabase database;

    // column tables
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC = "description";
    private static final String KEY_CATEGORY = "category";

    public SqlHandler(Context context){
        this.context = context;
    }

    public SqlHandler open() throws SQLException {
        dbHelper = new SqlHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long addRecord(TodoSqlite userModels){
        SQLiteDatabase db  = database;

        ContentValues values = new ContentValues();
        values.put(KEY_ID, userModels.getTodo_id());
        values.put(KEY_NAME, userModels.getName());
        values.put(KEY_DESC, userModels.getDescription());
        values.put(KEY_CATEGORY, userModels.getCategory());

        db.insert(TABLE_NAME_TODO, null, values);
//        db.close();
        return userModels.getTodo_id();
    }

    public void addRecordList(List<TodoSqlite> list){
        SQLiteDatabase db  = database;

        for(TodoSqlite userModels: list){
            ContentValues values = new ContentValues();
            values.put(KEY_ID, userModels.getTodo_id());
            values.put(KEY_NAME, userModels.getName());
            values.put(KEY_DESC, userModels.getDescription());
            values.put(KEY_CATEGORY, userModels.getCategory());

            db.insert(TABLE_NAME_TODO, null, values);
        }

//        db.close();

    }

    public TodoSqlite getContact(int id) {
        SQLiteDatabase db = database;

        Cursor cursor = db.query(TABLE_NAME_TODO, new String[] { KEY_ID,
                        KEY_NAME, KEY_DESC, KEY_CATEGORY }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TodoSqlite contact = new TodoSqlite(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return contact
        return contact;
    }

    public List<TodoSqlite> getAllByCategory(String category) {
        List<TodoSqlite> contactList = new ArrayList<TodoSqlite>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_TODO + "LIKE" + category;

        SQLiteDatabase db = database;

        Cursor cursor = db.query(true, TABLE_NAME_TODO, new String[] { KEY_ID,
                        KEY_NAME, KEY_DESC, KEY_CATEGORY }, KEY_CATEGORY + " LIKE ?",
                new String[] {"%"+ category+ "%" }, null, null, null,
                null);

        if (cursor.moveToFirst()) {
            do {
                TodoSqlite userModels = new TodoSqlite();
                userModels.setId(Integer.parseInt(cursor.getString(0)));
                userModels.setName(cursor.getString(1));
                userModels.setDescription(cursor.getString(2));
                userModels.setCategory(cursor.getString(3));

                contactList.add(userModels);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // get All Record
    public List<TodoSqlite> getAllRecord() {
        List<TodoSqlite> contactList = new ArrayList<TodoSqlite>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_TODO;

        SQLiteDatabase db = database;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TodoSqlite userModels = new TodoSqlite();
                userModels.setId(Integer.parseInt(cursor.getString(0)));
                userModels.setName(cursor.getString(1));
                userModels.setDescription(cursor.getString(2));
                userModels.setCategory(cursor.getString(3));

                contactList.add(userModels);
            } while (cursor.moveToNext());
        }

        // return contact list
//        db.close();
        return contactList;
    }

    public int getUserModelCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_TODO;
        SQLiteDatabase db = database;
        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();

        // return count
        return cursor.getCount();
    }

    public int updateContact(TodoSqlite contact) {
        SQLiteDatabase db = database;

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_DESC, contact.getDescription());
        values.put(KEY_CATEGORY, contact.getCategory());

        // updating row
        return db.update(TABLE_NAME_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getTodo_id()) });
    }

    public Integer deleteModel(TodoSqlite contact) {
        SQLiteDatabase db = database;
        db.delete(TABLE_NAME_TODO, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getTodo_id()) });
//        db.close();
        return contact.getTodo_id();
    }

}
