package hes.fit.bstu.project.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import hes.fit.bstu.project.Adapter.CategoryAdapter;
import hes.fit.bstu.project.Adapter.JsonConverter;
import hes.fit.bstu.project.Adapter.TaskAdapter;
import hes.fit.bstu.project.App;
import hes.fit.bstu.project.Model.Category;
import hes.fit.bstu.project.Database.TodoDatabaseHelper;
import hes.fit.bstu.project.R;
import hes.fit.bstu.project.Model.Task;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    static TodoDatabaseHelper dbHelper;
    static SQLiteDatabase database;
    public App app;
    NavigationView navigationView;
    RecyclerView recyclerView;
    TaskAdapter adapter;
    FileWriter fw;
    FileReader fr;
    File file;
    Button btn_save,btn_import;
    boolean flag = true;
    CategoryAdapter categoryAdapter;
    private final ArrayList<Task> tasks = new ArrayList<>();
    private final ArrayList<Category> categories = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.design_navigation_view);
        setupDrawerContent(navigationView);
        recyclerView = findViewById(R.id.recyclerViewNotes);
        btn_save= findViewById(R.id.btn_save);
        btn_import = findViewById(R.id.btn_import);
        btn_save.setVisibility(View.GONE);
        btn_import.setVisibility(View.GONE);

        //recyclerView.setLayoutManager(new GridLayoutManager(this,2,LinearLayoutManager.VERTICAL,false));

        getTask();

    }
    public void selectItemDrawer(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.category:

                getCategory();
                break;
            case R.id.all_tasks:

                getTask();
                break;
            case R.id.rezerv:
                initFile();
                rezerv();
                break;

        }
    }
    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectItemDrawer(menuItem);
                return false;
            }
        });
    }
    private void rezerv(){
        recyclerView.setVisibility(View.GONE);
        btn_import.setVisibility(View.VISIBLE);
        btn_save.setVisibility(View.VISIBLE);
        tasks.clear();

    }
    private void initFile() {
        file = new File( super.getFilesDir( ), ( ( App ) getApplication( ) ).getJsonFileName( ) );
        if ( file.exists( ) ) {
            if ( loadFromFile( ) && !tasks.isEmpty( ) ) {
            }
        } else {
            Toast.makeText( getApplicationContext( ), "Файл не найден", Toast.LENGTH_SHORT ).show( );
            try {
                file.createNewFile( );
                Toast.makeText( getApplicationContext( ), "Файл создан", Toast.LENGTH_SHORT ).show( );
            }
            catch ( IOException e ) {
                Log.d( "json", Objects.requireNonNull( e.getMessage( ) ) );
                e.printStackTrace( );
            }
        }
    }

    private boolean loadFromFile() {
        GsonBuilder builder = new GsonBuilder( );
        builder.registerTypeAdapter( Task.class, new JsonConverter( ) );
        Gson gson = builder.create( );
        tasks.clear( );

        try {
            fr = new FileReader( file );
            BufferedReader reader = new BufferedReader( fr );
            reader.skip( 13 );
            String jsonStr = reader.readLine( );

            while ( jsonStr != null && !jsonStr.equals( "]}" ) ) {
                if ( ',' == ( jsonStr.charAt( jsonStr.length( ) - 1 ) ) ) {
                    jsonStr = jsonStr.substring( 0, jsonStr.length( ) - 1 );
                }
                Log.d( "json", jsonStr );
                Task _p = gson.fromJson( jsonStr, Task.class );
                dbHelper.addTask(database,_p.getIdCategory(),_p.getTitle(),_p.getDescription());
                tasks.add( _p );
                jsonStr = reader.readLine( );
            }
            btn_import.setVisibility(View.GONE);
            btn_save.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new TaskAdapter(tasks);
            recyclerView.setAdapter(adapter);
            Toast.makeText( this, "Данные восстановлены", Toast.LENGTH_LONG ).show( );
            return true;
        }
        catch ( IOException e ) {
            Log.e( "json", Objects.requireNonNull( e.getMessage( ) ), e.fillInStackTrace( ) );
            e.printStackTrace( );
            return false;
        }
    }

    private void saveToFile() {

        Gson gson = new GsonBuilder( ).registerTypeAdapter( Task.class, new JsonConverter( ) ).create( );
        String jsonStr;
        if ( tasks != null && tasks.size( ) > 0 ) {
            try {
                fw = new FileWriter( file, false );
                fw.write( "{\"persons\":[\n" );
                for ( Task n : tasks ) {
                    jsonStr = gson.toJson( n, Task.class );
                    fw.write( jsonStr );
                    if ( tasks.indexOf( n ) != ( tasks.size( ) - 1 ) ) {
                        fw.append( ",\n" );
                    }
                }
                fw.write( "\n]}" );
                fw.flush( );
                Toast.makeText( getApplicationContext( ), "Файл сохранен", Toast.LENGTH_SHORT ).show( );
            }
            catch ( IOException e ) {
                Log.d( "json_", Objects.requireNonNull( e.getMessage( ) ) );
                Toast.makeText( this, e.getMessage( ), Toast.LENGTH_SHORT ).show( );
            }
            finally {
                try {
                    if ( fw != null ) {
                        fw.close( );
                        fw = null;
                    }
                }
                catch ( IOException ex ) {
                    Log.d( "json", Objects.requireNonNull( ex.getMessage( ) ) );
                    Toast.makeText( this, ex.getMessage( ), Toast.LENGTH_SHORT ).show( );
                }
            }
        }

    }
    public static void updDB_complete(int i){
        database = dbHelper.getWritableDatabase();
        dbHelper.status_update_complete(database, i);
        database.close();
    }
    public static void updDB_running(int i){
        database = dbHelper.getWritableDatabase();
        dbHelper.status_update_running(database, i);
        database.close();
    }

    private void getTask() {
        btn_save.setVisibility(View.GONE);
        btn_import.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        flag = true;
        dbHelper = new TodoDatabaseHelper(this);
        database =dbHelper.getWritableDatabase();
        tasks.clear();
        String query = "select Tasks.idTask, Tasks.Title, Tasks.Description, Categories.category, Categories.color, Categories._id, Tasks.Status  from Tasks join Categories on Categories._id = Tasks._id";
        Cursor cursor = database.rawQuery(query,null);
        if (cursor.moveToFirst()) {
        do {
        Task task = new Task();
        task.setId(cursor.getInt(0));
        task.setTitle(cursor.getString(1));
        task.setDescription(cursor.getString(2));
        task.setCategory(cursor.getString(3));
        task.setPriority(cursor.getInt(4));
        task.setIdCategory(cursor.getInt(5));
        task.setStatus(cursor.getString(6));
        tasks.add(task);
        }while (cursor.moveToNext());
    } else {
        Toast.makeText(this, "nothing", Toast.LENGTH_SHORT).show();
    }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(tasks);
        recyclerView.setAdapter(adapter);
        cursor.close();
        database.close();

        adapter.setOnTaskClickListener(new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(int position) {
                Toast.makeText(MainActivity.this, "clicked" + tasks.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                database = dbHelper.getWritableDatabase();
                if(flag){
                    dbHelper.deleteTask(database, tasks.get(viewHolder.getAdapterPosition()).getId());
                    getTask();
                }
                else{
                    dbHelper.deleteCategory(database,categories.get(viewHolder.getAdapterPosition()).getId());
                    getCategory();
                }

                Toast.makeText(MainActivity.this, "Deleted ", Toast.LENGTH_SHORT).show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void getCategory(){
        btn_save.setVisibility(View.GONE);
        btn_import.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        flag = false;
        dbHelper = new TodoDatabaseHelper(this);
        database =dbHelper.getWritableDatabase();
        categories.clear();
        Cursor cursor = database.query("Categories",null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(0));
                category.setTitle(cursor.getString(1));
                category.setColor(cursor.getInt(2));
                categories.add(category);
            }while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "nothing", Toast.LENGTH_SHORT).show();
        }
        //recyclerView.setLayoutManager(new GridLayoutManager(this,2,LinearLayoutManager.VERTICAL,false));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        categoryAdapter = new CategoryAdapter(categories);
        recyclerView.setAdapter(categoryAdapter);
        cursor.close();
        database.close();

        categoryAdapter.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(int position) {
                Toast.makeText(MainActivity.this, "clicked" + categories.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                database = dbHelper.getWritableDatabase();
                dbHelper.deleteCategory(database, tasks.get(viewHolder.getAdapterPosition()).getId());
                Toast.makeText(MainActivity.this, "Deleted "+ categories.get(viewHolder.getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();
            }
        });
        itemTouchHelper2.attachToRecyclerView(recyclerView);
    }

    public void btn_add(View view) {
        if(flag){
            Intent intent = new Intent(this, AddTaskActivity.class);
            startActivity(intent);
        }else{
            Intent intent2 = new Intent(this,AddCategoryActivity.class);
            startActivity(intent2);
        }

    }

    public void btn_json(View view) {
        switch (view.getId()){
            case R.id.btn_save:
                saveToFile();
                break;
            case R.id.btn_import:
                loadFromFile();
                break;
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.listmenu, menu);
//        return true;
//    }

}
