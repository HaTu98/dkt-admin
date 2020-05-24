package vn.edu.vnu.uet.dktadmin.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dktadmin.dto.model.Semester;

import java.util.List;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    Semester findBySemesterCode(String semesterCode);
    List<Semester> findBySemesterCodeContains(String code);
    List<Semester> findBySemesterNameContains(String name);
    List<Semester> findByIdIn(List<Long> id);
    List<Semester> findAllByOrderByIdAsc();
    Semester findBySemesterName(String semesterName);
}
