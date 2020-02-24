package vn.edu.vnu.uet.dktadmin.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dktadmin.dto.model.Room;

@Repository
public interface RoomRepository  extends JpaRepository<Room, Long> {
    Room findByRoomName(String name);
    Room findByRoomCode(String code);
}
