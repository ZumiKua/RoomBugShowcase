package in.zumikua.roombugshowcase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Item {
    @PrimaryKey
    int id;

    int value;

    Item(int id, int value) {
        this.id = id;
        this.value = value;
    }
}
