package vn.edu.vnu.uet.dktadmin.dto.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "room_semesters")
public class RoomSemester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numberOfComputer;
    private Integer preventiveComputer;
    private Long semesterId;
    private Long roomId;
}
