package vn.edu.vnu.uet.dktadmin.rest.model.subjectSemesterExam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentSubjectExamResponse {
    @JsonProperty("Id")
    private Long id;

    @JsonProperty("ExamId")
    private Long examId;

    @JsonProperty("StudentSubjectId")
    private Long studentSubjectId;

    @JsonProperty("FullName")
    private String fullName;

    @JsonProperty("StudentCode")
    private String studentCode;

    @JsonProperty("Gender")
    private Integer gender;

    @JsonProperty("DateOfBirth")
    private String dateOfBirth;

    @JsonProperty(value = "Course")
    private String course;

    @JsonProperty("Status")
    private Integer status;
}
