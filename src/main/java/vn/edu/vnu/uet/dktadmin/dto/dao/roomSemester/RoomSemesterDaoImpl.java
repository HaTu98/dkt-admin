package vn.edu.vnu.uet.dktadmin.dto.dao.roomSemester;

import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.RoomSemester;
import vn.edu.vnu.uet.dktadmin.dto.repository.RoomSemesterRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoomSemesterDaoImpl implements RoomSemesterDao{

    private final RoomSemesterRepository roomSemesterRepository;

    public RoomSemesterDaoImpl(RoomSemesterRepository roomSemesterRepository) {
        this.roomSemesterRepository = roomSemesterRepository;
    }

    @Override
    public RoomSemester createRoomSemester(RoomSemester roomSemester) {
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
}
