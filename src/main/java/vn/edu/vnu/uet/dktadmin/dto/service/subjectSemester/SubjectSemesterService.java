package vn.edu.vnu.uet.dktadmin.dto.service.subjectSemester;

import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.dto.dao.studentSubject.StudentSubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.subject.SubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.subjectSemester.SubjectSemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Subject;
import vn.edu.vnu.uet.dktadmin.dto.model.SubjectSemester;
import vn.edu.vnu.uet.dktadmin.dto.service.semester.SemesterService;
import vn.edu.vnu.uet.dktadmin.dto.service.subject.SubjectService;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester.ListSubjectSemesterResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester.SubjectSemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester.SubjectSemesterResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectSemesterService {
    private final SubjectSemesterDao subjectSemesterDao;
    private final MapperFacade mapperFacade;
    private final SubjectService subjectService;
    private final SemesterService semesterService;
    private final StudentSubjectDao studentSubjectDao;
    private final SubjectDao subjectDao;

    public SubjectSemesterService(SubjectSemesterDao subjectSemesterDao, MapperFacade mapperFacade, SubjectService subjectService, SemesterService semesterService, StudentSubjectDao studentSubjectDao, SubjectDao subjectDao) {
        this.subjectSemesterDao = subjectSemesterDao;
        this.mapperFacade = mapperFacade;
        this.subjectService = subjectService;
        this.semesterService = semesterService;
        this.studentSubjectDao = studentSubjectDao;
        this.subjectDao = subjectDao;
    }

    @Transactional
    public SubjectSemesterResponse create(SubjectSemesterRequest request) {
        validateSubjectSemester(request);
        SubjectSemester subjectSemester = mapperFacade.map(request, SubjectSemester.class);

        SubjectSemesterResponse response = mapperFacade.map(store(subjectSemester), SubjectSemesterResponse.class);
        Integer numberStudent = studentSubjectDao.countStudentInSubject(subjectSemester.getId());
        response.setNumberStudent(numberStudent);
        return response;
    }

    public SubjectSemesterResponse update(SubjectSemesterRequest request) {
        validateUpdateSubjectSemester(request);
        SubjectSemester subjectSemester = subjectSemesterDao.getById(request.getId());
        if (subjectSemester == null) {
            throw new BadRequestException(400, "SubjectSemester không tồn tại");
        }
        subjectSemester.setDescription(request.getDescription());
        subjectSemester.setSemesterId(request.getSemesterId());
        subjectSemester.setSubjectId(request.getSubjectId());
        SubjectSemesterResponse response = mapperFacade.map(subjectSemesterDao.store(subjectSemester), SubjectSemesterResponse.class);
        Integer numberStudent = studentSubjectDao.countStudentInSubject(subjectSemester.getId());
        response.setNumberStudent(numberStudent);
        return response;
    }

    public void deleteListSubjectSemester(List<Long> ids) {
        List<SubjectSemester> subjectSemesters = subjectSemesterDao.getBySubjectSemesterIdInList(ids);
        subjectSemesterDao.deleteList(subjectSemesters);
    }

    public SubjectSemesterResponse getById(Long id) {
        return mapperFacade.map(subjectSemesterDao.getById(id),SubjectSemesterResponse.class);
    }

    public boolean existSubjectSemester(Long id) {
        SubjectSemester subjectSemester = subjectSemesterDao.getById(id);
        return subjectSemester != null;
    }

    public boolean existSubjectSemester(Long subjectId, Long semesterId) {
        SubjectSemester subjectSemester = subjectSemesterDao.getBySubjectIdAndSemesterId(subjectId, semesterId);
        return subjectSemester != null;
    }

    public ListSubjectSemesterResponse getSemester(Long id,PageBase pageBase) {
        List<SubjectSemester> subjectSemesters = subjectSemesterDao.getBySemesterId(id);
        return getSubjectSemesterPaging(subjectSemesters, pageBase);
    }

    private ListSubjectSemesterResponse getSubjectSemesterPaging(List<SubjectSemester> subjectSemesters, PageBase pageBase) {
        List<SubjectSemesterResponse> responseList = new ArrayList<>();
        Integer page = pageBase.getPage();
        Integer size = pageBase.getSize();
        int total = subjectSemesters.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            SubjectSemesterResponse response = mapperFacade.map(subjectSemesters.get(i), SubjectSemesterResponse.class);
            Integer numberStudent = studentSubjectDao.countStudentInSubject(subjectSemesters.get(i).getId());
            Subject subject = subjectDao.getById(subjectSemesters.get(i).getSubjectId());

            response.setNumberStudent(numberStudent);
            response.setSubjectName(subject.getSubjectName());
            response.setSubjectCode(subject.getSubjectCode());
            response.setNumberOfCredit(subject.getNumberOfCredit());
            responseList.add(response);
        }
        PageResponse pageResponse = new PageResponse(page, size, total);
        return new ListSubjectSemesterResponse(
                responseList,
                pageResponse
        );
    }

    private void validateSubjectSemester(SubjectSemesterRequest request) {
        if (request.getSubjectId() == null) {
            throw new BadRequestException(400, "Subject không thể null");
        }
        if (request.getSemesterId() == null) {
            throw new BadRequestException(400, "Semester không thể null");
        }
        if (!subjectService.isExistSubject(request.getSubjectId())) {
            throw new BadRequestException(400, "Subject không tồn tại");
        }
        if (!semesterService.isExistSemester(request.getSemesterId())) {
            throw new BadRequestException(400, "Semester không tồn tại");
        }
        if (existSubjectSemester(request.getSubjectId(), request.getSemesterId())) {
            throw new BadRequestException(400, "SubjectSemester đã tồn tại");
        }
    }

    private void validateUpdateSubjectSemester(SubjectSemesterRequest request) {
        if (request.getSubjectId() == null) {
            throw new BadRequestException(400, "Subject không thể null");
        }
        if (request.getSemesterId() == null) {
            throw new BadRequestException(400, "Semester không thể null");
        }
        if (!subjectService.isExistSubject(request.getSubjectId())) {
            throw new BadRequestException(400, "Subject không tồn tại");
        }
        if (!semesterService.isExistSemester(request.getSemesterId())) {
            throw new BadRequestException(400, "Semester không tồn tại");
        }
        if (!existSubjectSemester(request.getSubjectId(), request.getSemesterId())) {
            throw new BadRequestException(400, "SubjectSemester không tồn tại");
        }
    }

    private SubjectSemester store(SubjectSemester subjectSemester) {
        return subjectSemesterDao.store(subjectSemester);
    }
}
