package info.penadidik.roomsqlitedbjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import info.penadidik.roomsqlitedbjava.relationroom.RoomRelationActivity;
import info.penadidik.roomsqlitedbjava.room.RoomDbActivity;
import info.penadidik.roomsqlitedbjava.sqlite.SqliteDbActivity;

public class MainActivity extends AppCompatActivity {

    Button btnRoomDb, btnSqliteDb, btnRoomRelation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnRoomDb = findViewById(R.id.btnRoomDb);
        btnSqliteDb = findViewById(R.id.btnSqliteDb);
        btnRoomRelation = findViewById(R.id.btnRoomDbRelation);

        btnRoomDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RoomDbActivity.class);
                startActivity(intent);
            }
        });

        btnSqliteDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SqliteDbActivity.class);
                startActivity(intent);
            }
        });

        btnRoomRelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RoomRelationActivity.class);
                startActivity(intent);
            }
        });

    }
}