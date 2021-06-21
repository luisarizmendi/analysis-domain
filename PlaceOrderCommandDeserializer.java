package org.larizmen.analysis.domain;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;


public class PlaceOrderCommandDeserializer extends ObjectMapperDeserializer<PlaceOrderCommand> {
    public PlaceOrderCommandDeserializer() {
        super(PlaceOrderCommand.class);
    }


}
