package vn.edu.vnu.uet.dktadmin.dto.dao.room;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dktadmin.dto.model.Room;
import vn.edu.vnu.uet.dktadmin.dto.repository.RoomRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomDaoImpl implements RoomDao {
    private final RoomRepository roomRepository;

    public RoomDaoImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


    @Override
    public Room store(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room getByName(String name) {
        return roomRepository.findByRoomName(name);
    }

    @Override
    public List<Room> getLikeCode(String code) {
        List<Room> rooms = roomRepository.findByRoomCodeContains(code);
        if (CollectionUtils.isEmpty(rooms)) {
            return new ArrayList<>();
        }
        return rooms;
    }

    @Override
    public List<Room> getLikeName(String name) {
        List<Room> rooms = roomRepository.findByRoomNameContains(name);
        if (CollectionUtils.isEmpty(rooms)) {
            return new ArrayList<>();
        }
        return rooms;
    }

    @Override
    public Room getByRoomCode(String roomCode) {
        return roomRepository.findByRoomCode(roomCode);
    }

    @Override
    public Room getById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public List<Room> getAllRoom() {
        List<Room> rooms = roomRepository.findAllByOrderByIdAsc();
        if (CollectionUtils.isEmpty(rooms))
            return new ArrayList<>();
        return rooms;
    }

    @Override
    public List<Room> getRoomInList(List<Long> id) {
        List<Room> rooms = roomRepository.findByIdIn(id);
        if (CollectionUtils.isEmpty(rooms)) {
            return new ArrayList<>();
        }
        return rooms;
    }

    @Override
    public void deleteRooms(List<Room> rooms) {
        roomRepository.deleteAll(rooms);
    }

    @Override
    public List<Room> getRoomNotInList(List<Long> ids) {
        List<Room> rooms = roomRepository.findByIdNotIn(ids);
        if (CollectionUtils.isEmpty(rooms)) {
            return new ArrayList<>();
        }
        return rooms;
    }

    @Override
    public List<Room> getAll() {
        List<Room> rooms = roomRepository.findAll();
        if (CollectionUtils.isEmpty(rooms)) {
            return new ArrayList<>();
        }
        return rooms;
    }
}
