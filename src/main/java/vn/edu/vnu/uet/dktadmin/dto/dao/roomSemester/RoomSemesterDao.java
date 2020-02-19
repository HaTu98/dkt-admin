package vn.edu.vnu.uet.dktadmin.dto.dao.roomSemester;

import vn.edu.vnu.uet.dktadmin.dto.model.RoomSemester;

import java.util.List;

public interface RoomSemesterDao {
    RoomSemester createRoomSemester(RoomSemester roomSemester);
    List<RoomSemester> getAll();
}
