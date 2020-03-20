package hes.fit.bstu.project.Adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import hes.fit.bstu.project.Model.Task;

public class JsonConverter implements JsonSerializer<Task>, JsonDeserializer<Task> {
    @Override
    public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        Task task = new Task();
        task.setIdCategory(obj.get("idCategory").getAsInt());
        task.setId( obj.get( "ID" ).getAsInt() );
        task.setTitle( obj.get( "Title" ).getAsString() );
        task.setDescription( obj.get( "Description" ).getAsString() );
        task.setDayOfWeek( obj.get( "DayOfWeek" ).getAsInt() );
        task.setCategory( obj.get( "Category" ).getAsString() );
        task.setPriority( obj.get( "Priority" ).getAsInt() );
        task.setDone( obj.get( "Done" ).getAsBoolean());

        return task;
    }

    @Override
    public JsonElement serialize(Task src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject obj = new JsonObject();
        obj.addProperty("idCategory",src.getIdCategory());
        obj.addProperty( "ID", src.getId() );
        obj.addProperty( "Title", src.getTitle() );
        obj.addProperty( "Description", src.getDescription() );
        obj.addProperty( "DayOfWeek", src.getDayOfWeek() );
        obj.addProperty( "Category", src.getCategory() );
        obj.addProperty( "Priority", src.getPriority() );
        obj.addProperty( "Done", src.done );

        return obj;
    }
}
