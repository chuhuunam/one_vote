package com.example.one_vote_service.config.filter;

import com.example.one_vote_service.common.ServiceAttributes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.message.internal.ReaderWriter;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Provider
public class ClientLoggingFilter extends LoggingFeature implements ClientRequestFilter, ClientResponseFilter {
    private final Log log = LogFactory.getLog(this.getClass());
    private long begin;
    private String requestId;

    private final ObjectMapper objectMapper;

    public ClientLoggingFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void filter(ClientRequestContext clientRequestContext) throws IOException {
        begin = System.currentTimeMillis();
        requestId = clientRequestContext.getHeaderString(ServiceAttributes.REQUEST_ID);
        if (StringUtils.isEmpty(requestId)) {
            requestId = UUID.randomUUID().toString();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("RequestId: ").append(requestId);
        sb.append(" - Host: ").append(clientRequestContext.getUri().getHost());
        sb.append(" - URL: ").append(clientRequestContext.getUri());
        sb.append(" - Method: ").append(clientRequestContext.getMethod());
        sb.append(" - Header: ").append(clientRequestContext.getHeaders());
        sb.append(" - QueryString: ").append(clientRequestContext.getUri().getQuery());
        sb.append(" - Entity: ").append(objectMapper.writeValueAsString(clientRequestContext.getEntity()));
        log.info("HTTP CLIENT REQUEST ==> " + sb.toString());
    }

    @Override
    public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) throws IOException {
        if (clientResponseContext == null) {
            log.info("HTTP CLIENT RESPONSE ==> RESPONSE CONTEXT IS NULL !!!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("RequestId: ").append(requestId);
        sb.append(" - Header: ").append(clientResponseContext.getHeaders());
        sb.append(" - Status: ").append(clientResponseContext.getStatus());
        sb.append(" - Entity: ").append(readResponseEntity(clientResponseContext));
        sb.append(" - In: ").append(System.currentTimeMillis() - begin).append(" ms");
        log.info("HTTP CLIENT RESPONSE ==> " + sb);
    }

    private String readResponseEntity(ClientResponseContext clientResponseContext) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = clientResponseContext.getEntityStream();

        final StringBuilder b = new StringBuilder();
        try {
            ReaderWriter.writeTo(in, out);

            byte[] requestEntity = out.toByteArray();
            if (requestEntity.length == 0) {
                b.append("\n");
            } else {
                b.append(new String(requestEntity)).append("\n");
            }
            clientResponseContext.setEntityStream(new ByteArrayInputStream(requestEntity));

        } catch (IOException ex) {
            log.error("", ex);
            return "Logging entity FAIL !";
        }
        return b.toString();
    }
}
