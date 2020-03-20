package hes.fit.bstu.project.ContentProvider;

import android.content.ContentUris;
import android.net.Uri;

public class TaskContract {
    static final String TABLE_NAME = "tasks";
    static final String CONTENT_AUTHORITY = "hes.fit.bstu.project.ContentProvider";
    static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
    static final String CONTENT_ITEM_TYPE= "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;

    public static class Columns{
        public static final String _ID = "_id";
        public static final String IDTASK = "idTask";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";

        private Columns(){

        }
    }
    static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME);
    // создает uri с помощью id
    static Uri buildTaskUri(long taskId){
        return ContentUris.withAppendedId(CONTENT_URI, taskId);
    }
    // получает id из uri
    static long getTaskId(Uri uri){
        return ContentUris.parseId(uri);
    }
}
