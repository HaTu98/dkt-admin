package vn.edu.vnu.uet.dktadmin.dto.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.common.Constant;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.exception.UnAuthorizeException;
import vn.edu.vnu.uet.dktadmin.common.model.DktAdmin;
import vn.edu.vnu.uet.dktadmin.common.security.AccountService;
import vn.edu.vnu.uet.dktadmin.common.security.JwtTokenHelper;
import vn.edu.vnu.uet.dktadmin.common.validator.EmailValidator;
import vn.edu.vnu.uet.dktadmin.dto.dao.admin.AdminDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Admin;
import vn.edu.vnu.uet.dktadmin.rest.model.auth.ChangePasswordRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.auth.LoginRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.auth.LoginResponse;

@Service
public class AuthenticationService {
    private final EmailValidator emailValidator;
    private final AdminDao adminDao;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper mapper;
    private final JwtTokenHelper jwtTokenHelper;
    private final AccountService accountService;

    public AuthenticationService(EmailValidator emailValidator, AdminDao adminDao, PasswordEncoder passwordEncoder, ObjectMapper mapper, JwtTokenHelper jwtTokenHelper, AccountService accountService) {
        this.emailValidator = emailValidator;
        this.adminDao = adminDao;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.jwtTokenHelper = jwtTokenHelper;
        this.accountService = accountService;
    }

    public LoginResponse login(LoginRequest request) throws UnAuthorizeException {
        String username = request.getUsername();
        String password = request.getPassword();
        if (isAdminInMemory(username,password)) {
            DktAdmin dktAdmin = DktAdmin.builder()
                    .id(0L)
                    .email("")
                    .fullName("TuHV")
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

    public void changePassword(ChangePasswordRequest request) {
        DktAdmin dktAdmin = accountService.getUserSession();
        String password = request.getPassword();
        String passwordNew = request.getPasswordNew();
        String passwordConfirm = request.getPasswordConfirm();
        if (password == null ||passwordNew == null|| passwordConfirm == null) {
            throw new BaseException(400, "Password không hợp lệ");
        }
        Admin admin = adminDao.getById(dktAdmin.getId());
        boolean result = passwordEncoder.matches(password, admin.getPassword());
        if (!result) {
            throw new BaseException(400, "Password không hợp lệ");
        }
        if (!passwordNew.equals(passwordConfirm)) {
            throw new BaseException(400, "Password new and PasswordConfirm not match!");
        }
        String passwordEncode = passwordEncoder.encode(passwordNew);
        admin.setPassword(passwordEncode);
        adminDao.save(admin);
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
        return Constant.adPassword.equals(password);
    }

    private LoginResponse generateLoginResponse(DktAdmin dktAdmin) {
        String token = jwtTokenHelper.generateToken(dktAdmin);
        return LoginResponse.builder()
                .token(token)
                .build();
    }
}
