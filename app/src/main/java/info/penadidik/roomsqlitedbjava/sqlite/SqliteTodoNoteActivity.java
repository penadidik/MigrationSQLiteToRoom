package info.penadidik.roomsqlitedbjava.sqlite;

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
import java.util.ArrayList;
import java.util.Arrays;

import info.penadidik.roomsqlitedbjava.R;
import info.penadidik.sqlitedb.SqlHandler;
import info.penadidik.sqlitedb.TodoSqlite;

public class SqliteTodoNoteActivity extends AppCompatActivity {

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
    SqlHandler sqlHandler;

    TodoSqlite updateTodo;

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

        sqlHandler = new SqlHandler(getApplicationContext());
        sqlHandler.open();

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
                    TodoSqlite todo = new TodoSqlite();
                    todo.setName(inTitle.getText().toString());
                    todo.setDescription(inDesc.getText().toString());
                    todo.setCategory(spinner.getSelectedItem().toString());

                    insertRow(todo);
                } else {

                    updateTodo.setName(inTitle.getText().toString());
                    updateTodo.setDescription(inDesc.getText().toString());
                    updateTodo.setCategory(spinner.getSelectedItem().toString());

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
        new AsyncTask<Integer, Void, TodoSqlite>() {
            @Override
            protected TodoSqlite doInBackground(Integer... params) {

                return sqlHandler.getContact(params[0]);

            }

            @Override
            protected void onPostExecute(TodoSqlite todo) {
                super.onPostExecute(todo);
                inTitle.setText(todo.getName());
                inDesc.setText(todo.getDescription());
                spinner.setSelection(spinnerList.indexOf(todo.getCategory()));

                updateTodo = todo;
            }
        }.execute(todo_id);

    }

    @SuppressLint("StaticFieldLeak")
    private void insertRow(TodoSqlite todo) {
        new AsyncTask<TodoSqlite, Void, Long>() {
            @Override
            protected Long doInBackground(TodoSqlite... params) {
                return sqlHandler.addRecord(params[0]);
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
    private void deleteRow(TodoSqlite todo) {
        new AsyncTask<TodoSqlite, Void, Integer>() {
            @Override
            protected Integer doInBackground(TodoSqlite... params) {
                return sqlHandler.deleteModel(params[0]);
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
    private void updateRow(TodoSqlite todo) {
        new AsyncTask<TodoSqlite, Void, Integer>() {
            @Override
            protected Integer doInBackground(TodoSqlite... params) {
                return sqlHandler.updateContact(params[0]);
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
