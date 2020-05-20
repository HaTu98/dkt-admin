package vn.edu.vnu.uet.dktadmin.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dktadmin.dto.model.SubjectSemester;

import java.util.List;

@Repository
public interface SubjectSemesterRepository extends JpaRepository<SubjectSemester,Long> {
    SubjectSemester findBySubjectIdAndSemesterId(Long subjectId, Long semesterId);
    List<SubjectSemester> findBySemesterId(Long semesterId);
    List<SubjectSemester> findByIdIn(List<Long> ids);
}
