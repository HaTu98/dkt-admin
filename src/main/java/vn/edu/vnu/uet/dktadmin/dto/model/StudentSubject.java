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

    private Long subjectId;
    private Long studentId;
    private Long semesterId;
    private String status;
    private Instant createdAt;
    private Instant modifiedAt;
    private Instant createdBy;
    private Instant modifiedBy;
}
