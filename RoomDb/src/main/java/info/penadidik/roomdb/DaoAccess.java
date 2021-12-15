package info.penadidik.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    long insertTodo(Todo todo);

    @Insert
    void insertTodoList(List<Todo> todoList);

    @Query("SELECT * FROM todo")
    List<Todo> fetchAllTodos();

    @Query("SELECT * FROM todo WHERE category = :category")
    List<Todo> fetchTodoListByCategory(String category);

    @Query("SELECT * FROM todo WHERE todo_id = :todoId")
    Todo fetchTodoListById(int todoId);

    @Update
    int updateTodo(Todo todo);

    @Delete
    int deleteTodo(Todo todo);

    @Query("DELETE FROM todo")
    void nukeTable();
}

