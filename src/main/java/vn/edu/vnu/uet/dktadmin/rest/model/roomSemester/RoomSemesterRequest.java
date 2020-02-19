package vn.edu.vnu.uet.dktadmin.rest.model.roomSemester;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomSemesterRequest {
    private int numberOfComputer;
    private String semesterCode;
    private String roomCode;
}
