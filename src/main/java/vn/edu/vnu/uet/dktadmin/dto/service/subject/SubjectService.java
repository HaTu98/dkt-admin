package vn.edu.vnu.uet.dktadmin.dto.service.subject;

import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.dto.dao.subject.SubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Subject;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.SubjectRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.SubjectResponse;

@Service
public class SubjectService {
    @Autowired
    private SubjectDao subjectDao;

    @Autowired
    private MapperFacade mapperFacade;

    public boolean isExistSubject(Long subjectId) {
        Subject subject = subjectDao.getById(subjectId);
        return subject != null;
    }
    public boolean isExistSubject(String  subjectCode) {
        Subject subject = subjectDao.getBySubjectCode(subjectCode);
        return subject != null;
    }
    @Transactional
    public SubjectResponse createSubject(SubjectRequest request) {
        validateSubject(request);
        Subject subject = mapperFacade.map(request, Subject.class);
        return mapperFacade.map(subjectDao.store(subject),SubjectResponse.class);
    }


    public void validateSubject(SubjectRequest request) {
        if (StringUtils.isEmpty(request.getSubjectCode())) {
            throw new BadRequestException(400, "Mã môn học không thể null");
        }
        if (StringUtils.isEmpty(request.getSubjectName())) {
            throw new BadRequestException(400, "Tên môn học không thể null");
        }
        if (StringUtils.isEmpty(request.getNumberOfCredit())) {
            throw new BadRequestException(400, "Số tín chỉ không thể null");
        }

        if (isExistSubject(request.getSubjectCode())) {
            throw new BadRequestException(400, "Môn học đã tồn tại");
        }

    }

}
