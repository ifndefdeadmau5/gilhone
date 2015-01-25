package com.SeongMin.GoodProduct.global;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class db {

    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";
    private static final String TAG = "NotesDbAdapter";
    /**
     * Database creation sql statement
     */

    private static final String DATABASE_CREATE = "create table if not exists company (_id integer primary key autoincrement, "
            + "title text not null, body text not null);";
    private static final String DATABASE_DROP = "drop table company;";
    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "company";
    private static final int DATABASE_VERSION = 2;
    private final Context mCtx;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public db(Context ctx) {
        this.mCtx = ctx;
    }

    public db open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        //mDb = mDbHelper.getReadableDatabase();

        mDb = mDbHelper.getWritableDatabase();

        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createNote(String title, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteNote(long rowId) {
        Log.i("Delete called", "value__" + rowId);
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllNotes() {
        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE, KEY_BODY}, null, null, null, null, null);
    }

    public Cursor fetchNote(String Code) throws SQLException {

        Cursor mCursor;// = mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_BODY }, KEY_ROWID
        //        + "=" + rowId, null, null, null, null, null);
        mCursor = mDb.rawQuery("SELECT body FROM company WHERE title='" + Code + "'", null);//mDb.rawQuery("SELECT body FROM company", null);
        if (mCursor != null) {
            ;
        }
        return mCursor;
    }

    public boolean updateNote(long rowId, String title, String body) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean isEmpty() {
        Cursor mCursor;
        mCursor = mDb.rawQuery("SELECT count(*) FROM company", null);
        boolean isCursorEmpty = !mCursor.moveToFirst();
        if (mCursor.moveToFirst()) {
            for (; ; ) {
                Log.e("mytag", "테이블 : " + mCursor.getInt(0));
                if (!mCursor.moveToNext())
                    break;
            }
        }


        if (!isCursorEmpty) {
            mCursor.moveToFirst();
            if (mCursor.getInt(0) < 5) {
                return true;
            } else {
                return false;
            }
        }
        Log.i("my", "CursorEmtpy");
        mCursor.close();
        return true;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }
}
