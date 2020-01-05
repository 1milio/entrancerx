package io.onemil.entrancerx.model;


import io.vertx.core.json.JsonObject;

public class LoginRequest {
    public String username;
    public String password;

    public LoginRequest() {
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public JsonObject toJson() {
        return new JsonObject().put("username", username).put("password", password);
    }

    public static LoginRequest fromJson(LoginRequest loginRequest, String json) {
        JsonObject jsonObject = new JsonObject(json);
        loginRequest.password = jsonObject.getString("password");
        loginRequest.username = jsonObject.getString("username");
        return loginRequest;
    }


}
