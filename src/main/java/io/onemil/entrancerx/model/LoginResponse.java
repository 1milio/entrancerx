package io.onemil.entrancerx.model;

import io.vertx.core.json.JsonObject;

public class LoginResponse {
    public String uuid;
    public String token;

    public LoginResponse() {
    }

    public LoginResponse(String uuid, String token) {
        this.uuid = uuid;
        this.token = token;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public JsonObject toJson() {
        return new JsonObject().put("uuid", uuid).put("token", token);
    }

    public static LoginResponse fromJson(LoginResponse loginResponse, String json) {
        JsonObject jsonObject = new JsonObject(json);
        loginResponse.uuid = jsonObject.getString("uuid");
        loginResponse.token = jsonObject.getString("token");
        return loginResponse;
    }
}
