package vn.edu.vnu.uet.dktadmin.dto.service.roomSemester;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.dao.room.RoomDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.roomSemester.RoomSemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Room;
import vn.edu.vnu.uet.dktadmin.dto.model.RoomSemester;
import vn.edu.vnu.uet.dktadmin.dto.model.Semester;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.RoomSemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.RoomSemesterResponse;

@Service
public class RoomSemesterService {
    @Autowired
    private RoomSemesterDao roomSemesterDao;

    @Autowired
    private SemesterDao semesterDao;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private MapperFacade mapperFacade;

    public RoomSemesterResponse createRoomSemester(RoomSemesterRequest request) {
        Semester semester = semesterDao.getById(request.getSemesterId());
        Room room = roomDao.getById(request.getRoomId());
        RoomSemester roomSemester = mapperFacade.map(request, RoomSemester.class);
        return mapperFacade.map(roomSemesterDao.createRoomSemester(roomSemester), RoomSemesterResponse.class);
    }
}
