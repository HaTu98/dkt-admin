package vn.edu.vnu.uet.dktadmin.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.vnu.uet.dktadmin.dto.model.SubjectSemester;

@Repository
public interface SubjectSemesterRepository extends JpaRepository<SubjectSemester,Long> {
}
