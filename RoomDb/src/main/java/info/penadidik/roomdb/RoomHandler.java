package info.penadidik.roomdb;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Todo.class}, version = 2, exportSchema = false)
public abstract class RoomHandler extends RoomDatabase {

    public abstract DaoAccess daoAccess();

    private static RoomHandler roomHandler;

    public static final String DATABASE_NAME = "app_room_db";

    private static final String LOG_TAG = RoomHandler.class.getSimpleName();

}
