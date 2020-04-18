package vn.edu.vnu.uet.dktadmin.dto.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "subject_semester")
public class SubjectSemester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String subjectSemesterCode;
    private String description;
    private String createdBy;
    private String modifiedBy;
    private Instant createdAt;
    private Instant modifiedAt;
    private Long subjectId;
    private Long semesterId;
}
