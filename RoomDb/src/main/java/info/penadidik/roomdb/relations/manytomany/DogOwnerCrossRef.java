package info.penadidik.roomdb.relations.manytomany;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "ref", primaryKeys = {"dogId", "ownerId"})
public class DogOwnerCrossRef {
    @NonNull public Long dogId;
    @NonNull public Long ownerId;
}
