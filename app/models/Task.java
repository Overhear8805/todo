package models;

import org.bson.Document;

public class Task {
  public static final String KEY_ID = "_id";
  public static final String KEY_DATE = "date";
  public static final String KEY_TEXT = "text";

  private String id;
  private long date;
  private String text;

  public Task(String id, long date, String text) {
    this.id = id;
    this.date = date;
    this.text = text;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public long getDate() {
    return date;
  }

  public void setDate(long date) {
    this.date = date;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public static Task documentToTask(Document document) {
    System.out.println("document: "+document);
    String id = document.getString(Task.KEY_ID);
    long date = document.getLong(Task.KEY_DATE);
    String text = document.getString(Task.KEY_TEXT);
    return new Task(id, date, text);
  }

  public Document toDocument() {
    return new Document(KEY_ID, id)
        .append(KEY_DATE, date)
        .append(KEY_TEXT, text);
  }

  public class TaskBuilder {
    private String id;
    private long date;
    private String text;

    public TaskBuilder() {
    }

    public TaskBuilder setId(String id) {
      this.id = id;
      return this;
    }

    public TaskBuilder setDate(long date) {
      this.date = date;
      return this;
    }

    public TaskBuilder setText(String text) {
      this.text = text;
      return this;
    }

    public Task createTask() {
      return new Task(id, date, text);
    }
  }
}