package vn.edu.vnu.uet.dktadmin.rest.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dktadmin.common.exception.UnAuthorizeException;
import vn.edu.vnu.uet.dktadmin.dto.service.auth.AuthenticationService;
import vn.edu.vnu.uet.dktadmin.dto.service.sendMail.ResetPassword;
import vn.edu.vnu.uet.dktadmin.rest.controller.BaseController;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.auth.LoginRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.auth.LoginResponse;

import javax.mail.MessagingException;
import java.util.Map;

@RestController
@RequestMapping("/admin/auth")
public class AuthController extends BaseController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ResetPassword resetPassword;

    @PostMapping("/login")
    public ApiDataResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            return ApiDataResponse.ok(authenticationService.login(request));
        } catch (UnAuthorizeException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        }

    }

    @PostMapping("/forgot_password")
    public ApiDataResponse<String> forgotPassword(@RequestBody Map<String,String> request) throws MessagingException {
        String email = request.get("Email");
        resetPassword.resetPassword(email);
        return ApiDataResponse.ok("success");
    }
}
