package vn.edu.vnu.uet.dktadmin.dto.service.room;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.dto.dao.room.RoomDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Room;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomResponse;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomDao roomDao;

    @Autowired
    private MapperFacade mapperFacade;

    public RoomResponse createRoom(RoomRequest request) {
        validateRoom(request);
        Room roomRequest = mapperFacade.map(request, Room.class);
        return mapperFacade.map(roomDao.createRoom(roomRequest), RoomResponse.class);
    }

    public RoomListResponse getAllRoom() {
        List<Room> rooms = roomDao.getAllRoom();
        if (CollectionUtils.isEmpty(rooms)) return null;

        List<RoomResponse> roomResponses = mapperFacade.mapAsList(rooms, RoomResponse.class);
        return new RoomListResponse(roomResponses);
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
    }
}
