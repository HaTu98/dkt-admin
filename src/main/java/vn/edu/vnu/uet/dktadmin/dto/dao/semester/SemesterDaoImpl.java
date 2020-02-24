package vn.edu.vnu.uet.dktadmin.dto.dao.semester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.Semester;
import vn.edu.vnu.uet.dktadmin.dto.repository.SemesterRepository;

@Service
public class SemesterDaoImpl implements SemesterDao{
    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    public Semester getBySemesterCode(String semesterCode) {
        return semesterRepository.findOneBySemesterCode(semesterCode);
    }
}
