package vn.edu.vnu.uet.dktadmin.dto.service.semester;

import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.vnu.uet.dktadmin.common.Constant;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Semester;
import vn.edu.vnu.uet.dktadmin.dto.model.Student;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterResponse;

import java.util.*;
import java.util.stream.Collectors;

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
        semester.setStatus(Constant.inActive);
        return mapperFacade.map(store(semester), SemesterResponse.class);
    }

    @Transactional
    public SemesterResponse update(SemesterRequest request) {
        validateUpdateSemester(request);
        Semester semester = mapperFacade.map(request, Semester.class);
        return mapperFacade.map(store(semester), SemesterResponse.class);
    }

    public SemesterResponse active(Long id) {
        Semester semester = semesterDao.getById(id);
        if (semester == null) {
            throw new BadRequestException(400, "Semester không tồn tại");
        }
        semester.setStatus(Constant.active);
        return mapperFacade.map(store(semester), SemesterResponse.class);
    }

    public SemesterResponse getSemesterById(Long id) {
        return mapperFacade.map(semesterDao.getById(id), SemesterResponse.class);
    }

    public SemesterListResponse getAll(PageBase pageBase) {
        List<Semester> semesters = semesterDao.getAll();
        return getListStudentPaging(semesters, pageBase);
    }

    public SemesterListResponse search(String query, PageBase pageBase) {
        List<Semester> semesterLikeCode = semesterDao.getLikeCode(query);
        List<Semester> semesterLikeName = semesterDao.getLikeName(query);
        Map<Long, Semester> semesterMap = new HashMap<>();
        for (Semester semester : semesterLikeCode) {
            semesterMap.put(semester.getId(), semester);
        }
        for (Semester semester : semesterLikeName) {
            semesterMap.put(semester.getId(), semester);
        }
        semesterMap = semesterMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return getListStudentPaging(new ArrayList<>(semesterMap.values()), pageBase);
    }

    private SemesterListResponse getListStudentPaging(List<Semester> semesters,PageBase pageBase) {
        List<Semester> semesterList = new ArrayList<>();
        Integer page = pageBase.getPage();
        Integer size = pageBase.getSize();
        int total = semesters.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            semesterList.add(semesters.get(i));
        }
        PageResponse pageResponse = new PageResponse(page, size, total);
        SemesterListResponse semesterListResponse = new SemesterListResponse(mapperFacade.mapAsList(semesterList, SemesterResponse.class));
        semesterListResponse.setPageResponse(pageResponse);
        return semesterListResponse;
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
