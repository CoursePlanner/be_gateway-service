package org.course_planner.utils.rest;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class CustomRESTClientBuilder {
    private final APIPropertyHelper apiPropertyHelper;

    public CustomRESTClientBuilder(APIPropertyHelper apiPropertyHelper) {
        this.apiPropertyHelper = apiPropertyHelper;
    }

    public OkHttpClient getOkHttpClient(final String serviceNameReference, final String endpointReference,
                                        Interceptor... interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(apiPropertyHelper.getConnectionTimeout(serviceNameReference, endpointReference), TimeUnit.SECONDS)
                .readTimeout(apiPropertyHelper.getReadTimeout(serviceNameReference, endpointReference), TimeUnit.SECONDS);

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        return builder.build();
    }
}
