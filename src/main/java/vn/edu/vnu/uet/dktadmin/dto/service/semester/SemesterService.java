package vn.edu.vnu.uet.dktadmin.dto.service.semester;

import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.vnu.uet.dktadmin.common.Constant;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Semester;
import vn.edu.vnu.uet.dktadmin.dto.model.Student;
import vn.edu.vnu.uet.dktadmin.rest.model.CheckExistRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SemesterService {
    private final SemesterDao semesterDao;

    private final MapperFacade mapperFacade;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        Semester semester = generateSemester(semesterRequest);
        semester.setStatus(Constant.UNREGISTERED);

        return generateResponse(store(semester));
    }

    @Transactional
    public SemesterResponse update(SemesterRequest request) {
        validateUpdateSemester(request);
        Semester semester = generateSemester(request);
        Semester semesterDB = semesterDao.getById(request.getId());
        semester.setStatus(semesterDB.getStatus());
        semester.setId(request.getId());

        return generateResponse(semesterDao.store(semester));
    }

    public SemesterResponse active(Long id) {
        Semester semester = semesterDao.getById(id);
        if (semester == null) {
            throw new BadRequestException(400, "Semester không tồn tại");
        }
        semester.setStatus(Constant.REGISTERING);
        return mapperFacade.map(store(semester), SemesterResponse.class);
    }

    public SemesterResponse done(Long id) {
        Semester semester = semesterDao.getById(id);
        if (semester == null) {
            throw new BadRequestException(400, "Semester không tồn tại");
        }
        semester.setStatus(Constant.REGISTERED);
        return mapperFacade.map(store(semester), SemesterResponse.class);
    }

    public SemesterResponse getSemesterById(Long id) {
        return generateResponse(semesterDao.getById(id));
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

    private Semester generateSemester(SemesterRequest request) {
        Semester semester = new Semester();
        semester.setSemesterCode(request.getSemesterCode());
        semester.setSemesterName(request.getSemesterName());
        semester.setDescription(request.getDescription());
        semester.setYear(request.getYear());
        LocalDateTime startDate = LocalDateTime.parse(request.getStartDate(), format);
        semester.setStartDate(startDate);
        LocalDateTime endDate = LocalDateTime.parse(request.getEndDate(), format);
        semester.setEndDate(endDate);

        return semester;
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

    public Boolean checkExistSemester(CheckExistRequest checkExistRequest) {
        if (Constant.ADD.equalsIgnoreCase(checkExistRequest.getMode())) {
            Semester semester = semesterDao.getBySemesterCode(checkExistRequest.getCode());
            return semester == null;
        } else if (Constant.EDIT.equalsIgnoreCase(checkExistRequest.getMode())){
            Semester semester = semesterDao.getBySemesterCode(checkExistRequest.getCode());
            Semester semesterById = semesterDao.getById(checkExistRequest.getId());
            if (semester == null) return false;
            if (semesterById == null) return true;
            if (semester.getSemesterCode().equals(semesterById.getSemesterCode())) {
                return true;
            }
            return false;
        }
        throw new BadRequestException(400, "Mode không tồn tại");
    }

    private Semester store(Semester semester) {
        return semesterDao.store(semester);
    }

    private SemesterResponse generateResponse(Semester semester) {
        SemesterResponse semesterResponse = mapperFacade.map(semester, SemesterResponse.class);
        semesterResponse.setStartDate(semester.getStartDate().format(format));
        semesterResponse.setEndDate(semester.getEndDate().format(format));
        semesterResponse.setStatus(semester.getStatus());
        return semesterResponse;
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
