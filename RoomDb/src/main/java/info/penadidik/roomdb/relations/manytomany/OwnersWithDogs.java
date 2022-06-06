package info.penadidik.roomdb.relations.manytomany;


import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import info.penadidik.roomdb.relations.Dog;
import info.penadidik.roomdb.relations.Owner;

public class OwnersWithDogs {
    @Embedded public Owner owner;
    @Relation(
            parentColumn = "ownerId",
            entityColumn = "dogId",
            associateBy = @Junction(DogOwnerCrossRef.class)
    )
    public List<Dog> dogs;
}
