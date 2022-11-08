package com.musicgacha.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;

public final class HttpUtils {

    private static final String[] IP_HEADERS = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR"

        // you can add more matching headers here ...
    };

    private HttpUtils() {
        // nothing here ...
    }

    public static ResponseEntity<String> getRequestIP(HttpServletRequest request) {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return ResponseEntity.ok("0.0.0.0");
        }

        for (String header: IP_HEADERS) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                String ip = ipList.split(",")[0];
                return ResponseEntity.ok(ip);
            }
        }
        return ResponseEntity.ok(request.getRemoteAddr());
    }
        
}
