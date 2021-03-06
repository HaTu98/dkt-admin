package vn.edu.vnu.uet.dktadmin.dto.service.room;

import ma.glasnost.orika.MapperFacade;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnu.uet.dktadmin.common.Constant;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.utilities.ExcelUtil;
import vn.edu.vnu.uet.dktadmin.common.utilities.Util;
import vn.edu.vnu.uet.dktadmin.dto.dao.location.LocationDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.room.RoomDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.roomSemester.RoomSemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.*;
import vn.edu.vnu.uet.dktadmin.dto.service.location.LocationService;
import vn.edu.vnu.uet.dktadmin.rest.model.CheckExistRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final RoomDao roomDao;
    private final LocationDao locationDao;
    private final LocationService locationService;
    private final MapperFacade mapperFacade;
    private final RoomSemesterDao roomSemesterDao;

    public RoomService(RoomDao roomDao, LocationDao locationDao, LocationService locationService, MapperFacade mapperFacade, RoomSemesterDao roomSemesterDao) {
        this.roomDao = roomDao;
        this.locationDao = locationDao;
        this.locationService = locationService;
        this.mapperFacade = mapperFacade;
        this.roomSemesterDao = roomSemesterDao;
    }

    public RoomResponse createRoom(RoomRequest request) {
        validateRoom(request);
        Location location = locationDao.getByLocationCode(Util.camelToSnake(request.getLocation()));
        if (location == null){
            location = locationService.createLocation(request.getLocation());
        }
        Room room = mapperFacade.map(request, Room.class);
        room.setLocationId(location.getId());
        RoomResponse response = mapperFacade.map(roomDao.store(room), RoomResponse.class);
        response.setLocation(location.getLocationName());
        return response;
    }

    public RoomListResponse getAllRoom(PageBase page) {
        List<Room> rooms = roomDao.getAllRoom();
        return getListRoomPaging(rooms, page);
    }

    public RoomResponse getRoom(Long id) {
        Room room = roomDao.getById(id);
        return mapperFacade.map(room, RoomResponse.class);
    }

    public RoomResponse updateRoom(RoomRequest request) {
        validateUpdateRoom(request);
        Location location = locationDao.getByLocationCode(Util.camelToSnake(request.getLocation()));
        if (location == null){
            location = locationService.createLocation(request.getLocation());
        }
        Room room = mapperFacade.map(request, Room.class);
        room.setLocationId(location.getId());
        room.setId(request.getId());
        return mapperFacade.map(roomDao.store(room), RoomResponse.class);
    }

    public RoomListResponse searchRoom(String query, PageBase pageBase) {
        List<Room> roomWithName = roomDao.getLikeName(query);
        List<Room> roomWithCode = roomDao.getLikeCode(query);
        Map<Long, Room> roomMap = new HashMap<>();
        for (Room room : roomWithName) {
            roomMap.put(room.getId(), room);
        }
        for (Room room : roomWithCode) {
            roomMap.put(room.getId(), room);
        }
        roomMap = roomMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return getListRoomPaging(
                new ArrayList<>(roomMap.values()), pageBase
        );
    }

    public Boolean checkExistRoom(CheckExistRequest checkExistRequest) {
        if (Constant.ADD.equalsIgnoreCase(checkExistRequest.getMode())) {
            Room room = roomDao.getByRoomCode(checkExistRequest.getCode());
            return room != null;
        } else if (Constant.EDIT.equalsIgnoreCase(checkExistRequest.getMode())){
            Room room = roomDao.getByRoomCode(checkExistRequest.getCode());
            Room roomById = roomDao.getById(checkExistRequest.getId());
            if (roomById == null) return true;
            if (room == null) return false;
            if (room.getRoomCode().equals(roomById.getRoomCode())) {
                return false;
            }
            return true;
        }
        throw new BadRequestException(400, "Mode không tồn tại");
    }

    public void deleteListRoom(List<Long> ids) {
        List<Room> rooms = roomDao.getRoomInList(ids);
        List<RoomSemester> roomSemesters = roomSemesterDao.getByRoomIdIn(ids);
        if (!CollectionUtils.isEmpty(roomSemesters)) {
            throw new BaseException(400, "Không thể xóa room");
        }
        roomDao.deleteRooms(rooms);
    }

    private RoomListResponse getListRoomPaging(List<Room> rooms, PageBase pageBase) {
        List<Location> locations = locationDao.getAll();
        Map<Long, String> locationMap = locations.stream()
                .collect(Collectors.toMap(Location::getId, Location::getLocationName));
        List<RoomResponse> roomResponses = new ArrayList<>();
        Integer size = pageBase.getSize();
        Integer page = pageBase.getPage();
        int total = rooms.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            RoomResponse response = mapperFacade.map(rooms.get(i), RoomResponse.class);
            response.setLocation(locationMap.get(rooms.get(i).getLocationId()));
            roomResponses.add(response);
        }
        PageResponse pageResponse = new PageResponse(page, size, total);
        return new RoomListResponse(roomResponses, pageResponse);
    }

    public boolean isExistRoom(Long roomId){
        Room room = roomDao.getById(roomId);
        return room != null;
    }

    public boolean isExistRoom(String roomCode, String locationName){
        Location location = locationDao.getByLocationName(locationName);
        if (location == null) return false;
        Room room = roomDao.getByRoomCodeAndLocationId(roomCode, location.getId());
        return room != null;
    }

    public XSSFWorkbook template() throws IOException {
        String templatePath = "\\template\\excel\\import_room.xlsx";
        File templateFile = new ClassPathResource(templatePath).getFile();
        FileInputStream templateInputStream = new FileInputStream(templateFile);
        return new XSSFWorkbook(templateInputStream);
    }

    public List<XSSFRow> importRoom(MultipartFile file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<XSSFRow> errors = new ArrayList<>();
        storeImportRoom(sheet, errors);
        return errors;
    }

    public Workbook export() throws IOException {
        List<Room> rooms = roomDao.getAll();

        Workbook workbook = template();

        writeXSSFSheet(workbook, rooms);
        return workbook;
    }

    private void writeXSSFSheet(Workbook workbook, List<Room> rooms) {
        Sheet sheet = workbook.getSheetAt(0);
        CellStyle cellStyleCenter = ExcelUtil.createCenterCellStyle(workbook);
        CellStyle cellStyleLeft = ExcelUtil.createLeftCellStyle(workbook);
        CellStyle cellStyleRight = ExcelUtil.createRightCellStyle(workbook);
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            Row row = sheet.createRow(i + 4);

            Cell cellStt = row.createCell(0);
            cellStt.setCellValue(i+1);
            cellStt.setCellStyle(cellStyleRight);

            Cell cellRoomName = row.createCell(1);
            ExcelUtil.setCellValueAndStyle(cellRoomName, room.getRoomName(), cellStyleLeft);

            Cell cellRoomCode = row.createCell(2);
            ExcelUtil.setCellValueAndStyle(cellRoomCode, room.getRoomCode(), cellStyleLeft);

            String location = locationDao.getById(room.getLocationId()).getLocationName();
            Cell cellLocation = row.createCell(3);
            ExcelUtil.setCellValueAndStyle(cellLocation, location, cellStyleLeft);

            Cell cellDescription = row.createCell(4);
            ExcelUtil.setCellValueAndStyle(cellDescription, room.getDescription(), cellStyleLeft);
        }
    }

    public RoomListResponse getRoomNotInSemester(Long semesterId, PageBase  pageBase) {
        List<RoomSemester> roomSemesters = roomSemesterDao.getBySemesterId(semesterId);
        List<Long> listRoomIds = roomSemesters.stream().map(RoomSemester::getRoomId).collect(Collectors.toList());
        List<Room> rooms;
        if (CollectionUtils.isEmpty(listRoomIds)) {
            rooms = roomDao.getAllRoom();
        } else {
            rooms = roomDao.getRoomNotInList(listRoomIds);
        }
        return getListRoomPaging(rooms, pageBase);
    }

    private void storeImportRoom(XSSFSheet sheet, List<XSSFRow> errors) {
        int rowNumber = sheet.getPhysicalNumberOfRows();
        for (int i = 4; i < rowNumber; i++) {
            XSSFRow row = sheet.getRow(i);
            try {
                String stt = ExcelUtil.getValueInCell(row.getCell(0));
                if (stt == null) continue;
                RoomRequest roomRequest = new RoomRequest();
                roomRequest.setRoomName(ExcelUtil.getValueInCell(row.getCell(1)).trim());
                roomRequest.setRoomCode(ExcelUtil.getValueInCell(row.getCell(2)));
                roomRequest.setLocation(ExcelUtil.getValueInCell(row.getCell(3)));
                roomRequest.setDescription(ExcelUtil.getValueInCell(row.getCell(4)));

                this.createRoom(roomRequest);
            } catch (Exception e) {
                errors.add(row);
            }
        }
    }

    private void validateRoom(RoomRequest request) {
        String roomCode = request.getRoomCode();
        if(roomCode == null) {
            throw new BadRequestException(400,"RoomCode không thể null");
        }
        if(request.getRoomName() == null) {
            throw new BadRequestException(400,"RoomName không thể null");
        }
        if(isExistRoom(roomCode, request.getLocation())) {
            throw new BadRequestException(400, "Room đã tồn tại");
        }
        if (request.getLocation() == null) {
            throw new BadRequestException(400, "Location không thể null");
        }
    }

    private void validateUpdateRoom(RoomRequest request) {
        String roomCode = request.getRoomCode();
        if(roomCode == null) {
            throw new BadRequestException(400,"RoomCode không thể null");
        }
        if(request.getRoomName() == null) {
            throw new BadRequestException(400,"RoomName không thể null");
        }
        if(!isExistRoom(request.getId())) {
            throw new BadRequestException(400, "Room không đã tồn tại");
        }
        if (request.getLocation() == null) {
            throw new BadRequestException(400, "Location không thể null");
        }
    }
}
