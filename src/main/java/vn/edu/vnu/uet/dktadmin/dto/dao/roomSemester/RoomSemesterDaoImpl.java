package vn.edu.vnu.uet.dktadmin.dto.dao.roomSemester;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dktadmin.dto.model.RoomSemester;
import vn.edu.vnu.uet.dktadmin.dto.repository.RoomSemesterRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomSemesterDaoImpl implements RoomSemesterDao{

    private final RoomSemesterRepository roomSemesterRepository;

    public RoomSemesterDaoImpl(RoomSemesterRepository roomSemesterRepository) {
        this.roomSemesterRepository = roomSemesterRepository;
    }

    @Override
    public RoomSemester store(RoomSemester roomSemester) {
        return roomSemesterRepository.save(roomSemester);
    }

    @Override
    public List<RoomSemester> getAll() {
        return roomSemesterRepository.findAll();
    }

    @Override
    public RoomSemester getById(Long id) {
        return roomSemesterRepository.findById(id).orElse(null);
    }

    @Override
    public RoomSemester getByRoomIdAndSemesterId(Long roomId, Long semesterId) {
        return  roomSemesterRepository.findByRoomIdAndSemesterId(roomId,semesterId);
    }

    @Override
    public List<RoomSemester> getBySemesterId(Long id) {
        return roomSemesterRepository.findBySemesterId(id);
    }

    @Override
    public List<RoomSemester> getRoomSemesterInList(List<Long> ids) {
        List<RoomSemester> roomSemesters = roomSemesterRepository.findByIdIn(ids);
        if (CollectionUtils.isEmpty(roomSemesters)) {
            return new ArrayList<>();
        }
        return roomSemesters;
    }

    @Override
    public void deleteList(List<RoomSemester> roomSemesters) {
        roomSemesterRepository.deleteAll(roomSemesters);
    }
}
