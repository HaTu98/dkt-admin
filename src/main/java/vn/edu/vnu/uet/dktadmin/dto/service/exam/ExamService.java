package vn.edu.vnu.uet.dktadmin.dto.service.exam;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.dto.dao.exam.ExamDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.room.RoomDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.roomSemester.RoomSemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.subjectSemester.SubjectSemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Exam;
import vn.edu.vnu.uet.dktadmin.dto.model.Room;
import vn.edu.vnu.uet.dktadmin.dto.model.RoomSemester;
import vn.edu.vnu.uet.dktadmin.dto.model.SubjectSemester;
import vn.edu.vnu.uet.dktadmin.dto.service.roomSemester.RoomSemesterService;
import vn.edu.vnu.uet.dktadmin.dto.service.subjectSemester.SubjectSemesterService;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.exam.ExamRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.exam.ExamResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.exam.ListExamResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {
    private final ExamDao examDao;
    private final MapperFacade mapperFacade;
    private final SubjectSemesterService subjectSemesterService;
    private final RoomSemesterService roomSemesterService;
    private final SubjectSemesterDao subjectSemesterDao;
    private final RoomSemesterDao roomSemesterDao;
    private final RoomDao roomDao;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ExamService(ExamDao examDao, MapperFacade mapperFacade, SubjectSemesterService subjectSemesterService, RoomSemesterService roomSemesterService, SubjectSemesterDao subjectSemesterDao, RoomSemesterDao roomSemesterDao, RoomDao roomDao) {
        this.examDao = examDao;
        this.mapperFacade = mapperFacade;
        this.subjectSemesterService = subjectSemesterService;
        this.roomSemesterService = roomSemesterService;
        this.subjectSemesterDao = subjectSemesterDao;
        this.roomSemesterDao = roomSemesterDao;
        this.roomDao = roomDao;
    }

    public ExamResponse create(ExamRequest request) {
        validateExam(request);
        Exam exam = generateExam(request);
        Exam response = examDao.store(exam);
        return getExamResponse(response);
    }

    public boolean isExistExam(Long examId) {
        Exam exam = examDao.getById(examId);
        return exam != null;
    }

    public boolean isExistExam(String examCode) {
        Exam exam = examDao.getByExamCode(examCode);
        return exam != null;
    }

    public ExamResponse getById(Long id) {
        Exam exam = examDao.getById(id);
        return getExamResponse(exam);
    }

    public ListExamResponse getBySemesterId(Long id, PageBase pageBase) {
        List<Exam> exams = examDao.getBySemesterId(id);
        return getExamPaging(exams, pageBase);
    }

    public ListExamResponse getExamPaging(List<Exam> exams, PageBase pageBase) {
        List<Exam> examList = new ArrayList<>();
        Integer page = pageBase.getPage();
        Integer size = pageBase.getSize();
        int total = exams.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            examList.add(exams.get(i));
        }
        List<ExamResponse> examResponses = new ArrayList<>();
        for (Exam exam : examList) {
            examResponses.add(getExamResponse(exam));
        }
        return new ListExamResponse(
                examResponses,
                new PageResponse(page, size, total)
        );
    }

    public void validateExam(ExamRequest request) {
        if (request.getRoomSemesterId() == null) {
            throw new BadRequestException(400, "RoomSemester không thể null");
        }
        if (request.getSubjectSemesterId() == null) {
            throw new BadRequestException(400, "SubjectSemester không thể null");
        }
        if (!subjectSemesterService.existSubjectSemester(request.getSubjectSemesterId())) {
            throw new BadRequestException(400, "SubjectSemester không tồn tại");
        }
        if (!roomSemesterService.isExistRoomSemester(request.getRoomSemesterId())) {
            throw new BadRequestException(400, "RoomSemester không tồn tại");
        }
        if (!validateConflictExam(request)) {
            throw new BadRequestException(400, "Thời gian không hợp lệ");
        }
    }

    private ExamResponse getExamResponse(Exam exam) {
        ExamResponse examResponse = mapperFacade.map(exam, ExamResponse.class);
        examResponse.setStartTime(exam.getStartTime().format(format));
        examResponse.setEndTime(exam.getEndTime().format(format));
        examResponse.setDate(exam.getDate().format(format));
        return examResponse;
    }

    private Exam generateExam(ExamRequest request) {
        Exam exam = new Exam();
        exam.setSubjectSemesterId(request.getSubjectSemesterId());
        exam.setRoomSemesterId(request.getRoomSemesterId());
        exam.setNumberOfStudent(request.getNumberOfStudent() == null ? 0 : request.getNumberOfStudent());
        exam.setNumberOfStudentSubscribe(0);

        LocalDateTime startDate = LocalDateTime.parse(request.getDate(), format);
        exam.setDate(startDate);
        LocalDateTime startTime = LocalDateTime.parse(request.getStartTime(), format);
        exam.setStartTime(startTime);
        LocalDateTime endTime = LocalDateTime.parse(request.getEndTime(), format);
        exam.setEndTime(endTime);
        exam.setExamCode(request.getExamCode());

        RoomSemester roomSemester = roomSemesterDao.getById(request.getRoomSemesterId());
        Room room = roomDao.getById(roomSemester.getRoomId());
        exam.setLocationId(room.getLocationId());

        SubjectSemester subjectSemester = subjectSemesterDao.getById(request.getSubjectSemesterId());
        exam.setSemesterId(subjectSemester.getSemesterId());
        return exam;
    }


    private boolean validateConflictExam(ExamRequest request) {
        LocalDateTime date = LocalDateTime.parse(request.getDate(), format);
        List<Exam> exams = examDao.getByRoomAndDate(request.getRoomSemesterId(), date);
        if (CollectionUtils.isEmpty(exams)) {
            return true;
        }

        LocalDateTime startTime = LocalDateTime.parse(request.getStartTime(), format);
        LocalDateTime endTime = LocalDateTime.parse(request.getEndTime(), format);
        for (Exam exam : exams) {
            LocalDateTime start = exam.getStartTime();
            LocalDateTime end = exam.getEndTime();
            if (!startTime.isBefore(start) && !startTime.isAfter(end)) {
                return false;
            }
            if (!endTime.isBefore(start) &&  !endTime.isAfter(end)) {
                return false;
            }
            if (!startTime.isAfter(start) && !endTime.isBefore(end)) {
                return false;
            }
        }
        return true;
    }
}
