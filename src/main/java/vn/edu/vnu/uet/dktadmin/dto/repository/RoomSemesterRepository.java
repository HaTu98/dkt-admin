package vn.edu.vnu.uet.dktadmin.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dktadmin.dto.model.RoomSemester;

@Repository
public interface RoomSemesterRepository extends JpaRepository<RoomSemester,Long> {
    RoomSemester findByRoomIdAndSemesterId(Long roomId, Long semesterId);
}
