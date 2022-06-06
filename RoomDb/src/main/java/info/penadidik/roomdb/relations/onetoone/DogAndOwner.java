package info.penadidik.roomdb.relations.onetoone;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import info.penadidik.roomdb.relations.Dog;
import info.penadidik.roomdb.relations.Owner;

public class DogAndOwner {
    @Embedded public Owner owner;
    @Relation(
            parentColumn = "ownerId",
            entityColumn = "dogOwnerId"
    )
    public Dog dog;
}
