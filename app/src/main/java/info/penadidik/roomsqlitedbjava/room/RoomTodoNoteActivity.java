package info.penadidik.roomsqlitedbjava.room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Arrays;

import info.penadidik.roomdb.RoomHandler;
import info.penadidik.roomdb.Todo;
import info.penadidik.roomsqlitedbjava.R;

public class RoomTodoNoteActivity extends AppCompatActivity {

    Spinner spinner;
    EditText inTitle, inDesc;
    Button btnDone, btnDelete;
    boolean isNewTodo = false;

    private String[] categories = {
            "Android",
            "iOS",
            "Kotlin",
            "Swift"
    };

    public ArrayList<String> spinnerList = new ArrayList<>(Arrays.asList(categories));
    RoomHandler roomHandler;

    Todo updateTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = findViewById(R.id.spinner);
        inTitle = findViewById(R.id.inTitle);
        inDesc = findViewById(R.id.inDescription);
        btnDone = findViewById(R.id.btnDone);
        btnDelete = findViewById(R.id.btnDelete);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        roomHandler = Room.databaseBuilder(getApplicationContext(), RoomHandler.class, "app_room_db").build();

        int todo_id = getIntent().getIntExtra("id", -100);

        if (todo_id == -100)
            isNewTodo = true;

        if (!isNewTodo) {
            fetchTodoById(todo_id);
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewTodo) {
                    Todo todo = new Todo();
                    todo.name = inTitle.getText().toString();
                    todo.description = inDesc.getText().toString();
                    todo.category = spinner.getSelectedItem().toString();

                    insertRow(todo);
                } else {

                    updateTodo.name = inTitle.getText().toString();
                    updateTodo.description = inDesc.getText().toString();
                    updateTodo.category = spinner.getSelectedItem().toString();

                    updateRow(updateTodo);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRow(updateTodo);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchTodoById(final int todo_id) {
        new AsyncTask<Integer, Void, Todo>() {
            @Override
            protected Todo doInBackground(Integer... params) {

                return roomHandler.daoAccess().fetchTodoListById(params[0]);

            }

            @Override
            protected void onPostExecute(Todo todo) {
                super.onPostExecute(todo);
                inTitle.setText(todo.name);
                inDesc.setText(todo.description);
                spinner.setSelection(spinnerList.indexOf(todo.category));

                updateTodo = todo;
            }
        }.execute(todo_id);

    }

    @SuppressLint("StaticFieldLeak")
    private void insertRow(Todo todo) {
        new AsyncTask<Todo, Void, Long>() {
            @Override
            protected Long doInBackground(Todo... params) {
                return roomHandler.daoAccess().insertTodo(params[0]);
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);

                Intent intent = getIntent();
                intent.putExtra("isNew", true).putExtra("id", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(todo);

    }

    @SuppressLint("StaticFieldLeak")
    private void deleteRow(Todo todo) {
        new AsyncTask<Todo, Void, Integer>() {
            @Override
            protected Integer doInBackground(Todo... params) {
                return roomHandler.daoAccess().deleteTodo(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isDeleted", true).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(todo);

    }


    @SuppressLint("StaticFieldLeak")
    private void updateRow(Todo todo) {
        new AsyncTask<Todo, Void, Integer>() {
            @Override
            protected Integer doInBackground(Todo... params) {
                return roomHandler.daoAccess().updateTodo(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isNew", false).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(todo);

    }
}
