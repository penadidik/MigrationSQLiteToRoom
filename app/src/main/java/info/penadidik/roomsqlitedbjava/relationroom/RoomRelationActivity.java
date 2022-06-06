package info.penadidik.roomsqlitedbjava.relationroom;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import info.penadidik.roomdb.RoomHandler;
import info.penadidik.roomdb.Todo;
import info.penadidik.roomdb.relations.Dog;
import info.penadidik.roomdb.relations.Owner;
import info.penadidik.roomdb.relations.manytomany.DogOwnerCrossRef;
import info.penadidik.roomdb.relations.manytomany.OwnersWithDogs;
import info.penadidik.roomdb.relations.onetomany.OwnerWithDogs;
import info.penadidik.roomdb.relations.onetoone.DogAndOwner;
import info.penadidik.roomsqlitedbjava.R;
import info.penadidik.sqlitedb.SqlHelper;
import info.penadidik.sqlitedb.TodoSqlite;


public class RoomRelationActivity extends AppCompatActivity {

    RoomHandler roomHandler;
    List<Dog> dogList = new ArrayList<>();
    List<Owner> ownerList = new ArrayList<>();
    Button btnShowDbDog, btnShowDbOwner, btnOpenRelation1, btnOpenRelation2, btnOpenRelation3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_relation);

        btnShowDbDog = findViewById(R.id.btnDog);
        btnShowDbOwner = findViewById(R.id.btnOwner);
        btnOpenRelation1 = findViewById(R.id.btnRelation1);
        btnOpenRelation2 = findViewById(R.id.btnRelation2);
        btnOpenRelation3 = findViewById(R.id.btnRelation3);

        roomHandler = Room.databaseBuilder(getApplicationContext(), RoomHandler.class, RoomHandler.DATABASE_NAME).fallbackToDestructiveMigration().build();

        checkDb();

        btnShowDbDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnShowDbOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }

    void checkDb() {

        new AsyncTask<String, Void, List<Dog>>() {
            @Override
            protected List<Dog> doInBackground(String... params) {
                return roomHandler.daoAccess().fetchAllDogs();
            }

            @Override
            protected void onPostExecute(List<Dog> doList) {
                Log.d("dog list check: ", new Gson().toJson(doList));
                createDummyDog();
                createDummyOwner();
            }
        }.execute();
    }

    void createDummyDog() {
        dogList.clear();

        Dog dog1 = new Dog();
        dog1.dogOwnerId = 1L;
        dog1.barkVolume = 1;
        dog1.breed = "a";
        dog1.name = "aa";
        dog1.cuteness = 1;
        dogList.add(dog1);

        Dog dog2 = new Dog();
        dog2.dogOwnerId = 2L;
        dog2.barkVolume = 2;
        dog2.breed = "b";
        dog2.name = "bebe";
        dog2.cuteness = 2;
        dogList.add(dog2);

        Dog dog3 = new Dog();
        dog3.dogOwnerId = 3L;
        dog3.barkVolume = 3;
        dog3.breed = "c";
        dog3.name = "cece";
        dog3.cuteness = 3;
        dogList.add(dog3);

        Dog dog4 = new Dog();
        dog4.dogOwnerId = 3L;
        dog4.barkVolume = 4;
        dog4.breed = "d";
        dog4.name = "dede";
        dog4.cuteness = 4;
        dogList.add(dog4);

        Dog dog5 = new Dog();
        dog5.dogOwnerId = 3L;
        dog5.barkVolume = 5;
        dog5.breed = "e";
        dog5.name = "ere";
        dog5.cuteness = 5;
        dogList.add(dog5);

        Dog dog6 = new Dog();
        dog6.dogOwnerId = 4L;
        dog6.barkVolume = 6;
        dog6.breed = "f";
        dog6.name = "fafa";
        dog6.cuteness = 6;
        dogList.add(dog6);

        insertDogList(dogList);

    }

    void createDummyOwner() {
        ownerList.clear();

        Owner owner1 = new Owner();
        owner1.name = "si A";
        ownerList.add(owner1);

        Owner owner2 = new Owner();
        owner2.name = "si B";
        ownerList.add(owner2);

        Owner owner3 = new Owner();
        owner3.name = "si C";
        ownerList.add(owner3);

        Owner owner4 = new Owner();
        owner4.name = "si D";
        ownerList.add(owner4);

        Owner owner5 = new Owner();
        owner5.name = "si E";
        ownerList.add(owner5);

        insertOwnerList(ownerList);

    }

    private void insertDogList(List<Dog> dogList) {
        new AsyncTask<List<Dog>, Void, Void>() {
            @Override
            protected Void doInBackground(List<Dog>... params) {
                Log.d("insert dog list: ", new Gson().toJson(params[0]));
                roomHandler.daoAccess().nukeDog();
                roomHandler.daoAccess().insertDogList(params[0]);
                return null;

            }

            @Override
            protected void onPostExecute(Void voids) {
                super.onPostExecute(voids);
            }
        }.execute(dogList);
    }

    private void insertOwnerList(List<Owner> ownerList) {
        new AsyncTask<List<Owner>, Void, Void>() {
            @Override
            protected Void doInBackground(List<Owner>... params) {
                Log.d("insert owner list: ", new Gson().toJson(params[0]));
                roomHandler.daoAccess().nukeOwner();
                roomHandler.daoAccess().insertOwnerList(params[0]);
                return null;

            }

            @Override
            protected void onPostExecute(Void voids) {
                super.onPostExecute(voids);
                createDummyRelationManyToMany();
            }
        }.execute(ownerList);
    }

    private void showOneToOne() {
        new AsyncTask<String, Void, List<DogAndOwner>>() {
            @Override
            protected List<DogAndOwner> doInBackground(String... params) {
                return roomHandler.daoAccess().getDogsAndOwners();
            }

            @Override
            protected void onPostExecute(List<DogAndOwner> list) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("relation one to one: ", new Gson().toJson(list));
                    }
                }, 5000);
            }
        }.execute();
    }

    private void showOneToMany() {
        new AsyncTask<String, Void, List<OwnerWithDogs>>() {
            @Override
            protected List<OwnerWithDogs> doInBackground(String... params) {
                return roomHandler.daoAccess().getDogWithOwners();
            }

            @Override
            protected void onPostExecute(List<OwnerWithDogs> list) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("relation one to many: ", new Gson().toJson(list));
                    }
                }, 5000);
            }
        }.execute();
    }

    private void showManyToMany() {
        new AsyncTask<String, Void, List<OwnersWithDogs>>() {
            @Override
            protected List<OwnersWithDogs> doInBackground(String... params) {
                return roomHandler.daoAccess().getOwnersWithDogs();
            }

            @Override
            protected void onPostExecute(List<OwnersWithDogs> list) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("relation many to many: ", new Gson().toJson(list));
                    }
                }, 5000);
            }
        }.execute();
    }

