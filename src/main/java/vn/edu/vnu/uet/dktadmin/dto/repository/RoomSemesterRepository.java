package vn.edu.vnu.uet.dktadmin.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dktadmin.dto.model.RoomSemester;

import java.util.List;

@Repository
public interface RoomSemesterRepository extends JpaRepository<RoomSemester,Long> {
    RoomSemester findByRoomIdAndSemesterId(Long roomId, Long semesterId);
    List<RoomSemester> findBySemesterId(Long id);
}
