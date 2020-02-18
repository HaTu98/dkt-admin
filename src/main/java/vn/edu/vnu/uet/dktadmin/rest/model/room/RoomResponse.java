package vn.edu.vnu.uet.dktadmin.rest.model.room;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomResponse {
    private String name;
    private String code;
    private String location;
    private String description;
}
