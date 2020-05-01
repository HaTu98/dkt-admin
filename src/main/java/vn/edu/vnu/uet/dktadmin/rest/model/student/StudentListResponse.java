package vn.edu.vnu.uet.dktadmin.rest.model.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentListResponse {
    @JsonProperty(value = "Students")
    List<StudentResponse> students;

    public StudentListResponse (List<StudentResponse> students) {
        this.students = students;
    }
}
