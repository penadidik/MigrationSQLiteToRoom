package info.penadidik.roomsqlitedbjava.sqlite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.penadidik.roomdb.RoomHandler;
import info.penadidik.roomdb.RoomHandler_Impl;
import info.penadidik.roomdb.Todo;
import info.penadidik.roomsqlitedbjava.R;
import info.penadidik.sqlitedb.SqlHandler;
import info.penadidik.sqlitedb.SqlHelper;
import info.penadidik.sqlitedb.TodoSqlite;

public class SqliteDbActivity extends AppCompatActivity implements SqliteRecyclerViewAdapter.ClickListener, AdapterView.OnItemSelectedListener {

    SqlHandler sqlHandler;
    RecyclerView recyclerView;
    RoomHandler roomHandler;
    Spinner spinner;
    Button button;
    SqliteRecyclerViewAdapter sqliteRecyclerViewAdapter;
    FloatingActionButton floatingActionButton;
    private String[] categories = {
            "All",
            "Android",
            "iOS",
            "Kotlin",
            "Swift"
    };

    ArrayList<TodoSqlite> todoArrayList = new ArrayList<>();
    ArrayList<String> spinnerList = new ArrayList<>(Arrays.asList(categories));

    public static final int NEW_TODO_REQUEST_CODE = 200;
    public static final int UPDATE_TODO_REQUEST_CODE = 300;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        sqlHandler = new SqlHandler(getApplicationContext());
        sqlHandler.open();
        initViews();

        checkIfAppLaunchedFirstTime();
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SqliteDbActivity.this, SqliteTodoNoteActivity.class), NEW_TODO_REQUEST_CODE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomHandler = Room.databaseBuilder(getApplicationContext(), RoomHandler.class, SqlHelper.DB_NAME).fallbackToDestructiveMigration().build();
                Log.d("Migration", "starting...");
                ArrayList<Todo> todos = new ArrayList<>();
                for(TodoSqlite item: sqlHandler.getAllRecord()){
                    Todo todo = new Todo();
                    todo.name = item.getName();
                    todo.description = item.getDescription();
                    todo.category = item.getCategory();
                    todos.add(todo);
                }

                doMigration(todos);
            }
        });

    }

    private void initViews() {
        floatingActionButton = findViewById(R.id.fab);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        recyclerView = findViewById(R.id.recyclerView);
        button = findViewById(R.id.btnMigrate);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sqliteRecyclerViewAdapter = new SqliteRecyclerViewAdapter(this);
        recyclerView.setAdapter(sqliteRecyclerViewAdapter);
    }

    @Override
    public void launchIntent(int id) {
        startActivityForResult(new Intent(SqliteDbActivity.this, SqliteTodoNoteActivity.class).putExtra("id", id), UPDATE_TODO_REQUEST_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (position == 0) {
            loadAllTodos();
        } else {
            String string = parent.getItemAtPosition(position).toString();
            loadFilteredTodos(string);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("StaticFieldLeak")
    private void doMigration(List<Todo> todoList) {
        new AsyncTask<List<Todo>, Void, Void>() {
            @Override
            protected Void doInBackground(List<Todo>... params) {
                Log.d("Migration", "It will be deleted all...");
                roomHandler.daoAccess().nukeTable();
                Log.d("Migration", "It will be finish...");
                roomHandler.daoAccess().insertTodoList(params[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void voids) {
                super.onPostExecute(voids);
                Log.d("Migration", "finish...");
            }
        }.execute(todoList);

    }


    @SuppressLint("StaticFieldLeak")
    private void loadFilteredTodos(String category) {
        new AsyncTask<String, Void, List<TodoSqlite>>() {
            @Override
            protected List<TodoSqlite> doInBackground(String... params) {
                return sqlHandler.getAllByCategory(params[0]);

            }

            @Override
            protected void onPostExecute(List<TodoSqlite> todoList) {
                sqliteRecyclerViewAdapter.updateTodoList(todoList);
            }
        }.execute(category);

    }


    @SuppressLint("StaticFieldLeak")
    private void fetchTodoByIdAndInsert(int id) {
        new AsyncTask<Integer, Void, TodoSqlite>() {
            @Override
            protected TodoSqlite doInBackground(Integer... params) {
                return sqlHandler.getContact(params[0]);

            }

            @Override
            protected void onPostExecute(TodoSqlite todoList) {
                sqliteRecyclerViewAdapter.addRow(todoList);
            }
        }.execute(id);

    }

    @SuppressLint("StaticFieldLeak")
    private void loadAllTodos() {
        new AsyncTask<String, Void, List<TodoSqlite>>() {
            @Override
            protected List<TodoSqlite> doInBackground(String... params) {
                return sqlHandler.getAllRecord();
            }

            @Override
            protected void onPostExecute(List<TodoSqlite> todoList) {
                sqliteRecyclerViewAdapter.updateTodoList(todoList);
            }
        }.execute();
    }

    private void buildDummyTodos() {
        TodoSqlite todo = new TodoSqlite();
        todo.setId(1);
        todo.setName("Android Retrofit Tutorial");
        todo.setDescription("Cover a tutorial on the Retrofit networking library using a RecyclerView to show the data.");
        todo.setCategory("Android");
        todoArrayList.add(todo);

        todo = new TodoSqlite();
        todo.setId(2);
        todo.setName("iOS TableView Tutorial");
        todo.setDescription("Covers the basics of TableViews in iOS using delegates.");
        todo.setCategory("iOS");
        todoArrayList.add(todo);

        todo = new TodoSqlite();
        todo.setId(3);
        todo.setName("Kotlin Arrays");
        todo.setDescription("Cover the concepts of Arrays in Kotlin and how they differ from the Java ones.");
        todo.setCategory("Kotlin");
        todoArrayList.add(todo);

        todo = new TodoSqlite();
        todo.setId(4);
        todo.setName("Swift Arrays");
        todo.setDescription("Cover the concepts of Arrays in Swift and how they differ from the Java and Kotlin ones.");
        todo.setCategory("Swift");
        todoArrayList.add(todo);

        insertList(todoArrayList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            //reset spinners
            spinner.setSelection(0);

            if (requestCode == NEW_TODO_REQUEST_CODE) {
                long id = data.getLongExtra("id", -1);
                Toast.makeText(getApplicationContext(), "Row inserted", Toast.LENGTH_SHORT).show();
                fetchTodoByIdAndInsert((int) id);

            } else if (requestCode == UPDATE_TODO_REQUEST_CODE) {

                boolean isDeleted = data.getBooleanExtra("isDeleted", false);
                int number = data.getIntExtra("number", -1);
                if (isDeleted) {
                    Toast.makeText(getApplicationContext(), number + " rows deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), number + " rows updated", Toast.LENGTH_SHORT).show();
                }

                loadAllTodos();

            }


        } else {
            Toast.makeText(getApplicationContext(), "No action done by user", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void insertList(List<TodoSqlite> todoList) {
        new AsyncTask<List<TodoSqlite>, Void, Void>() {
            @Override
            protected Void doInBackground(List<TodoSqlite>... params) {
                sqlHandler.addRecordList(params[0]);
                return null;

            }

            @Override
            protected void onPostExecute(Void voids) {
                super.onPostExecute(voids);
            }
        }.execute(todoList);

    }

    private void checkIfAppLaunchedFirstTime() {
        final String PREFS_NAME = "SharedPrefs";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("firstTime_SqliteDb", true)) {
            settings.edit().putBoolean("firstTime_SqliteDb", false).apply();
            buildDummyTodos();
        }
    }
}