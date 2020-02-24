package vn.edu.vnu.uet.dktadmin.dto.dao.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dktadmin.dto.model.Room;
import vn.edu.vnu.uet.dktadmin.dto.repository.RoomRepository;

import java.util.List;

@Service
public class RoomDaoImpl implements RoomDao {
    @Autowired
    private RoomRepository roomRepository;


    @Override
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room getByName(String name) {
        return roomRepository.findByRoomName(name);
    }

    @Override
    public Room getByCode(String code) {
        return roomRepository.findByRoomCode(code);
    }

    @Override
    public List<Room> getAllRoom() {
        List<Room> rooms = roomRepository.findAll();
        if (CollectionUtils.isEmpty(rooms))
            return null;
        return rooms;
    }
}
