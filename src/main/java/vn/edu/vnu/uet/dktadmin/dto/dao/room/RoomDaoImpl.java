package vn.edu.vnu.uet.dktadmin.dto.dao.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dktadmin.dto.model.Room;
import vn.edu.vnu.uet.dktadmin.dto.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoomDaoImpl implements RoomDao {
    private final RoomRepository roomRepository;

    public RoomDaoImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


    @Override
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room getByName(String name) {
        return roomRepository.findByRoomName(name);
    }

    @Override
    public Room getByRoomCode(String roomCode) {
        return roomRepository.findByRoomCode(roomCode);
    }

    @Override
    public Room getById(Long id) {
        Optional<Room> room = roomRepository.findById(id);
        return room.orElseGet(room::get);
    }

    @Override
    public List<Room> getAllRoom() {
        List<Room> rooms = roomRepository.findAll();
        if (CollectionUtils.isEmpty(rooms))
            return null;
        return rooms;
    }
}
