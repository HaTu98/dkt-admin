package vn.edu.vnu.uet.dktadmin.dto.dao.room;

import vn.edu.vnu.uet.dktadmin.dto.model.Room;

import java.util.List;

public interface RoomDao {
    Room store(Room room);
    Room getByName(String name);
    List<Room> getLikeCode(String code);
    List<Room> getLikeName(String name);
    Room getByRoomCode(String roomCode);
    Room getById(Long id);
    List<Room> getAllRoom();
    List<Room> getRoomInList(List<Long> id);
    void deleteRooms(List<Room> rooms);
    List<Room> getRoomNotInList(List<Long> ids);
}
