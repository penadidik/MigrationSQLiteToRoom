package info.penadidik.sqlitedb;

public class TodoSqlite {

    int id;
    String name;
    String description;
    String category;

    // contrustor(empty)
    public TodoSqlite() {
    }

    // constructor
    public TodoSqlite(int id, String name, String description, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTodo_id() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }
}
