package vn.edu.vnu.uet.dktadmin.dto.service.room;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.utilities.Util;
import vn.edu.vnu.uet.dktadmin.dto.dao.location.LocationDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.room.RoomDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Location;
import vn.edu.vnu.uet.dktadmin.dto.model.Room;
import vn.edu.vnu.uet.dktadmin.dto.service.location.LocationService;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final RoomDao roomDao;
    private final LocationDao locationDao;
    private final LocationService locationService;
    private final MapperFacade mapperFacade;

    public RoomService(RoomDao roomDao, LocationDao locationDao, LocationService locationService, MapperFacade mapperFacade) {
        this.roomDao = roomDao;
        this.locationDao = locationDao;
        this.locationService = locationService;
        this.mapperFacade = mapperFacade;
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


    private RoomListResponse getListRoomPaging(List<Room> rooms, PageBase pageBase) {
        List<Room> roomList = new ArrayList<>();
        Integer size = pageBase.getSize();
        Integer page = pageBase.getPage();
        int total = rooms.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            roomList.add(rooms.get(i));
        }
        PageResponse pageResponse = new PageResponse(page, size, total);
        return new RoomListResponse(mapperFacade.mapAsList(roomList, RoomResponse.class), pageResponse);
    }

    public boolean isExistRoom(Long roomId){
        Room room = roomDao.getById(roomId);
        return room != null;
    }

    public boolean isExistRoom(String roomCode){
        Room room = roomDao.getByRoomCode(roomCode);
        return room != null;
    }

    private void validateRoom(RoomRequest request) {
        String roomCode = request.getRoomCode();
        if(roomCode == null) {
            throw new BadRequestException(400,"RoomCode không thể null");
        }
        if(request.getRoomName() == null) {
            throw new BadRequestException(400,"RoomName không thể null");
        }
        if(isExistRoom(roomCode)) {
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
