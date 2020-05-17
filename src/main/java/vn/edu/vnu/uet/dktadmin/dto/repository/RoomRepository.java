package vn.edu.vnu.uet.dktadmin.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dktadmin.dto.model.Room;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByRoomName(String name);

    Room findByRoomCode(String roomCode);

    List<Room> findByRoomCodeContains(String code);

    List<Room> findByRoomNameContains(String name);

    List<Room> findByIdIn(List<Long> id);

    List<Room> findAllByOrderByIdAsc();
}
