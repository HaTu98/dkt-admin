package vn.edu.vnu.uet.dktadmin.dto.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "student_subjects")
public class StudentSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private Long subjectSemesterId;
    private Long semesterId;
    private Long subjectId;
    private Integer status;
    private Boolean isRegistered;
    private Instant createdAt;
    private Instant modifiedAt;
    private String createdBy;
    private String modifiedBy;
}
