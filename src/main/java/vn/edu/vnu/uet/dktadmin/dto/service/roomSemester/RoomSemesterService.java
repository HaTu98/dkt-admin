package vn.edu.vnu.uet.dktadmin.dto.service.roomSemester;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.dto.dao.room.RoomDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.roomSemester.RoomSemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.RoomSemester;
import vn.edu.vnu.uet.dktadmin.dto.service.room.RoomService;
import vn.edu.vnu.uet.dktadmin.dto.service.semester.SemesterService;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.RoomSemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.RoomSemesterResponse;

@Service
public class RoomSemesterService {
    private final RoomSemesterDao roomSemesterDao;
    private final SemesterDao semesterDao;
    private final RoomDao roomDao;
    private final RoomService roomService;
    private final SemesterService semesterService;
    private final MapperFacade mapperFacade;

    public RoomSemesterService(RoomSemesterDao roomSemesterDao, SemesterDao semesterDao, RoomDao roomDao, RoomService roomService, SemesterService semesterService, MapperFacade mapperFacade) {
        this.roomSemesterDao = roomSemesterDao;
        this.semesterDao = semesterDao;
        this.roomDao = roomDao;
        this.roomService = roomService;
        this.semesterService = semesterService;
        this.mapperFacade = mapperFacade;
    }

    public RoomSemesterResponse createRoomSemester(RoomSemesterRequest request) {
        validateRoomSemester(request);
        RoomSemester roomSemester = mapperFacade.map(request, RoomSemester.class);
        return mapperFacade.map(roomSemesterDao.createRoomSemester(roomSemester), RoomSemesterResponse.class);
    }

    public boolean isExistRoomSemester(Long id) {
        RoomSemester roomSemester = roomSemesterDao.getById(id);
        return  roomSemester != null;
    }
    public boolean isExistRoomSemester(Long roomId, Long semesterId) {
        RoomSemester roomSemester = roomSemesterDao.getByRoomIdAndSemesterId(roomId, semesterId);
        return roomSemester != null;
    }

    public void validateRoomSemester(RoomSemesterRequest request) {
        if(request.getRoomId() == null) {
            throw new BadRequestException(400,"Room không thể null");
        }
        if(request.getSemesterId() == null) {
            throw new BadRequestException(400,"Semester không thể null");
        }
        if (isExistRoomSemester(request.getRoomId(), request.getSemesterId())){
            throw new BadRequestException(400, "RoomSemester đã tồn tại");
        }
        if (!roomService.isExistRoom(request.getRoomId())) {
            throw new BadRequestException(400, "Room không tồn tại");
        }
        if(!semesterService.isExistSemester(request.getSemesterId())) {
            throw new BadRequestException(400, "Semester không tồn tại");
        }
    }
}
