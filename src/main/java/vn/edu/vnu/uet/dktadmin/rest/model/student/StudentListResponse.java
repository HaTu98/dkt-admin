package vn.edu.vnu.uet.dktadmin.rest.model.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StudentListResponse {
    List<StudentResponse> students;
}
