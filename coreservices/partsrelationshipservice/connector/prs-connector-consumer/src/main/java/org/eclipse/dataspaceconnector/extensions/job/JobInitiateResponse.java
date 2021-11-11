package org.eclipse.dataspaceconnector.extensions.job;

import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;

public class JobInitiateResponse {
    private String id;
    private String error;
    private ResponseStatus status = ResponseStatus.OK;

    private JobInitiateResponse() {
    }

    public String getId() {
        return id;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public static class Builder {
        private final JobInitiateResponse response;

        private Builder() {
            response = new JobInitiateResponse();
        }

        public static JobInitiateResponse.Builder newInstance() {
            return new JobInitiateResponse.Builder();
        }

        public JobInitiateResponse.Builder id(String id) {
            response.id = id;
            return this;
        }

        public JobInitiateResponse.Builder status(ResponseStatus status) {
            response.status = status;
            return this;
        }

        public JobInitiateResponse.Builder error(String error) {
            response.error = error;
            return this;
        }

        public JobInitiateResponse build() {
            return response;
        }
    }
}
