package vn.edu.vnu.uet.dktadmin.dto.dao.room;

import vn.edu.vnu.uet.dktadmin.dto.model.Room;

import java.util.List;

public interface RoomDao {
    Room createRoom(Room room);
    Room getByName(String name);
    Room getById(Long code);
    List<Room> getAllRoom();
}
