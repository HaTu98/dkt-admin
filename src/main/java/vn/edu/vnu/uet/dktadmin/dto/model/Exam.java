package vn.edu.vnu.uet.dktadmin.dto.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numberOfStudent;
    private Integer numberOfStudentSubscribe;
    private LocalDateTime startTime;
    private LocalDate date;
    private Long subjectSemesterId;
    private Long roomSemesterId;
    private Long roomId;
    private Long semesterId;
    private Long subjectId;
    private Long locationId;
    private LocalDateTime endTime;
    @Version
    private long version;
}
