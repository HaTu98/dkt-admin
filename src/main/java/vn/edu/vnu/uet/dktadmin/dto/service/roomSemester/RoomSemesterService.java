package vn.edu.vnu.uet.dktadmin.dto.service.roomSemester;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.dto.dao.room.RoomDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.roomSemester.RoomSemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.RoomSemester;
import vn.edu.vnu.uet.dktadmin.dto.model.Student;
import vn.edu.vnu.uet.dktadmin.dto.service.room.RoomService;
import vn.edu.vnu.uet.dktadmin.dto.service.semester.SemesterService;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.ListRoomSemesterResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.RoomSemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.RoomSemesterResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester.ListSubjectSemesterResponse;

import java.util.ArrayList;
import java.util.List;

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
        return mapperFacade.map(roomSemesterDao.store(roomSemester), RoomSemesterResponse.class);
    }

    public boolean isExistRoomSemester(Long id) {
        RoomSemester roomSemester = roomSemesterDao.getById(id);
        return  roomSemester != null;
    }
    public boolean isExistRoomSemester(Long roomId, Long semesterId) {
        RoomSemester roomSemester = roomSemesterDao.getByRoomIdAndSemesterId(roomId, semesterId);
        return roomSemester != null;
    }

    public RoomSemesterResponse update(RoomSemesterRequest request) {
        validateRoomSemester(request);
        RoomSemester roomSemester = mapperFacade.map(request, RoomSemester.class);
        return mapperFacade.map(roomSemesterDao.store(roomSemester), RoomSemesterResponse.class);
    }

    public RoomSemesterResponse getById(Long id) {
        RoomSemester roomSemester = roomSemesterDao.getById(id);
        return mapperFacade.map(roomSemester, RoomSemesterResponse.class);
    }

    public ListRoomSemesterResponse getBySemesterId(Long id, PageBase pageBase) {
        List<RoomSemester> roomSemesters = roomSemesterDao.getBySemesterId(id);
        return getRoomSemesterPaging(roomSemesters, pageBase);
    }

    public ListRoomSemesterResponse getRoomSemesterPaging(List<RoomSemester> roomSemesters, PageBase pageBase) {
        List<RoomSemester> roomSemesterList = new ArrayList<>();
        Integer page = pageBase.getPage();
        Integer size = pageBase.getSize();
        int total = roomSemesters.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            roomSemesterList.add(roomSemesters.get(i));
        }
        PageResponse pageResponse = new PageResponse(page, size, total);
        return new ListRoomSemesterResponse(mapperFacade.mapAsList(roomSemesterList, RoomSemesterResponse.class), pageResponse);
    }

    private void validateRoomSemester(RoomSemesterRequest request) {
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

    private void validateUpdateRoomSemester(RoomSemesterRequest request) {
        if (isExistRoomSemester(request.getId())) {
            throw new BadRequestException(400, "RoomSemester không tồn tại");
        }

        if(request.getRoomId() == null) {
            throw new BadRequestException(400,"Room không thể null");
        }
        if(request.getSemesterId() == null) {
            throw new BadRequestException(400,"Semester không thể null");
        }
        if (!isExistRoomSemester(request.getRoomId(), request.getSemesterId())){
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
