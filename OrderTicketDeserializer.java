package org.larizmen.analysis.domain;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class OrderTicketDeserializer extends ObjectMapperDeserializer<OrderTicket> {

    public OrderTicketDeserializer() {
        super(OrderTicket.class);
    }
}
