package org.course_planner.utils.rest;

import org.course_planner.utils.exceptions.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class APIPropertyHelper {
    private static final String CONST_API_CONFIG_PARENT = "org.course_planner.api-configs";
    private static final String CONST_API_CONFIG_HOST = "host";
    private static final String CONST_API_EP_CONFIG_PARENT = "endpoint-configs";
    private static final String CONST_API_EP = "endpoint";
    private static final String CONST_TIMEOUT_DEFAULT_PARENT = "timeouts";
    private static final String CONST_CONNECTION_TIMEOUT = "connection-timeout";
    private static final String CONST_READ_TIMEOUT = "read-timeout";
    private static final String CONST_METHOD = "method";
    private static final String CONST_PROPERTY_SEPARATOR = ".";

    @Autowired
    private Environment environment;

    public String getHostBaseUrl(String serviceNameReference) {
        String value = environment.getProperty(joinPropertyParts(CONST_API_CONFIG_PARENT,
                serviceNameReference, CONST_API_CONFIG_HOST), String.class);

        if (value == null) {
            throw new PropertyNotFoundException("getHostBaseUrl: Missing property!", HttpStatus.FAILED_DEPENDENCY);
        }
        return value;
    }

    public String getEndpoint(String hostPropertyReference, String endpointPropertyReference) {
        String value = environment.getProperty(joinPropertyParts(CONST_API_CONFIG_PARENT,
                hostPropertyReference, CONST_API_EP_CONFIG_PARENT, endpointPropertyReference, CONST_API_EP),
                String.class);

        if (value == null) {
            throw new PropertyNotFoundException("getEndpoint: Missing property!", HttpStatus.FAILED_DEPENDENCY);
        }
        return value;
    }

    public Integer getConnectionTimeout(String hostPropertyReference, String endpointPropertyReference) {
        String endpointSpecificProperty = joinPropertyParts(CONST_API_CONFIG_PARENT,
                hostPropertyReference, CONST_API_EP_CONFIG_PARENT, endpointPropertyReference, CONST_CONNECTION_TIMEOUT);
        String defaultProperty = joinPropertyParts(CONST_API_CONFIG_PARENT, CONST_TIMEOUT_DEFAULT_PARENT,
                CONST_CONNECTION_TIMEOUT);

        Integer value = environment.getProperty(endpointSpecificProperty, Integer.class, environment.getProperty(
                defaultProperty, Integer.class, -1));

        if (value == -1) {
            throw new PropertyNotFoundException("getConnectionTimeout: Missing property!", HttpStatus.FAILED_DEPENDENCY);
        }
        return value;
    }

    public Integer getReadTimeout(String hostPropertyReference, String endpointPropertyReference) {
        String endpointSpecificProperty = joinPropertyParts(CONST_API_CONFIG_PARENT,
                hostPropertyReference, CONST_API_EP_CONFIG_PARENT, endpointPropertyReference, CONST_READ_TIMEOUT);
        String defaultProperty = joinPropertyParts(CONST_API_CONFIG_PARENT, CONST_TIMEOUT_DEFAULT_PARENT,
                CONST_READ_TIMEOUT);

        Integer value = environment.getProperty(endpointSpecificProperty, Integer.class, environment.getProperty(
                defaultProperty, Integer.class, -1));

        if (value == -1) {
            throw new PropertyNotFoundException("getReadTimeout: Missing property!", HttpStatus.FAILED_DEPENDENCY);
        }
        return value;
    }

    public HttpMethod getMethod(String hostPropertyReference, String endpointPropertyReference) {
        String value = environment.getProperty(joinPropertyParts(CONST_API_CONFIG_PARENT,
                hostPropertyReference, CONST_API_EP_CONFIG_PARENT, endpointPropertyReference, CONST_METHOD),
                String.class, "");

        if (value.isBlank()) {
            throw new PropertyNotFoundException("getMethod: Missing property!", HttpStatus.FAILED_DEPENDENCY);
        }
        return HttpMethod.valueOf(value);
    }

    private String joinPropertyParts(String ...values) {
        StringBuilder finalProperty = new StringBuilder();
        for (String value : values) {
            finalProperty.append(value).append(CONST_PROPERTY_SEPARATOR);
        }

        return finalProperty.substring(0, finalProperty.length() - 1);
    }
}
