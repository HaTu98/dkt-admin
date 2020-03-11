package vn.edu.vnu.uet.dktadmin.dto.dao.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.Exam;
import vn.edu.vnu.uet.dktadmin.dto.repository.ExamRepository;

@Service
public class ExamDaoImpl implements ExamDao{
    @Autowired
    private ExamRepository examRepository;

    @Override
    public Exam store(Exam exam) {
        return examRepository.save(exam);
    }
}
