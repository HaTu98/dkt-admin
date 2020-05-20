package vn.edu.vnu.uet.dktadmin.dto.dao.studentSubject;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubject;
import vn.edu.vnu.uet.dktadmin.dto.repository.StudentSubjectRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class StudentSubjectDaoImpl implements StudentSubjectDao {
    private final StudentSubjectRepository studentSubjectRepository;

    public StudentSubjectDaoImpl(StudentSubjectRepository studentSubjectRepository) {
        this.studentSubjectRepository = studentSubjectRepository;
    }

    @Override
    public List<StudentSubject> getAll() {
        return studentSubjectRepository.findAll();
    }

    @Override
    public StudentSubject store(StudentSubject studentSubject) {
        return studentSubjectRepository.save(studentSubject);
    }

    @Override
    public StudentSubject getByStudentAndSubjectSemesterId(Long studentId, Long subjectSemesterId) {
        return studentSubjectRepository.findByStudentIdAndSubjectSemesterId(studentId, subjectSemesterId);
    }

    @Override
    public StudentSubject getById(Long id) {
        return studentSubjectRepository.findById(id).orElse(null);
    }

    @Override
    public List<StudentSubject> getBySemesterId(Long semesterId) {
        return studentSubjectRepository.findBySemesterId(semesterId);
    }

    @Override
    public List<StudentSubject> getBySubjectSemesterId(Long subjectSemesterId) {
        return studentSubjectRepository.findBySubjectSemesterId(subjectSemesterId);
    }

    @Override
    public void delete(StudentSubject studentSubject) {
        studentSubjectRepository.delete(studentSubject);
    }

    @Override
    public List<StudentSubject> getByIsNotRegistered(Long id) {
        List<StudentSubject> studentSubjects = studentSubjectRepository.findBySemesterIdAndIsRegisteredIsFalse(id);
        if (CollectionUtils.isEmpty(studentSubjects)) {
            return new ArrayList<>();
        }
        return studentSubjects;
    }

    @Override
    public List<StudentSubject> getByIsRegisteredAndStudentId(Long id, Long studentId) {
        List<StudentSubject> studentSubjects = studentSubjectRepository.
                findBySemesterIdAndStudentIdAndIsRegisteredIsTrue(id,studentId);
        if (CollectionUtils.isEmpty(studentSubjects)) {
            return new ArrayList<>();
        }
        return studentSubjects;
    }

    @Override
    public Integer countStudentInSubject(Long subjectSemesterId) {
        Integer num = studentSubjectRepository.countBySubjectSemesterId(subjectSemesterId);
        return num == null ? 0 : num;
    }


}
