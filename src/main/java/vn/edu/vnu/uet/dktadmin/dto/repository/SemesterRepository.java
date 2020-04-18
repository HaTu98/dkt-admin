package vn.edu.vnu.uet.dktadmin.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dktadmin.dto.model.Semester;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    Semester findBySemesterCode(String semesterCode);
}
