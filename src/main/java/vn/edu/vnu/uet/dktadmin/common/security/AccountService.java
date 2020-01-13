package vn.edu.vnu.uet.dktadmin.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.edu.vnu.uet.dktadmin.common.model.DktAdmin;

@Component
public class AccountService {

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public DktAdmin getUserSession() {
        return (DktAdmin) this.getAuthentication().getPrincipal();
    }
}
