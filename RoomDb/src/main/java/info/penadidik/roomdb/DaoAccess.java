package info.penadidik.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import info.penadidik.roomdb.relations.Dog;
import info.penadidik.roomdb.relations.Owner;
import info.penadidik.roomdb.relations.manytomany.DogOwnerCrossRef;
import info.penadidik.roomdb.relations.manytomany.OwnersWithDogs;
import info.penadidik.roomdb.relations.onetomany.OwnerWithDogs;
import info.penadidik.roomdb.relations.onetoone.DogAndOwner;

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

    @Insert
    void insertDogList(List<Dog> dogList);

    @Query("SELECT * FROM dog")
    List<Dog> fetchAllDogs();

    @Insert
    void insertOwnerList(List<Owner> ownerList);

    @Query("SELECT * FROM owner")
    List<Owner> fetchAllOwners();

    @Transaction
    @Query("SELECT * FROM owner")
    List<DogAndOwner> getDogsAndOwners();

    @Query("SELECT * FROM owner")
    @Transaction
    List<OwnerWithDogs> getDogWithOwners();

    @Query("SELECT * FROM owner")
    @Transaction
    List<OwnersWithDogs> getOwnersWithDogs();

    @Query("DELETE FROM dog")
    void nukeDog();

    @Query("DELETE FROM owner")
    void nukeOwner();

    @Query("DELETE FROM ref")
    void nukeRef();

//    @Insert
//    void insertOneToOne(DogAndOwner dogAndOwner);
//
//    @Insert
//    void insertOneToMany(OwnerWithDogs ownerWithDogs);
//
    @Insert
    void insertManyToMany(List<DogOwnerCrossRef> ownersWithDogs);
}

