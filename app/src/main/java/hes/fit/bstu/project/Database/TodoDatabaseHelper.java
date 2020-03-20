package hes.fit.bstu.project.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.widget.Toast;

import hes.fit.bstu.project.Activities.MainActivity;

public class TodoDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "todotable.db";
	private static final int DATABASE_VERSION = 1;

	public TodoDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TodoTable.CategoryTable.TABLE_NAME + " ( "
                + TodoTable.CategoryTable.Columns.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TodoTable.CategoryTable.Columns.COLUMN_CATEGORY + " TEXT NOT NULL, "
                + TodoTable.CategoryTable.Columns.COLUMN_COLOR + " INTEGER );");

        database.execSQL("CREATE TABLE IF NOT EXISTS " + TodoTable.TaskTable.TABLE_NAME + " ( "
                + TodoTable.TaskTable.Columns.COLUMN_CATEGORY_ID + " INTEGER NOT NULL, "
                + TodoTable.TaskTable.Columns.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TodoTable.TaskTable.Columns.COLUMN_TITLE + " TEXT NOT NULL, "
                + TodoTable.TaskTable.Columns.COLUMN_STATUS + " TEXT NOT NULL, "
                + TodoTable.TaskTable.Columns.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                +" FOREIGN KEY("+ TodoTable.TaskTable.Columns.COLUMN_CATEGORY_ID + ") REFERENCES " + TodoTable.CategoryTable.TABLE_NAME + "("
                + TodoTable.TaskTable.Columns.COLUMN_CATEGORY_ID + ") ON DELETE CASCADE ON UPDATE CASCADE);");
        init(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE " + TodoTable.TaskTable.TABLE_NAME + ";");
        database.execSQL("DROP TABLE " + TodoTable.CategoryTable.TABLE_NAME + ";");
        onCreate(database);
	}

    public void addTask(SQLiteDatabase db, int idCategory, String title, String description)
    {
        ContentValues cv = new ContentValues();
        cv.put("_id", idCategory);
        cv.put("Title", title);
        cv.put("Description", description);
        cv.put("Status", "running");
        db.insert(TodoTable.TaskTable.TABLE_NAME,null, cv);
        cv.clear();
        //cv.put("D", idCategory);
    }

    public void addCategory(SQLiteDatabase db, String category, int color)
    {
        ContentValues cv = new ContentValues();
        cv.put("category", category);
        cv.put("color", color);
        db.insert(TodoTable.CategoryTable.TABLE_NAME,null,cv);
        cv.clear();
    }

    public void deleteTask(SQLiteDatabase database, int id)
    {
        database.delete(TodoTable.TaskTable.TABLE_NAME,"idTask = "+ id,null);
    }
    public void deleteCategory(SQLiteDatabase database, int id)
    {
        database.delete(TodoTable.CategoryTable.TABLE_NAME, "_id = "+ id,null);
    }

    public void init(SQLiteDatabase db)
    {
        addCategory(db,"Универ",1);
        addCategory(db, "Работа",2);
        addTask(db, 1,"Сделать проект","По бд");
        addTask(db, 1,"Сделать лабу","По бд");
    }
    public void status_update_complete(SQLiteDatabase db, int id)
    {
        String strID = String.valueOf(id);
        ContentValues cv = new ContentValues();
        cv.put("Status", "Complete");
        db.update(TodoTable.TaskTable.TABLE_NAME, cv, "IdTask = ?", new String[]{strID});
    }
    public void status_update_running(SQLiteDatabase db, int id)
    {
        String strID = String.valueOf(id);
        ContentValues cv = new ContentValues();
        cv.put("Status", "running");
        db.update(TodoTable.TaskTable.TABLE_NAME, cv, "IdTask = ?", new String[]{strID});
    }

//select Tasks.Title, Tasks.Description, Categories.category  from Tasks join Categories on Categories._id = Tasks._id
}
