package de.simpleprojectmanager.simpleprojectmanager.api.response;

import de.simpleprojectmanager.simpleprojectmanager.api.response.IDefaultResponse;

public class ErrorResponse implements IDefaultResponse {

    //Error-code
    private String error;

    public ErrorResponse(String error) {
        this.error=error;
    }

    public String getError() {
        return this.error;
    }
}
