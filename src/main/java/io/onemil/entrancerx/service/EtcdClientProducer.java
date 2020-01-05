package io.onemil.entrancerx.service;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;

import javax.enterprise.inject.Produces;

public class EtcdClientProducer {

    Client client;

    KV kvClient;

    @Produces
    public Client client() {
        if (client == null)
            client = Client.builder().endpoints("http://localhost:2379").build();
        return client;
    }

    @Produces
    public KV kvClient() {
        if (kvClient == null)
            kvClient = client().getKVClient();
        return kvClient;

    }
}
