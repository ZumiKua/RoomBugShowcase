package in.zumikua.roombugshowcase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM Item WHERE ID = 1")
    Observable<List<Item>> observerItem();

    @Insert
    void insert(Item item);
}
