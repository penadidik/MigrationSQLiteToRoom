package info.penadidik.roomdb.relations;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "owner")
public class Owner {
    @PrimaryKey(autoGenerate = true)
    public Long ownerId;

    public String name;
}
