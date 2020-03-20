package hes.fit.bstu.project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hes.fit.bstu.project.Model.Category;
import hes.fit.bstu.project.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private CategoryAdapter.OnCategoryClickListener onTaskClickListener;
    private List<Category> categoryList;

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
    public interface OnCategoryClickListener {
        void onCategoryClick(int position);
        void onLongClick(int position);
    }
    public void setOnCategoryClickListener(CategoryAdapter.OnCategoryClickListener onTaskClickListener) {
        this.onTaskClickListener = onTaskClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        //notesViewHolder.textViewDayOfWeek.setText(getDayAsString(note.getDayOfWeek() + 1));
        holder.textViewTitle.setText(category.getTitle());
        int colorId;
        int color = category.getColor();
        switch (color) {
            case 1:
                colorId = holder.itemView.getResources().getColor(android.R.color.holo_red_light);
                break;
            case 2:
                colorId = holder.itemView.getResources().getColor(android.R.color.holo_orange_light);
                break;
            default:
                colorId = holder.itemView.getResources().getColor(android.R.color.holo_green_light);
                break;
        }
        holder.textViewTitle.setBackgroundColor(colorId);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;


        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTaskClickListener != null) {
                        onTaskClickListener.onCategoryClick(getAdapterPosition());
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

        }
    }
}
