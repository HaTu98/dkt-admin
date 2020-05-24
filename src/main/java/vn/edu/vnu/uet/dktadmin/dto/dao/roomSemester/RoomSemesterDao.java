package vn.edu.vnu.uet.dktadmin.dto.dao.roomSemester;

import vn.edu.vnu.uet.dktadmin.dto.model.RoomSemester;

import java.util.List;

public interface RoomSemesterDao {
    RoomSemester store(RoomSemester roomSemester);
    void storeAll(List<RoomSemester> roomSemesters);
    List<RoomSemester> getAll();
    RoomSemester getById(Long id);
    RoomSemester getByRoomIdAndSemesterId(Long roomId, Long semesterId);
    List<RoomSemester> getBySemesterId(Long id);
    List<RoomSemester> getRoomSemesterInList(List<Long> ids);
    List<RoomSemester> getBySemesterIdIn(List<Long> ids);
    void deleteList(List<RoomSemester> roomSemesters);
    List<RoomSemester> getByRoomIdIn(List<Long> ids);
}
