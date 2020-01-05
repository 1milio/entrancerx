package io.onemil.entrancerx.util;

import com.coreos.jetcd.KV;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.kv.GetResponse;
import com.coreos.jetcd.kv.PutResponse;
import com.coreos.jetcd.options.PutOption;

import java.util.concurrent.CompletableFuture;

public class EtcdUtils {

    public static CompletableFuture<GetResponse> get(KV kvClient, String key) {
        ByteSequence k = ByteSequence.fromString(key);
        return kvClient.get(k);
    }

    public static CompletableFuture<PutResponse> put(KV kvClient, String key, String value) {
        ByteSequence k = ByteSequence.fromString(key);
        ByteSequence v = ByteSequence.fromString(value);
        return kvClient.put(k, v, PutOption.newBuilder().withPrevKV().build());
    }
}
