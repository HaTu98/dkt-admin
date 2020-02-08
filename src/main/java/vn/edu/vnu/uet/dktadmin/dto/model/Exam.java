package vn.edu.vnu.uet.dktadmin.dto.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

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
    private Long time;
    private LocalDate date;
    private Long subjectId;
    private Long roomSemesterId;
}