//    void createDummyRelationOneToOne() {
//        DogAndOwner dogAndOwner = new DogAndOwner();
//        dogAndOwner.owner = ownerList.get(0);
//        dogAndOwner.dog = dogList;
//
//        roomHandler.daoAccess().insertOneToOne(dogAndOwner);
//    }
//
//    void createDummyRelationOneToMany() {
//        OwnerWithDogs ownerWithDogs = new OwnerWithDogs();
//        ownerWithDogs.dogs = dogList;
//        ownerWithDogs.owner = ownerList.get(2);
//
//        roomHandler.daoAccess().insertOneToMany(ownerWithDogs);
//    }
//
    void createDummyRelationManyToMany() {
        List<DogOwnerCrossRef> ownersWithDogsList = new ArrayList<>();
        DogOwnerCrossRef item1 = new DogOwnerCrossRef();
        item1.dogId = 1L;
        item1.ownerId = 2L;
        ownersWithDogsList.add(item1);

        DogOwnerCrossRef item2 = new DogOwnerCrossRef();
        item2.dogId = 2L;
        item2.ownerId = 1L;
        ownersWithDogsList.add(item2);

        DogOwnerCrossRef item3 = new DogOwnerCrossRef();
        item3.dogId = 2L;
        item3.ownerId = 2L;
        ownersWithDogsList.add(item3);

        insertRefList(ownersWithDogsList);
    }

    private void insertRefList(List<DogOwnerCrossRef> ownerList) {
        new AsyncTask<List<DogOwnerCrossRef>, Void, Void>() {
            @Override
            protected Void doInBackground(List<DogOwnerCrossRef>... params) {
                roomHandler.daoAccess().nukeRef();
                roomHandler.daoAccess().insertManyToMany(params[0]);
                return null;

            }

            @Override
            protected void onPostExecute(Void voids) {
                super.onPostExecute(voids);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("relation ref: ", new Gson().toJson(ownerList));
                        showOneToOne();
                        showOneToMany();
                        showManyToMany();
                    }
                }, 5000);
            }
        }.execute(ownerList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        roomHandler.close();
    }
}
