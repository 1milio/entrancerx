package io.onemil.entrancerx.rx;

import com.coreos.jetcd.KV;
import io.onemil.entrancerx.model.GeoJson;
import io.onemil.entrancerx.model.LoginRequest;
import io.onemil.entrancerx.model.LoginResponse;
import io.onemil.entrancerx.util.EtcdUtils;
import io.vertx.core.json.JsonObject;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path("/entrancerx")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EntranceRx {

    @Inject
    KV kvClient;


    @POST
    @Path("/login")
    public CompletionStage<LoginResponse> login(JsonObject jsonObject) {
        LoginRequest loginRequest = LoginRequest.fromJson(new LoginRequest(), jsonObject.toString());
        System.out.println("login: " + loginRequest);
        CompletableFuture<LoginResponse> future = new CompletableFuture<>();
        future.complete(new LoginResponse(loginRequest.username, UUID.randomUUID().toString()));
        return future;
    }

    @GET
    @Path("/{uuid}")
    public CompletionStage<JsonObject> lastPosition(@PathParam(value = "uuid") String uuid) {
        System.out.println("lastPosition: " + uuid);
        return EtcdUtils.get(kvClient, uuid)
                .thenApply(r -> {
                    if (r.getKvs() != null
                            && r.getKvs().size() > 0
                            && r.getKvs().get(0) != null
                            && r.getKvs().get(0).getValue() != null) {
                        System.out.println(r.getKvs().get(0).getValue().toStringUtf8());
                        GeoJson geoJson = new GeoJson();
                        GeoJson.fromJson(geoJson, r.getKvs().get(0).getValue().toStringUtf8());
                        return geoJson.toJson();
                    } else {
                        System.out.println("no value");
                        return null;
                    }

                }).exceptionally(
                        e -> {
                            System.out.println(e.getMessage());
                            return new JsonObject().put("error", e.getMessage());
                        });
    }

    @POST
    @Path("/{uuid}")
    public CompletionStage<JsonObject> sendData(@PathParam(value = "uuid") String uuid, JsonObject jsonObject) {
        System.out.println("sendData: " + uuid + ", geoJson: " + jsonObject.toString());
        return EtcdUtils.put(kvClient, uuid, jsonObject.toString())
                .thenApply(r -> {
                    System.out.println("prev result: " + r.getPrevKv().getValue().toStringUtf8());
                    return jsonObject;
                }).thenApply(vai -> {
                    System.out.println("current: " + vai);
                    return jsonObject;
                })
                .exceptionally(
                        e -> {
                            System.out.println(e.getMessage());
                            return new JsonObject().put("error", e.getMessage());
                        });
    }
}
