package org.larizmen.analysis.domain;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

/**
 * Jackson deserializer for TicketUp value object
 */
public class ProcessTicketDeserializer extends ObjectMapperDeserializer<ProcessTicket> {

    public ProcessTicketDeserializer() {
        super(ProcessTicket.class);
    }
}
