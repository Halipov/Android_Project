package hes.fit.bstu.project.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import hes.fit.bstu.project.Database.TodoDatabaseHelper;
import hes.fit.bstu.project.R;

public class AddCategoryActivity extends AppCompatActivity {
    EditText category;
    SQLiteDatabase db;
    TodoDatabaseHelper dbHelper;
    FloatingActionButton btn_add;
    int idColor = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        dbHelper = new TodoDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        category = findViewById(R.id.category);
        btn_add = findViewById(R.id.fab_add);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_green:
                idColor = 3;
                Toast.makeText(this, "set Green", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_orange:
                idColor = 2;
                Toast.makeText(this, "set Orange", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_red:
                idColor = 1;
                Toast.makeText(this, "set Red", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_add:
                dbHelper.addCategory(db,category.getText().toString(),idColor);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
