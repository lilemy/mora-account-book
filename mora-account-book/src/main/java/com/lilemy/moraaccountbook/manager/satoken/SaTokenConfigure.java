package com.lilemy.moraaccountbook.manager.satoken;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.lilemy.moraaccountbook.model.enums.user.UserRoleEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SaToken 配置类
 *
 * @author lilemy
 * @date 2026-08-21
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    private static final String[] NOT_MATCH_PATH = {"/*.html", "/**/*.html", "/**/*.js", "/**/*.css", "**/swagger-config", "**/api-docs"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> {
            SaRouter.match("/**")
                    .notMatch(NOT_MATCH_PATH)
                    .notMatch(SaHttpMethod.OPTIONS)
                    .check(r -> StpUtil.checkLogin());

            // 匹配所有 admin 开头的路径，要求登录用户必须有为管理员
            SaRouter.match("/*/admin/**", r -> StpUtil.checkRole(UserRoleEnum.ADMIN.toString()));
        })).addPathPatterns("/**").excludeHttpMethods(HttpMethod.OPTIONS);
    }
}
