package vn.edu.vnu.uet.dktadmin.dto.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.common.Constant;
import vn.edu.vnu.uet.dktadmin.common.exception.UnAuthorizeException;
import vn.edu.vnu.uet.dktadmin.common.model.DktAdmin;
import vn.edu.vnu.uet.dktadmin.common.security.JwtTokenHelper;
import vn.edu.vnu.uet.dktadmin.common.validator.EmailValidator;
import vn.edu.vnu.uet.dktadmin.dto.dao.admin.AdminDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Admin;
import vn.edu.vnu.uet.dktadmin.rest.model.auth.LoginRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.auth.LoginResponse;

@Service
public class AuthenticationService {
    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    public LoginResponse login(LoginRequest request) throws UnAuthorizeException {
        String username = request.getUsername();
        String password = request.getPassword();
        if (isAdminInMemory(username,password)) {
            DktAdmin dktAdmin = DktAdmin.builder()
                    .id(0l)
                    .email("")
                    .username(username)
                    .role("Admin")
                    .build();
            return generateLoginResponse(dktAdmin);

        }
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new UnAuthorizeException(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
        Admin admin = this.getUsernameOrEmail(username);
        if(admin == null) {
            throw new UnAuthorizeException(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        boolean result = passwordEncoder.matches( request.getPassword(), admin.getPassword());
        if (!result) {
            throw new UnAuthorizeException(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        DktAdmin dktAdmin = mapper.convertValue(admin, DktAdmin.class);
        return generateLoginResponse(dktAdmin);
    }

    private Admin getUsernameOrEmail(String username) {
        if (emailValidator.validateEmail(username)) {
            return adminDao.getByEmail(username);
        }
        return adminDao.getByUsername(username);
    }

    private boolean isAdminInMemory(String username, String password){
        if (!Constant.adUsername.equals(username))
            return false;
        if(!Constant.adPassword.equals(password))
            return false;
        return true;
    }

    private LoginResponse generateLoginResponse(DktAdmin dktAdmin) {
        String token = jwtTokenHelper.generateTokenStudent(dktAdmin);
        return LoginResponse.builder()
                .token(token)
                .build();
    }
}
