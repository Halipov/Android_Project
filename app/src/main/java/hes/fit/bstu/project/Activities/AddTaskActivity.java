package hes.fit.bstu.project.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

import hes.fit.bstu.project.Database.TodoDatabaseHelper;
import hes.fit.bstu.project.R;

public class AddTaskActivity extends AppCompatActivity {
    EditText title,description;
    SQLiteDatabase db;
    TodoDatabaseHelper dbHelper;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        dbHelper = new TodoDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        init();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getCategory());
        spinner.setAdapter(adapter);
    }
    public void init(){
        spinner = findViewById(R.id.spinner);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
    }

    protected ArrayList<String> getCategory() {
        ArrayList<String> data = new ArrayList<>();
        String query = "select category as Category from Categories";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int subjectIndex = cursor.getColumnIndex("Category");
            do {
                data.add(cursor.getString(subjectIndex));
            } while (cursor.moveToNext());
        }
        return data;
    }

    public void onClick(View view) {
       // ContentValues cv = new ContentValues();
        dbHelper.addTask(db, spinner.getSelectedItemPosition()+1, title.getText().toString(),description.getText().toString());
        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
