package vn.edu.vnu.uet.dktadmin.dto.service.exam;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;
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
import vn.edu.vnu.uet.dktadmin.rest.model.exam.ExamRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.exam.ExamResponse;

@Service
public class ExamService {
    private final ExamDao examDao;
    private final MapperFacade mapperFacade;
    private final SubjectSemesterService subjectSemesterService;
    private final RoomSemesterService roomSemesterService;
    private final SubjectSemesterDao subjectSemesterDao;
    private final RoomSemesterDao roomSemesterDao;
    private final RoomDao roomDao;

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
        Exam exam = mapperFacade.map(request, Exam.class);
        RoomSemester roomSemester = roomSemesterDao.getById(request.getRoomSemesterId());
        Room room = roomDao.getById(roomSemester.getRoomId());
        SubjectSemester subjectSemester = subjectSemesterDao.getById(request.getSubjectSemesterId());
        exam.setSemesterId(subjectSemester.getSemesterId());
        exam.setLocationId(room.getLocationId());
        return mapperFacade.map(examDao.store(exam),ExamResponse.class);
    }

    public boolean isExistExam(Long examId) {
        Exam exam = examDao.getById(examId);
        return exam != null;
    }
    public boolean isExistExam(String examCode) {
        Exam exam  = examDao.getByExamCode(examCode);
        return exam != null;
    }

    public void validateExam(ExamRequest request){
        if (request.getRoomSemesterId() == null) {
            throw new BadRequestException(400, "RoomSemester không thể null");
        }
        if (request.getSubjectSemesterId() == null) {
            throw new BadRequestException(400, "SubjectSemester không thể null");
        }
        if (!subjectSemesterService.existSubjectSemester(request.getSubjectSemesterId())) {
            throw new BadRequestException(400, "SubjectSemester không tồn tại");
        }
        if(!roomSemesterService.isExistRoomSemester(request.getRoomSemesterId())) {
            throw new BadRequestException(400, "RoomSemester không tồn tại");
        }
        if (isExistExam(request.getExamCode())) {
            throw new BadRequestException(400, "ExamCode đã tồn tại");
        }
    }
}
