package io.onemil.entrancerx.routes;

import com.coreos.jetcd.KV;
import io.onemil.entrancerx.model.GeoJson;
import io.onemil.entrancerx.model.LoginResponse;
import io.onemil.entrancerx.util.EtcdUtils;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.util.UUID;


@RouteBase(path = "entrancers", produces = MediaType.APPLICATION_JSON)
public class EntranceRs {

    @Inject
    KV kvClient;

    @Route(path = "/login", methods = HttpMethod.POST)
    void login(RoutingContext rc) {
        JsonObject jsonObject = rc.getBodyAsJson();
        rc.response().end(
                new LoginResponse(jsonObject.getString("username"), UUID.randomUUID().toString()).toString()
        );
    }

    @Route(path = "/:uuid", methods = HttpMethod.GET)
    public void lastPosition(RoutingContext rc) {
        String uuid = rc.pathParam("uuid");
        EtcdUtils.get(kvClient, uuid).whenComplete(
                (result, exception) -> {
                    if (result != null) {
                        GeoJson geoJson = new GeoJson();
                        GeoJson.fromJson(geoJson, result.getKvs().get(0).getValue().toStringUtf8());
                        rc.response().end(geoJson.toString());
                    } else {
                        rc.response().end(new JsonObject().put("error", exception.getMessage()).toString());
                    }
                }
        );
    }

    @Route(path = "/:uuid", methods = HttpMethod.POST)
    public void sendData(RoutingContext rc) {
        String uuid = rc.pathParam("uuid");
        JsonObject jsonObject = rc.getBodyAsJson();
        EtcdUtils.put(kvClient, uuid, jsonObject.toString()).whenComplete(
                (result, exception) -> {
                    if (result != null) {
                        rc.response().end(jsonObject.toString());
                    } else {
                        rc.response().end(new JsonObject().put("error", exception.getMessage()).toString());
                    }
                });
    }

}
