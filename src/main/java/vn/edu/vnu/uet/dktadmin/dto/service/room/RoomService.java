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
        String code = request.getRoomCode();
        Room roomInDB  = roomDao.getByCode(code);
        if (roomInDB != null) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), "Room đã tồn tại");
        }
        Room roomRequest = mapperFacade.map(request, Room.class);

        Room response = roomDao.createRoom(roomRequest);
        return mapperFacade.map(response, RoomResponse.class);
    }

    public RoomListResponse getAllRoom() {
        List<Room> rooms = roomDao.getAllRoom();
        if (CollectionUtils.isEmpty(rooms)) return null;

        List<RoomResponse> roomResponses = mapperFacade.mapAsList(rooms, RoomResponse.class);
        return new RoomListResponse(roomResponses);
    }
}
