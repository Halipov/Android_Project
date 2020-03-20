package hes.fit.bstu.project.Database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TodoTable {
    public static class CategoryTable{
        public  static final String TABLE_NAME = "Categories";
        public static class Columns{
            public static final String COLUMN_ID = "_id";
            public static final String COLUMN_CATEGORY = "category";
            public static final String COLUMN_COLOR = "color";
        }
    }
    public static class TaskTable{
        public  static final String TABLE_NAME = "Tasks";
        public static class Columns{
            public static final String COLUMN_CATEGORY_ID = "_id";
            public static final String COLUMN_ID = "idTask";
            public static final String COLUMN_TITLE = "Title";
            public static final String COLUMN_DESCRIPTION = "Description";
            public static final String COLUMN_STATUS = "Status";
        }

    }

}
