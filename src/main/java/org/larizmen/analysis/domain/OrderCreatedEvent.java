package org.larizmen.analysis.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.debezium.outbox.quarkus.ExportedEvent;


import java.time.Instant;

public class OrderCreatedEvent implements ExportedEvent<String, JsonNode> {

    private static ObjectMapper mapper = new ObjectMapper();

    private static final String TYPE = "Order";
    private static final String EVENT_TYPE = "OrderCreated";

    private final String orderId;
    private final JsonNode jsonNode;
    private final Instant timestamp;

    private OrderCreatedEvent(String orderId, JsonNode jsonNode, Instant timestamp) {
        this.orderId = orderId;
        this.jsonNode = jsonNode;
        this.timestamp = timestamp;
    }

    public static OrderCreatedEvent of(final Order order) {

        ObjectNode asJson = mapper.createObjectNode()
                .put("orderId", order.getOrderId())
                .put("timestamp", order.getTimestamp().toString());

        if (order.getRegularLineItems().isPresent()) {
            ArrayNode RegularLineItems = asJson.putArray("RegularLineItems") ;
            for (LineItem lineItem : order.getRegularLineItems().get()) {
                ObjectNode lineAsJon = mapper.createObjectNode()
                        .put("item", lineItem.getItem().toString())
                        .put("name", lineItem.getName());
                RegularLineItems.add(lineAsJon);
            }
        }

        if (order.getVirusLineItems().isPresent()) {
            ArrayNode VirusLineItems = asJson.putArray("VirusLineItems") ;
            for (LineItem lineItem : order.getVirusLineItems().get()) {
                ObjectNode lineAsJon = mapper.createObjectNode()
                        .put("item", lineItem.getItem().toString())
                        .put("name", lineItem.getName());
                VirusLineItems.add(lineAsJon);
            }
        }

        return new OrderCreatedEvent(
                order.getOrderId(),
                asJson,
                order.getTimestamp());
    }

    @Override
    public String getAggregateId() {
        return orderId;
    }

    @Override
    public String getAggregateType() {
        return TYPE;
    }

    @Override
    public JsonNode getPayload() {
        return jsonNode;
    }

    @Override
    public String getType() {
        return EVENT_TYPE;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
