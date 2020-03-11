package vn.edu.vnu.uet.dktadmin.dto.service.exam;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.dao.exam.ExamDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Exam;
import vn.edu.vnu.uet.dktadmin.rest.model.exam.ExamRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.exam.ExamResponse;

@Service
public class ExamService {

    @Autowired
    private ExamDao examDao;

    @Autowired
    private MapperFacade mapperFacade;

    public ExamResponse create(ExamRequest request) {
        Exam exam = mapperFacade.map(request, Exam.class);
        exam = examDao.store(exam);
        return mapperFacade.map(exam,ExamResponse.class);
    }
}
