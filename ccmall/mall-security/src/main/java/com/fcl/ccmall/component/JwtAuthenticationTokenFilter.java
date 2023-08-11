package com.fcl.ccmall.component;

import com.fcl.ccmall.common.enums.UserFlagEnum;
import com.fcl.ccmall.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${CUSTOMER_PERMISSIONS_REDIS_PRE}")
    private String CUSTOMER_PERMISSIONS_REDIS_PRE;
    @Value("${MANAGE_PERMISSIONS_REDIS_PRE}")
    private String MANAGE_PERMISSIONS_REDIS_PRE;

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (token != null) {
            String username = jwtTokenUtils.getUsername(token);
            String userFlag = jwtTokenUtils.getUserFlag(token);
            log.info("当前用户名为{}", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 我们把用户的权限信息保存到redis中 因此可以从redis中取到我们的数据
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UserDetails userDetails = null;
                if (UserFlagEnum.MANAGER.getDescription().equals(userFlag)) {
                    userDetails = (UserDetails) redisTemplate.opsForValue()
                            .get(MANAGE_PERMISSIONS_REDIS_PRE + username);
                }
                if (UserFlagEnum.CUSTOMER.getDescription().equals(userFlag)){
                    userDetails = (UserDetails) redisTemplate.opsForValue()
                            .get(CUSTOMER_PERMISSIONS_REDIS_PRE + username);
                }
                log.info("从redis中获取数据{}", userDetails);
                if (null == userDetails) {
                    //如果redis中没有数据 那么从数据库中获取权限数据
                    userDetails = userDetailsService.loadUserByUsername(username);
                }
                if (jwtTokenUtils.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info("authenticated user:{}", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
