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
import vn.edu.vnu.uet.dktadmin.rest.model.PageBaseRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.ListSubjectResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.SubjectRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.SubjectResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubjectService {
    private final SubjectDao subjectDao;
    private final MapperFacade mapperFacade;

    public SubjectService(SubjectDao subjectDao, MapperFacade mapperFacade) {
        this.subjectDao = subjectDao;
        this.mapperFacade = mapperFacade;
    }

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

    public SubjectResponse updateSubject(SubjectRequest request) {
        validateUpdate(request);
        Subject subject = mapperFacade.map(request, Subject.class);
        subject.setId(subjectDao.getBySubjectCode(request.getSubjectCode()).getId());
        return mapperFacade.map(subjectDao.store(subject),SubjectResponse.class);
    }

    public void deleteSubject(Long id) {
        subjectDao.delete(id);
    }

    public ListSubjectResponse getSubject(PageBaseRequest request) {
        List<Subject> subjects =  subjectDao.getAll();
        return pagingSubject(subjects,request);
    }

    public ListSubjectResponse searchSubject(String query, PageBaseRequest pageRequest) {
        List<Subject> subjectCodes = subjectDao.getLikeCode(query);
        List<Subject> subjectNames = subjectDao.getLikeName(query);
        Map<Long, Subject> subjectMap = new HashMap<>();
        for (Subject subject : subjectCodes) {
            subjectMap.put(subject.getId(), subject);
        }
        for (Subject subject : subjectNames) {
            subjectMap.put(subject.getId(), subject);
        }
        subjectMap = subjectMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return pagingSubject(
                new ArrayList<>(subjectMap.values()),pageRequest
        );
    }

    private ListSubjectResponse pagingSubject(List<Subject> subjects, PageBaseRequest pageRequest) {
        List<Subject> subjectList =  new ArrayList<>();
        Integer page = pageRequest.getPage();
        Integer size = pageRequest.getSize();
        int total = subjects.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            subjectList.add(subjects.get(i));
        }
        ListSubjectResponse response = new ListSubjectResponse(mapperFacade.mapAsList(subjectList,SubjectResponse.class));
        PageResponse pageResponse = new PageResponse(page, size, total);
        response.setPageResponse(pageResponse);
        return response;
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

    public void validateUpdate(SubjectRequest request) {
        if (StringUtils.isEmpty(request.getSubjectCode())) {
            throw new BadRequestException(400, "Mã môn học không thể null");
        }
        if (StringUtils.isEmpty(request.getSubjectName())) {
            throw new BadRequestException(400, "Tên môn học không thể null");
        }
        if (StringUtils.isEmpty(request.getNumberOfCredit())) {
            throw new BadRequestException(400, "Số tín chỉ không thể null");
        }
        if (!isExistSubject(request.getSubjectCode())) {
            throw new BadRequestException(400, "Môn học không tồn tại");
        }
    }

}
