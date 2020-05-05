package vn.edu.vnu.uet.dktadmin.dto.service.semester;

import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Semester;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterResponse;

@Service
public class SemesterService {
    private final SemesterDao semesterDao;

    private final MapperFacade mapperFacade;

    public SemesterService(MapperFacade mapperFacade, SemesterDao semesterDao) {
        this.mapperFacade = mapperFacade;
        this.semesterDao = semesterDao;
    }

    public boolean isExistSemester(String semesterCode) {
        Semester semester = semesterDao.getBySemesterCode(semesterCode);
        return semester != null;
    }

    public boolean isExistSemester(Long semesterId) {
        Semester semester = semesterDao.getById(semesterId);
        return semester != null;
    }

    @Transactional
    public SemesterResponse create(SemesterRequest semesterRequest) {
        validateSemester(semesterRequest);
        Semester semester = mapperFacade.map(semesterRequest, Semester.class);
        return mapperFacade.map(store(semester), SemesterResponse.class);
    }

    @Transactional
    public SemesterResponse update(SemesterRequest request) {
        validateUpdateSemester(request);
        Semester semester = mapperFacade.map(request, Semester.class);
        return mapperFacade.map(store(semester), SemesterResponse.class);
    }

    public SemesterResponse getSemesterById(Long id) {
        return mapperFacade.map(semesterDao.getById(id), SemesterResponse.class);
    }

    private Semester store(Semester semester) {
        return semesterDao.store(semester);
    }

    private void validateSemester(SemesterRequest request) {
        if (StringUtils.isEmpty(request.getSemesterName())) {
            throw new BadRequestException(400, "Tên học kì không thể null");
        }
        if (StringUtils.isEmpty(request.getSemesterCode())) {
            throw new BadRequestException(400, "Mã học kì không thể null");
        }
        if (isExistSemester(request.getSemesterCode())) {
            throw new BadRequestException(400, "Học kì đã tồn tại");
        }
    }

    private void validateUpdateSemester(SemesterRequest request) {
        if (request.getId() == null) {
            throw new BadRequestException(400, "Id học kì không thể null");
        }
        if (StringUtils.isEmpty(request.getSemesterName())) {
            throw new BadRequestException(400, "Tên học kì không thể null");
        }
        if (StringUtils.isEmpty(request.getSemesterCode())) {
            throw new BadRequestException(400, "Mã học kì không thể null");
        }
        if (!isExistSemester(request.getId())) {
            throw new BadRequestException(400, "Học kì không tồn tại");
        }
    }
}
