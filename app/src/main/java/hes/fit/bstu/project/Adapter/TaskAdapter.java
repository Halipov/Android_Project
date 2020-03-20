package hes.fit.bstu.project.Adapter;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hes.fit.bstu.project.Activities.MainActivity;
import hes.fit.bstu.project.Database.TodoDatabaseHelper;
import hes.fit.bstu.project.R;
import hes.fit.bstu.project.Model.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TasksViewHolder>{
    private List<Task> notes;
    private OnTaskClickListener onTaskClickListener;
    private Task task;
    TodoDatabaseHelper dbHelper;
    SQLiteDatabase database;
    public TaskAdapter(ArrayList<Task> notes) {
        this.notes = notes;
    }

    public interface OnTaskClickListener {
        void onTaskClick(int position);
        void onLongClick(int position);
    }

    public void setOnTaskClickListener(OnTaskClickListener onTaskClickListener) {
        this.onTaskClickListener = onTaskClickListener;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_item, viewGroup, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder notesViewHolder, int i) {
        Task note = notes.get(i);
        silentUpdate = true;
        notesViewHolder.textViewTitle.setText(note.getCategory());
        notesViewHolder.textViewDescription.setText(note.getTitle());
        task = new Task();
        task = notes.get(i);
        //notesViewHolder.textViewDayOfWeek.setText(getDayAsString(note.getDayOfWeek() + 1));
        int colorId;
        int priority = note.getPriority();
        switch (priority) {
            case 1:
                colorId = notesViewHolder.itemView.getResources().getColor(android.R.color.holo_red_light);
                break;
            case 2:
                colorId = notesViewHolder.itemView.getResources().getColor(android.R.color.holo_orange_light);
                break;
            default:
                colorId = notesViewHolder.itemView.getResources().getColor(android.R.color.holo_green_light);
                break;
        }
        notesViewHolder.textViewTitle.setBackgroundColor(colorId);
        if (note.getStatus().equals("Complete")){
            notesViewHolder.completed.setChecked(true);
            notesViewHolder.updateStrokeOut();
        }
        if(note.getStatus() == "running") {
            notesViewHolder.completed.setChecked(false);
            notesViewHolder.updateStrokeOut();
        }
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }
    private boolean silentUpdate;
    class TasksViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewDayOfWeek;

        CheckBox completed;


        public TasksViewHolder(@NonNull final View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDayOfWeek = itemView.findViewById(R.id.textViewDayOfWeek);
            completed = itemView.findViewById(R.id.completed);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTaskClickListener != null) {
                        onTaskClickListener.onTaskClick(getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onTaskClickListener != null) {
                        onTaskClickListener.onLongClick(getAdapterPosition());
                    }
                    return true;
                }
            });
            completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                 if(compoundButton.isChecked()){
                     task.setStatus("Complete");
                     updateStrokeOut();
                     String s = String.valueOf(task.getId());
                     textViewDayOfWeek.setText(s);
                     MainActivity.updDB_complete(task.getId());
                 }
                 else {
                     task.setStatus("running");
                     MainActivity.updDB_running(task.getId());
                     updateStrokeOut();
                 }
                }
            });
        }
        public void updateStrokeOut(){
                if (task.getStatus().equals("Complete")) {
                    textViewDescription.setPaintFlags(textViewDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    textViewDescription.setPaintFlags(textViewDescription.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
    }
    public static String getDayAsString(int position) {
        switch (position) {
            case 1:
                return "Понедельник";
            case 2:
                return "Вторник";
            case 3:
                return "Среда";
            case 4:
                return "Четверг";
            case 5:
                return "Пятинца";
            case 6:
                return "Суббота";
            default:
                return "Воскресенье";
        }
    }

    public void updateComplete(int i){
        database = dbHelper.getWritableDatabase();
        dbHelper.status_update_complete(database, i);
        database.close();
    }

    public void updateRunning(int i){
        database = dbHelper.getWritableDatabase();
        dbHelper.status_update_running(database, i);
        database.close();
    }

    public void setNotes(List<Task> notes) {
        this.notes = notes;
        //notifyDataSetChanged();
    }

    public List<Task> getNotes() {
        return notes;
    }
}
