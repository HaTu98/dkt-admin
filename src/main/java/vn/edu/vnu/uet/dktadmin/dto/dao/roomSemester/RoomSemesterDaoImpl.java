package vn.edu.vnu.uet.dktadmin.dto.dao.roomSemester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.RoomSemester;
import vn.edu.vnu.uet.dktadmin.dto.repository.RoomSemesterRepository;

import java.util.List;

@Service
public class RoomSemesterDaoImpl implements RoomSemesterDao{

    @Autowired
    private RoomSemesterRepository roomSemesterRepository;

    @Override
    public RoomSemester createRoomSemester(RoomSemester roomSemester) {
        return roomSemesterRepository.save(roomSemester);
    }

    @Override
    public List<RoomSemester> getAll() {
        return roomSemesterRepository.findAll();
    }
}
