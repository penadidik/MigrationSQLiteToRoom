package info.penadidik.roomdb.relations;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dog")
public class Dog {

    @PrimaryKey(autoGenerate = true)
    public Long dogId;

    public Long dogOwnerId;

    public String name;

    public int cuteness;

    public int barkVolume;

    public String breed;
}
