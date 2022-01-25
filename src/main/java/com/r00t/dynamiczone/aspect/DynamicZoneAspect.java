package com.r00t.dynamiczone.aspect;

import com.r00t.dynamiczone.util.ReflectionMethodUtils;
import com.r00t.dynamiczone.util.ZoneUtils;
import com.r00t.dynamiczone.util.Zoned;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.ZoneId;
import java.util.Objects;

@Aspect
@Component
public class DynamicZoneAspect {

    @Around("@annotation(com.r00t.dynamiczone.util.TranslateZonedVariables)")
    public Object handleTimeZone(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        ReflectionMethodUtils.doWithMethodParameters(
                method, joinPoint.getArgs(),
                (parameter, o) -> ZoneUtils.convertFields(o, getTimeZone()),
                parameter -> parameter.getDeclaredAnnotation(RequestBody.class) != null
        );

        ReflectionMethodUtils.doWithMethodParametersIndexes(
                method, joinPoint.getArgs(),
                (index, o) -> joinPoint.getArgs()[index] = ZoneUtils.convertParameter(o, getTimeZone()),
                parameter -> parameter.getDeclaredAnnotation(Zoned.class) != null
        );

        Object response = joinPoint.proceed();
        ZoneUtils.convertFields(response, getTimeZone());
        return response;
    }

    private ZoneId getTimeZone() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())
        ).getRequest();

        if (request.getHeader("X-NM-CLIENT-TIMEZONE") == null)
            return ZoneId.systemDefault();
        else
            return ZoneId.of(request.getHeader("X-NM-CLIENT-TIMEZONE"));
    }
}
