package vn.edu.vnu.uet.dktadmin.rest.model.exam;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ExamRequest {
    private Integer numberOfStudent;
    private String examCode;
    private LocalTime startTime;
    private Long duration;
    private LocalDate date;
    private Long subjectId;
    private Long roomSemesterId;
}
