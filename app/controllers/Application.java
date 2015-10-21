package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import models.Task;
import org.bson.Document;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;

public class Application extends Controller {
  private static final String DB_NAME = "todojs";
  private static final String COLLECTION_NOTES = "todojs_notes";
  private static final String COLLECTION_TASKS = "todojs_tasks";
  private static final MongoDatabase db = new MongoClient("localhost").getDatabase(DB_NAME);

  public static Result index() {
    return ok(views.html.index.render("Hello my Play Framework"));
  }

  /**
   * Notes
   **/
  public static Result notes() {
    return ok(views.html.todo.render("Hello"));
  }

  public static Result addNote() {
    return ok(views.html.todo.render("Add note"));
  }

  public static Result deleteNote() {
    return ok(views.html.todo.render("Delete note"));
  }

  public static Result editNote() {
    return ok(views.html.todo.render("Edit note"));
  }


  /**
   *  Remove all tasks.
   *  @return 200 OK Json[] task.
   */
  public static Result clearTasks() {
    db.getCollection(COLLECTION_TASKS).drop();
    ArrayList<Task> tasks = new ArrayList<>();
    MongoCursor<Document> cursor = db.getCollection(COLLECTION_TASKS).find().iterator();
    try {
      while (cursor.hasNext())
        tasks.add(Task.documentToTask(cursor.next()));
    } finally {
      cursor.close();
    }

    ObjectNode result = Json.newObject();
    result.put("status", "ok");
    result.put("message", new Gson().toJson(tasks));
    return ok(result);
  }

  /**
   *  Get all tasks.
   *  @return 200 OK Json[] task.
   */
  public static Result tasks() {
    ArrayList<Task> tasks = new ArrayList<>();
    MongoCursor<Document> cursor = db.getCollection(COLLECTION_TASKS).find().iterator();
    try {
      while (cursor.hasNext())
        tasks.add(Task.documentToTask(cursor.next()));
    } finally {
      cursor.close();
    }

    ObjectNode result = Json.newObject();
    result.put("status", "ok");
    result.put("message", new Gson().toJson(tasks));
    return ok(result);
  }

  /**
   *  Inserts a task to db.
   *  @param JSON A complete task.
   *  @return 201 CREATED Json task.
   */
  @BodyParser.Of(BodyParser.Json.class)
  public static Result addTask() {
    JsonNode json = request().body().asJson();
    if (isNull(json)) return badRequest();

    Task task = jsonToTask(json);

    db.getCollection(COLLECTION_TASKS).insertOne(task.toDocument());
    return created(new Gson().toJson(task));
  }

  /**
   *  Insert a task to db.
   *  @param JSON A complete task.
   *  @return 200 OK Json task.
   */
  public static Result deleteTask() {
    JsonNode json = request().body().asJson();
    if (isNull(json)) return badRequest();

    Task task = jsonToTask(json);

    MongoCursor<Document> cursor = db.getCollection(COLLECTION_TASKS).find(task.toDocument()).iterator();
    if (!cursor.hasNext()) return notFound();

    db.getCollection(COLLECTION_TASKS).deleteOne(cursor.next());
    return ok(new Gson().toJson(task));
  }

  /**
   *  Edit an existing task.
   *  @param String _id of task
   *  @param Json Updated task
   *  @return 200 OK Json task.
   */
  public static Result editTask(String id) {
    JsonNode json = request().body().asJson();
    if (isNull(json)) return badRequest();

    System.out.println("id: "+id);

    Task task = jsonToTask(json);
    BasicDBObject query = new BasicDBObject();
    query.put(Task.KEY_ID, id);

    MongoCursor<Document> cursor = db.getCollection(COLLECTION_TASKS).find(query).iterator();
    if (!cursor.hasNext()) return notFound();
    System.out.println(cursor.next().toJson());

    db.getCollection(COLLECTION_TASKS).updateOne(cursor.next(), task.toDocument());
    return ok(new Gson().toJson(task));
  }

  private static boolean isNull(Object object) {
    return object == null;
  }

  private static Task jsonToTask(JsonNode json){
    String id = json.get(Task.KEY_ID).textValue();
    long date = json.get(Task.KEY_DATE).asLong();
    String text = json.get(Task.KEY_TEXT).textValue();
    return new Task(id, date, text);
  }
}
