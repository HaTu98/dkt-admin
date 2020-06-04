package vn.edu.vnu.uet.dktadmin.dto.service.roomSemester;

import ma.glasnost.orika.MapperFacade;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.utilities.ExcelUtil;
import vn.edu.vnu.uet.dktadmin.dto.dao.location.LocationDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.room.RoomDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.roomSemester.RoomSemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Location;
import vn.edu.vnu.uet.dktadmin.dto.model.Room;
import vn.edu.vnu.uet.dktadmin.dto.model.RoomSemester;
import vn.edu.vnu.uet.dktadmin.dto.service.room.RoomService;
import vn.edu.vnu.uet.dktadmin.dto.service.semester.SemesterService;
import vn.edu.vnu.uet.dktadmin.dto.service.studentSubject.StudentSubjectService;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.ListRoomSemesterResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.RoomSemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.RoomSemesterResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.UpdateRoomRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomSemesterService {
    private static final Logger log = LoggerFactory.getLogger(RoomSemesterService.class);
    private final RoomSemesterDao roomSemesterDao;
    private final SemesterDao semesterDao;
    private final RoomDao roomDao;
    private final RoomService roomService;
    private final SemesterService semesterService;
    private final LocationDao locationDao;
    private final MapperFacade mapperFacade;

    public RoomSemesterService(RoomSemesterDao roomSemesterDao, SemesterDao semesterDao, RoomDao roomDao, RoomService roomService, SemesterService semesterService, LocationDao locationDao, MapperFacade mapperFacade) {
        this.roomSemesterDao = roomSemesterDao;
        this.semesterDao = semesterDao;
        this.roomDao = roomDao;
        this.roomService = roomService;
        this.semesterService = semesterService;
        this.locationDao = locationDao;
        this.mapperFacade = mapperFacade;
    }

    public RoomSemesterResponse createRoomSemester(RoomSemesterRequest request) {
        validateRoomSemester(request);
        RoomSemester roomSemester = mapperFacade.map(request, RoomSemester.class);
        return generateResponse(roomSemesterDao.store(roomSemester));
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
        validateUpdateRoomSemester(request);
        RoomSemester roomSemester = mapperFacade.map(request, RoomSemester.class);
        return generateResponse(roomSemesterDao.store(roomSemester));
    }

    public RoomSemesterResponse getById(Long id) {
        RoomSemester roomSemester = roomSemesterDao.getById(id);
        return mapperFacade.map(roomSemester, RoomSemesterResponse.class);
    }

    public ListRoomSemesterResponse getBySemesterId(Long id, PageBase pageBase) {
        List<RoomSemester> roomSemesters = roomSemesterDao.getBySemesterId(id);
        return getRoomSemesterPaging(roomSemesters, pageBase);
    }

    public void deleteListRoomSemester(List<Long> ids) {
        List<RoomSemester> roomSemesters = roomSemesterDao.getRoomSemesterInList(ids);
        roomSemesterDao.deleteList(roomSemesters);
    }

    public ListRoomSemesterResponse getRoomSemesterPaging(List<RoomSemester> roomSemesters, PageBase pageBase) {
        List<RoomSemesterResponse> responseList = new ArrayList<>();
        Integer page = pageBase.getPage();
        Integer size = pageBase.getSize();
        int total = roomSemesters.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            responseList.add(generateResponse(roomSemesters.get(i)));
        }
        PageResponse pageResponse = new PageResponse(page, size, total);
        return new ListRoomSemesterResponse(responseList, pageResponse);
    }

    public void updateList(List<UpdateRoomRequest> requests) {
        Map<Long, UpdateRoomRequest> mapData = new HashMap<>();
        List<Long> listIds = new ArrayList<>();
        for (UpdateRoomRequest request : requests) {
            mapData.put(request.getId(), request);
            listIds.add(request.getId());
        }
        List<RoomSemester> roomSemesters = roomSemesterDao.getRoomSemesterInList(listIds);
        for (int i = 0; i < roomSemesters.size(); i++) {
            UpdateRoomRequest updateRoom = mapData.get(roomSemesters.get(i).getId());
            roomSemesters.get(i).setNumberOfComputer(updateRoom.getNumberOfComputer());
            roomSemesters.get(i).setPreventiveComputer(updateRoom.getPreventiveComputer());
        }
        roomSemesterDao.storeAll(roomSemesters);
    }

    public Workbook export(Long semesterId) throws IOException {
        List<RoomSemester> roomSemesters = roomSemesterDao.getBySemesterId(semesterId);
        String templatePath = "\\template\\excel\\template_room_semester.xlsx";
        File templateFile = new ClassPathResource(templatePath).getFile();
        FileInputStream templateInputStream = new FileInputStream(templateFile);
        Workbook workbook = new XSSFWorkbook(templateInputStream);

        writeXSSFSheet(workbook, roomSemesters);
        return workbook;
    }

    private void writeXSSFSheet(Workbook workbook, List<RoomSemester> roomSemesters) {
        Sheet sheet = workbook.getSheetAt(0);
        CellStyle cellStyle = ExcelUtil.createDefaultCellStyle(workbook);
        List<Room> rooms = roomDao.getAllRoom();
        Map<Long, Room> roomMap = rooms.stream().collect(Collectors.toMap(Room::getId, x -> x));

        List<Location> locations = locationDao.getAll();
        Map<Long, Location> longLocationMap = locations.stream().collect(Collectors.toMap(Location::getId, x -> x));
        for (int i = 0; i < roomSemesters.size(); i++) {
            try {
                RoomSemester roomSemester = roomSemesters.get(i);
                Row row = sheet.createRow(4 + i);

                Cell cellStt = row.createCell(0);
                cellStt.setCellValue(i + 1);
                cellStt.setCellStyle(cellStyle);

                Room room = roomMap.get(roomSemester.getRoomId());
                Cell roomNameCell = row.createCell(1);
                ExcelUtil.setCellValueAndStyle(roomNameCell, room.getRoomName(), cellStyle);

                Cell roomCodeCell = row.createCell(2);
                ExcelUtil.setCellValueAndStyle(roomCodeCell, room.getRoomCode(), cellStyle);

                Location location = longLocationMap.get(room.getLocationId());
                Cell locationCell = row.createCell(3);
                ExcelUtil.setCellValueAndStyle(locationCell, location.getLocationName(), cellStyle);

                Cell numberComputerCell = row.createCell(4);
                ExcelUtil.setCellValueAndStyle(numberComputerCell, roomSemester.getNumberOfComputer(), cellStyle);

                Cell preventiveComputerCell = row.createCell(5);
                ExcelUtil.setCellValueAndStyle(preventiveComputerCell, roomSemester.getPreventiveComputer(), cellStyle);

                Cell descriptionCell = row.createCell(6);
                ExcelUtil.setCellValueAndStyle(descriptionCell, room.getDescription(), cellStyle);


            } catch (Exception e) {
                log.error("error export :", e);
            }

        }
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
        RoomSemester roomSemester = roomSemesterDao.getByRoomIdAndSemesterId(request.getRoomId(), request.getSemesterId());
        if (roomSemester != null && !request.getId().equals(roomSemester.getId())) {
            throw new BadRequestException(400, "RoomSemester đã tồn tại");
        }
        if (!roomService.isExistRoom(request.getRoomId())) {
            throw new BadRequestException(400, "Room không tồn tại");
        }
        if(!semesterService.isExistSemester(request.getSemesterId())) {
            throw new BadRequestException(400, "Semester không tồn tại");
        }
    }
    private RoomSemesterResponse generateResponse(RoomSemester roomSemester) {
        RoomSemesterResponse response = new RoomSemesterResponse();
        response.setId(roomSemester.getId());
        response.setNumberOfComputer(roomSemester.getNumberOfComputer());
        response.setPreventiveComputer(roomSemester.getPreventiveComputer());

        Room room = roomDao.getById(roomSemester.getRoomId());
        response.setDescription(room.getDescription());
        response.setRoomCode(room.getRoomCode());
        response.setRoomName(room.getRoomName());
        response.setRoomId(room.getId());
        Location location = locationDao.getById(room.getLocationId());
        response.setLocation(location.getLocationName());
        response.setSemesterId(roomSemester.getSemesterId());
        return response;
    }
}
