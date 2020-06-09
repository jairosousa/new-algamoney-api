package com.algamoney.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("tokens")
public class TokenResource {

    /**
     * Codigo para fazer o Logout
     * Simplemente retira o cookie refreshToken
     * @param request
     * @param response
     */
    @DeleteMapping("revoke")
    public void revoke(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); //// TODO: 09/06/2020 em Produção será true pois vamos usar HTTPS
        cookie.setPath(request.getContextPath().concat("/oauth/token"));
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }
}
