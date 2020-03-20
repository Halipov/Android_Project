package hes.fit.bstu.project;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import hes.fit.bstu.project.Model.Category;
import hes.fit.bstu.project.Model.Task;

public class App extends Application {
    private static App app;
    private ArrayList<Task> tasks;
    private final String FILE_NAME_JSON = "PROG_Laba_XZ.json";
    public App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate( );

        app = this;
        app.initialize( );
    }

    private void initialize() {
        tasks = new ArrayList<>( );
    }

    public List<Task> getPersons() {
        return tasks;
    }
    public String getJsonFileName() {
        return FILE_NAME_JSON;
    }
}
