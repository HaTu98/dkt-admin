package vn.edu.vnu.uet.dktadmin.dto.service.studentSubject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubject;

import java.util.List;

@Service
public class StudentSubjectService {
    @Autowired
    private StudentSubjectDao studentSubjectDao;

    public StudentSubject create (StudentSubject request) {
        return studentSubjectDao.store(request);
    }

    public List<StudentSubject> getAll() {
        return studentSubjectDao.getAll();
    }
}
