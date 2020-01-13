package vn.edu.vnu.uet.dktadmin.dto.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dkt.rest.model.auth.LoginRequest;
import vn.edu.vnu.uet.dkt.rest.model.auth.LoginResponse;
import vn.edu.vnu.uet.dktadmin.common.exception.UnauthorizedException;
import vn.edu.vnu.uet.dktadmin.common.model.DktStudent;
import vn.edu.vnu.uet.dktadmin.common.security.JwtTokenHelper;
import vn.edu.vnu.uet.dktadmin.common.validator.EmailValidator;
import vn.edu.vnu.uet.dktadmin.dto.dao.admin.AdminDao;
import vn.edu.vnu.uet.dktadmin.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Admin;
import vn.edu.vnu.uet.dktadmin.dto.model.Student;

@Service
public class AuthenticationService {
    @Autowired
    private EmailValidator emailValidator;

    /*@Autowired
    private StudentDao studentDao;*/

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new UnauthorizedException("Tài khoản mật khẩu không chính xác");
        }

        Admin admin = this.getUsernameOrEmail(username);

        boolean result = passwordEncoder.matches( request.getPassword(), admin.getPassword());
        if (!result) {
            throw new UnauthorizedException("Tài khoản mật khẩu không chính xác");
        }

        DktStudent dktStudent = mapper.convertValue(admin, DktStudent.class);
        String token = jwtTokenHelper.generateTokenStudent(dktStudent);
        return LoginResponse.builder()
                .token(token)
                .build();
    }

    private Admin getUsernameOrEmail(String username) {
        if (emailValidator.validateEmail(username)) {
            return adminDao.getByEmail(username);
        }
        return adminDao.getByUsername(username);
    }
}
