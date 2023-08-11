package com.fcl.ccmall.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import com.fcl.ccmall.annotation.WebLog;
import com.fcl.ccmall.common.utils.TimeUtil;
import com.fcl.ccmall.model.SysWebLog;
import com.fcl.ccmall.service.SysWebLogService;
import com.fcl.ccmall.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Slf4j
@Component
public class WebLogAspect {

    @Resource
    private SysWebLogService sysWebLogService;

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Pointcut("@annotation(com.fcl.ccmall.annotation.WebLog)")
    public void WebLog() {

    }

    @Around("WebLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        SysWebLog sysWebLog = new SysWebLog();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        String urlStr = request.getRequestURL().toString();
        WebLog webLogAnnotation = method.getAnnotation(WebLog.class);
        String token = request.getHeader("token");
        sysWebLog.setDescription(webLogAnnotation.description());
        sysWebLog.setUsername(jwtTokenUtils.getUsername(token));
        sysWebLog.setStartTime(TimeUtil.timeMillisToLocalDateTime(startTime));
        sysWebLog.setSpendTime(new Long(endTime - startTime).intValue());
        sysWebLog.setBasePath(StrUtil.removeSuffix(urlStr, URLUtil.url(urlStr).getPath()));
        sysWebLog.setUrl(request.getRequestURL().toString());
        sysWebLog.setUri(request.getRequestURI());
        sysWebLog.setMethod(request.getMethod());
        sysWebLog.setIp(request.getRemoteAddr());
        sysWebLog.setParameter(getParameter(method, joinPoint.getArgs()));
        sysWebLog.setResult(JSON.toJSONString(result));
        // 这里我们以异步的方式加入数据库，
        // 为的就是最小限度的影响当前操作的相应时间
        sysWebLogService.asyncInsert(sysWebLog);
        return result;
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private String getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StrUtil.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.size() == 0) {
            return null;
        } else if (argList.size() == 1) {
            return JSON.toJSONString(argList.get(0));
        } else {
            return JSON.toJSONString(argList);
        }
    }
}
