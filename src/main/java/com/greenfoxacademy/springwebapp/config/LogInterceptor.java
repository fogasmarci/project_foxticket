package com.greenfoxacademy.springwebapp.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class LogInterceptor implements HandlerInterceptor {
  private final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
    Map<String, String[]> params = request.getParameterMap();
    String sb = "Params: " + (params.isEmpty() ? "none" :
        params.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()).replaceAll("[\\[\\]]", ""))
            .collect(Collectors.joining(", ")));

    String logMessage = String.format("%s %s %s Status: %d", request.getMethod(), request.getServletPath(), sb, response.getStatus());
    if (response.getStatus() >= 400) {
      logger.atError().log(logMessage);
    } else {
      logger.info(logMessage);
    }
  }
}
