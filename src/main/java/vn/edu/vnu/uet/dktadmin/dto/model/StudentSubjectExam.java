package vn.edu.vnu.uet.dktadmin.dto.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "student_subject_exams")
public class StudentSubjectExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Integer status;
    private Long examId;
    private Long semesterId;
    private Long studentSubjectId;
    private Long studentId;
}
