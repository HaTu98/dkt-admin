package vn.edu.vnu.uet.dktadmin.dto.service.exam;

import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.utilities.Util;
import vn.edu.vnu.uet.dktadmin.dto.dao.exam.ExamDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.location.LocationDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.room.RoomDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.roomSemester.RoomSemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.subject.SubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.subjectSemester.SubjectSemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.*;
import vn.edu.vnu.uet.dktadmin.dto.service.roomSemester.RoomSemesterService;
import vn.edu.vnu.uet.dktadmin.dto.service.subjectSemester.SubjectSemesterService;
import vn.edu.vnu.uet.dktadmin.rest.controller.exam.ExamController;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.exam.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExamService {
    private static final Logger log = LoggerFactory.getLogger(ExamController.class);
    private final ExamDao examDao;
    private final MapperFacade mapperFacade;
    private final SubjectSemesterService subjectSemesterService;
    private final RoomSemesterService roomSemesterService;
    private final SubjectSemesterDao subjectSemesterDao;
    private final RoomSemesterDao roomSemesterDao;
    private final SubjectDao subjectDao;
    private final LocationDao locationDao;
    private final RoomDao roomDao;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ExamService(ExamDao examDao, MapperFacade mapperFacade, SubjectSemesterService subjectSemesterService, RoomSemesterService roomSemesterService, SubjectSemesterDao subjectSemesterDao, RoomSemesterDao roomSemesterDao, SubjectDao subjectDao, LocationDao locationDao, RoomDao roomDao) {
        this.examDao = examDao;
        this.mapperFacade = mapperFacade;
        this.subjectSemesterService = subjectSemesterService;
        this.roomSemesterService = roomSemesterService;
        this.subjectSemesterDao = subjectSemesterDao;
        this.roomSemesterDao = roomSemesterDao;
        this.subjectDao = subjectDao;
        this.locationDao = locationDao;
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

    public ListRegisterResultResponse getBySemesterId(Long id, PageBase pageBase) {
        List<Exam> exams = examDao.getBySemesterId(id);
        return generateListRegister(exams, pageBase, id);
    }

    public ListRegisterResultResponse generateListRegister(List<Exam> exams, PageBase pageBase, Long semesterId) {
        List<Exam> examList = new ArrayList<>();
        Integer page = pageBase.getPage();
        Integer size = pageBase.getSize();
        int total = exams.size();
        int maxSize = Math.min(total, size * page);
        for (int i = size * (page - 1); i < maxSize; i++) {
            examList.add(exams.get(i));
        }

        List<RegisterResultResponse> responses = new ArrayList<>();
        List<RoomSemester> roomSemesters = roomSemesterDao.getBySemesterId(semesterId);
        Map<Long,RoomSemester> roomSemesterMap = roomSemesters.stream()
                .collect(Collectors.toMap(RoomSemester::getId, x -> x));
        List<Subject> subjects = subjectDao.getAll();
        Map<Long,Subject> subjectMap = subjects.stream().collect(Collectors.toMap(Subject::getId, x -> x));
        List<Room> rooms = roomDao.getAllRoom();
        Map<Long, Room> roomMap = rooms.stream().collect(Collectors.toMap(Room::getId, x ->x));
        List<Location> locations = locationDao.getAll();
        Map<Long, Location> locationMap = locations.stream().collect(Collectors.toMap(Location::getId, x -> x));
        for (Exam exam : examList) {
            try {
                RegisterResultResponse response = new RegisterResultResponse();
                response.setId(exam.getId());
                response.setSubjectSemesterId(exam.getSubjectSemesterId());

                //Subject subject = subjectDao.getById(exam.getSubjectId());
                Subject subject = subjectMap.get(exam.getSubjectId());
                response.setSubjectName(subject.getSubjectName());
                response.setSubjectCode(subject.getSubjectCode());
                response.setNumberOfCredit(subject.getNumberOfCredit());

                //RoomSemester roomSemester = roomSemesterDao.getById(exam.getRoomSemesterId());
                RoomSemester roomSemester = roomSemesterMap.get(exam.getRoomSemesterId());
                //Room room = roomDao.getById(roomSemester.getRoomId());
                Room room = roomMap.get(roomSemester.getRoomId());
                response.setRoomName(room.getRoomName());

                //Location location = locationDao.getById(exam.getLocationId());
                Location location = locationMap.get(exam.getLocationId());
                response.setLocation(location.getLocationName());

                response.setNumberStudent(exam.getNumberOfStudent());
                response.setNumberOfStudentSubscribe(exam.getNumberOfStudentSubscribe());
                response.setDate(exam.getDate().format(formatDate));

                response.setRoomSemesterId(exam.getRoomSemesterId());

                String startDate = exam.getStartTime().format(format);
                String endDate = exam.getEndTime().format(format);
                String time = startDate.substring(11) + "-" + endDate.substring(11);
                response.setTime(time);
                responses.add(response);
            } catch (Exception e) {
                log.error("get exam error", e);
            }

        }
        return new ListRegisterResultResponse(responses, new PageResponse(page, size, total));
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

    public ListRegisterResultResponse getAll(Long id) {
        List<Exam> exams = examDao.getBySemesterId(id);
        RegisterResultResponse registerResultResponse = new RegisterResultResponse();
        return null;
    }

    private ExamResponse getExamResponse(Exam exam) {
        ExamResponse examResponse = mapperFacade.map(exam, ExamResponse.class);
        examResponse.setStartTime(exam.getStartTime().format(format));
        examResponse.setEndTime(exam.getEndTime().format(format));
        examResponse.setDate(exam.getDate().format(formatDate));
        return examResponse;
    }

    private Exam generateExam(ExamRequest request) {
        Exam exam = new Exam();
        exam.setSubjectSemesterId(request.getSubjectSemesterId());
        exam.setRoomSemesterId(request.getRoomSemesterId());
        exam.setNumberOfStudent(request.getNumberOfStudent() == null ? 0 : request.getNumberOfStudent());
        exam.setNumberOfStudentSubscribe(0);

        LocalDate startDate = LocalDate.parse(request.getDate(), formatDate);
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
        exam.setSubjectId(subjectSemester.getSubjectId());
        return exam;
    }


    private boolean validateConflictExam(ExamRequest request) {
        LocalDate date = LocalDate.parse(request.getDate(), formatDate);
        List<Exam> exams = examDao.getByRoomAndDate(request.getRoomSemesterId(), date);
        if (CollectionUtils.isEmpty(exams)) {
            return true;
        }

        LocalDateTime startTime = LocalDateTime.parse(request.getStartTime(), format);
        LocalDateTime endTime = LocalDateTime.parse(request.getEndTime(), format);
        for (Exam exam : exams) {
            LocalDateTime start = exam.getStartTime();
            LocalDateTime end = exam.getEndTime();
            if (!Util.validateTime(start, end, startTime, endTime)) {
                return false;
            }
        }
        return true;
    }
}
