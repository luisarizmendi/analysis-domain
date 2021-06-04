package org.larizmen.analysis.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

/**
 * Custom Jackson deserializer for PlaceOrderCommands
 */
@RegisterForReflection
public class PlaceOrderCommandDeserializer extends ObjectMapperDeserializer<PlaceOrderCommand> {

    public PlaceOrderCommandDeserializer() {
        super(PlaceOrderCommand.class);
    }

/*
    @Override
    public PlaceOrderCommand deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

*/
/*
        @JsonProperty("id") final String id,
        @JsonProperty("patientId") final String patientId,
        @JsonProperty("regularItems") Optional<List<LineItem>> regularLineItems,
        @JsonProperty("virusItems") Optional<List<LineItem>> virusLineItems) {
*//*


        JsonNode node = jp.getCodec().readTree(jp);
        String id = node.get("id").asText();
        OrderSource orderSource =
        String itemName = node.get("itemName").asText();
        int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();

    }
*/
}
