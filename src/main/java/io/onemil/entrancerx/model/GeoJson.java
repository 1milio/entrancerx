package io.onemil.entrancerx.model;

import io.vertx.core.json.JsonObject;

public class GeoJson {
    public long lat;
    public long lon;

    public GeoJson() {
    }

    public GeoJson(long lat, long lon) {
        this.lat=lat;
        this.lon=lon;
    }


    @Override
    public String toString() {
        return toJson().toString();
    }

    public JsonObject toJson() {
        return new JsonObject().put("lat", lat).put("lon", lon);
    }

    public static GeoJson fromJson(GeoJson geoJson, String json) {
        JsonObject jsonObject = new JsonObject(json);
        geoJson.lat = jsonObject.getLong("lat");
        geoJson.lon = jsonObject.getLong("lon");
        return geoJson;
    }
}
