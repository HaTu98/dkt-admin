package vn.edu.vnu.uet.dktadmin.dto.service.semester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Semester;

@Service
public class SemesterService {
    @Autowired
    private SemesterDao semesterDao;

    public boolean existSemester(String semesterCode){
        Semester semester = semesterDao.getBySemesterCode(semesterCode);
        return semester != null;
    }
}
