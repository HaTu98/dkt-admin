package vn.edu.vnu.uet.dktadmin.dto.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;
    private String roomCode;
    private Long locationId;
    private String description;
}
