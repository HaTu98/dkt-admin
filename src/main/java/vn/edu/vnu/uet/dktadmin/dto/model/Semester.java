package vn.edu.vnu.uet.dktadmin.dto.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "semesters")
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String semesterName;
    private String semesterCode;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer year;
    private String description;
    private Integer status;
}
