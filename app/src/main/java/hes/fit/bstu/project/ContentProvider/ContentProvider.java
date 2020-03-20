package hes.fit.bstu.project.ContentProvider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import hes.fit.bstu.project.App;
import hes.fit.bstu.project.Database.TodoDatabaseHelper;
import hes.fit.bstu.project.Model.Task;

public class ContentProvider extends android.content.ContentProvider {

    TodoDatabaseHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static final int TASKS = 100;
    public static final int TASK_ID = 101;
    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        // content://com.metanit.tasktimer.provider/FRIENDS
        matcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.TABLE_NAME, TASKS);
        // content://com.metanit.tasktimer.provider/FRIENDS/8
        matcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.TABLE_NAME + "/#", TASK_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        //mOpenHelper = App.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch(match){
            case TASKS:
                queryBuilder.setTables(TaskContract.TABLE_NAME);
                break;
            case TASK_ID:
                queryBuilder.setTables(TaskContract.TABLE_NAME);
                long taskId = TaskContract.getTaskId(uri);
                queryBuilder.appendWhere(TaskContract.Columns._ID + " = " + taskId);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch(match){
            case TASKS:
                return TaskContract.CONTENT_TYPE;
            case TASK_ID:
                return TaskContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db;
        Uri returnUri;
        long recordId;

        switch(match){
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(TaskContract.TABLE_NAME, null, values);
                if(recordId > 0){
                    returnUri = TaskContract.buildTaskUri(recordId);
                }
                else{
                    throw new android.database.SQLException("Failed to insert: " + uri.toString());
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        String selectionCriteria = selection;

        if(match != TASKS && match != TASK_ID)
            throw new IllegalArgumentException("Unknown URI: "+ uri);

        if(match==TASK_ID) {
            long taskId = TaskContract.getTaskId(uri);
            selectionCriteria = TaskContract.Columns._ID + " = " + taskId;
            if ((selection != null) && (selection.length() > 0)) {
                selectionCriteria += " AND (" + selection + ")";
            }
        }
        return db.delete(TaskContract.TABLE_NAME, selectionCriteria, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
