package com.ajaxjs.base.controller;

import com.ajaxjs.iam.client.BaseOidcClientUserController;
import com.ajaxjs.iam.client.ClientUtils;
import com.ajaxjs.iam.client.model.UserAccessToken;
import com.ajaxjs.iam.jwt.JWebToken;
import com.ajaxjs.iam.jwt.JWebTokenMgr;
import com.ajaxjs.iam.jwt.JwtAccessToken;
import com.ajaxjs.iam.resource_server.Utils;
import com.ajaxjs.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController extends BaseOidcClientUserController {
    @Value("${user.loginPage}")
    private String userLoginCode;

    @Value("${user.clientId}")
    private String clientId;

    @Value("${user.clientSecret}")
    private String clientSecret;

    @Value("${website.url}")
    private String websiteUrl;

    @GetMapping("/login")
    public RedirectView get(HttpSession session, @RequestParam(value = "web_url", required = false) String webUrl) {
        return loginPageUrl(session, userLoginCode, clientId, websiteUrl, webUrl);
    }

    @RequestMapping("/callback")
    public ModelAndView token(@RequestParam String code, @RequestParam(required = false) String state,
                              @RequestParam(value = "web_url", required = false) String webUrl,
                              HttpSession session, HttpServletResponse resp) {
        return callbackToken(clientId, clientSecret, code, state, webUrl, session, resp);
    }

    /**
     * JWT 验证的密钥
     */
    @Value("${user.jwtSecretKey}")
    private String jwtSecretKey;

    /**
     * JWT 解密
     */
    @Bean
    JWebTokenMgr jWebTokenMgr() {
        JWebTokenMgr mgr = new JWebTokenMgr();
        mgr.setSecretKey(jwtSecretKey);

        return mgr;
    }

    @Override
    public JwtAccessToken onAccessTokenGot(JwtAccessToken token, HttpSession session) {
        String idToken = token.getId_token();
        JWebTokenMgr mgr = jWebTokenMgr();
        JWebToken jwt = mgr.parse(idToken);

        if (mgr.isValid(jwt)) {
            UserAccessToken user = new UserAccessToken();
            user.setId(Long.parseLong(jwt.getPayload().getSub()));
            user.setName(jwt.getPayload().getName());

            return token;
//			AccessToken accessToken = new AccessToken();
//			BeanUtils.copyProperties((AccessToken) token, accessToken);
//			user.setAccessToken(accessToken);
//			session.setAttribute(UserLogined.USER_IN_SESSION, user);
        } else
            throw new SecurityException("返回非法 JWT Token");
    }
}
