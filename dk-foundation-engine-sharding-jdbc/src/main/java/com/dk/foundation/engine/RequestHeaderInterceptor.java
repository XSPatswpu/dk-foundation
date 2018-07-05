package com.dk.foundation.engine;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.tomcat.util.collections.CaseInsensitiveKeyMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by duguk on 2018/1/5.
 */
public class RequestHeaderInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {

            Map<String, Collection<String>> resolvedHeaders = new CaseInsensitiveKeyMap<>();
            resolvedHeaders.putAll(template.headers());

            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                if(!resolvedHeaders.containsKey(name)){
                    String values = request.getHeader(name);
                    List<String> headers = new ArrayList<String>();
                    headers.addAll(Arrays.asList(values));
                    resolvedHeaders.put(name, headers);
                }
            }
            template.headers(null);
            template.headers(resolvedHeaders);
        }
    }
}
