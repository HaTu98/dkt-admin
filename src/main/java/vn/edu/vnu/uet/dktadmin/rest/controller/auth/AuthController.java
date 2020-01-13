package vn.edu.vnu.uet.dktadmin.rest.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dkt.rest.model.auth.LoginRequest;
import vn.edu.vnu.uet.dkt.rest.model.auth.LoginResponse;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.dto.service.AuthenticationService;
import vn.edu.vnu.uet.dktadmin.rest.controller.BaseController;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/auth")
public class AuthController extends BaseController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request, BindingResult result){

        if (result.hasErrors()) {
            throw new FormValidateException(result);
        }
        return authenticationService.login(request);
    }
}
