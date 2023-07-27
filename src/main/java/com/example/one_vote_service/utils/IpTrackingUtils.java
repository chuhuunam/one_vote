package com.example.one_vote_service.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class IpTrackingUtils {
    public static String getRemoteIp(HttpServletRequest httpServletRequest) {
        String remoteIp = httpServletRequest.getHeader("X-FORWARDED-FOR");
        if (StringUtils.isBlank(remoteIp)) {
            return httpServletRequest.getRemoteAddr();
        }
        return remoteIp;
    }
}
