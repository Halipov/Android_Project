package hes.fit.bstu.project.Model;

public class Category {


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private  int id;
    private String Title;
    private int color;


    public Category(String categoryText, int color) {
        this.Title = categoryText;
        this.color = color;
    }

    public Category() {

    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
