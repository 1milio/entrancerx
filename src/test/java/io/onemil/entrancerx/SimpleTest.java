package io.onemil.entrancerx;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.kv.GetResponse;
import com.coreos.jetcd.options.PutOption;
import io.onemil.entrancerx.model.GeoJson;
import io.onemil.entrancerx.util.EtcdUtils;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class SimpleTest {

    static String uuid = "9c697d35-9159-4a49-883e-797679eb851f";

    @Test
    public void putTest() {
        Client client = Client.builder().endpoints("http://192.168.1.108:2379").build();
        KV kvClient = client.getKVClient();
        GeoJson geoJson = new GeoJson(123, 222223);
        try {
            EtcdUtils.put(kvClient, uuid, geoJson.toString()).thenApply(r -> {
                System.out.println("ok is:" + r.hasPrevKv());
                System.out.println("ok k:" + r.getPrevKv().getKey().toStringUtf8());
                System.out.println("ok v:" + r.getPrevKv().getValue().toStringUtf8());
                return r;
            }).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTest() {
        Client client = Client.builder().endpoints("http://192.168.1.108:2379").build();
        KV kvClient = client.getKVClient();
        try {
            GetResponse result =  EtcdUtils.get(kvClient, uuid).get();
            ByteSequence val = result.getKvs().get(0).getValue();
            System.out.println(val.toStringUtf8());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
