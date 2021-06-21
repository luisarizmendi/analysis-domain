package org.larizmen.analysis.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import java.time.Instant;

@RegisterForReflection
public class PlaceOrderCommand {
    List<OrderLineItem> regularLineItems;
    List<OrderLineItem> virusLineItems;
    String id;
    String patientId;
    Instant timestamp;

    public PlaceOrderCommand() {
    }


    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PlaceOrderCommand(
            @JsonProperty("id") final String id,
            @JsonProperty("patientId") final String patientId,
            @JsonProperty("regularLineItems") Optional<List<OrderLineItem>> regularLineItems,
            @JsonProperty("virusLineItems") Optional<List<OrderLineItem>> virusLineItems) {
      this.id = id;
      this.patientId = patientId;
      if (regularLineItems.isPresent()) {
        this.regularLineItems = regularLineItems.get();
      }else{
        this.regularLineItems = null;
      }
      if (virusLineItems.isPresent()) {
        this.virusLineItems = virusLineItems.get();
      }else{
        this.virusLineItems = null;
      }
      this.timestamp = Instant.now();
    }

/*    public PlaceOrderCommand(String id, String patientId, List<OrderLineItem> regularLineItems, List<OrderLineItem> virusLineItems) {
        this.patientId = patientId;
        this.regularLineItems = regularLineItems;
        this.virusLineItems = virusLineItems;
        this.timestamp = Instant.now();
    }
*/
    public Optional<String> getPatientId() {
        return Optional.ofNullable(patientId);
    }

    public Optional<List<OrderLineItem>> getRegularItems() {
        return Optional.ofNullable(regularLineItems);
    }

    public Optional<List<OrderLineItem>> getVirusItems() {
        return Optional.ofNullable(virusLineItems);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PlaceOrderCommand.class.getSimpleName() + "[", "]")
                .add("regularLineItems=" + regularLineItems)
                .add("virusLineItems=" + virusLineItems)
                .add("id='" + id + "'")
                .add("patientId='" + patientId + "'")
                .add("timestamp=" + timestamp)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceOrderCommand that = (PlaceOrderCommand) o;
        return  Objects.equals(regularLineItems, that.regularLineItems) &&
                Objects.equals(virusLineItems, that.virusLineItems) &&
                Objects.equals(id, that.id) &&
              Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(patientId, that.patientId) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(regularLineItems, virusLineItems, id, patientId);
    }

    public String getId() {
        return id;
    }



    public void setRegularLineItems(List<OrderLineItem> regularLineItems) {
        this.regularLineItems = regularLineItems;
    }

    public void setVirusLineItems(List<OrderLineItem> virusLineItems) {
        this.virusLineItems = virusLineItems;
    }


    public Optional<List<OrderLineItem>> getRegularLineItems() {
        return Optional.ofNullable(regularLineItems);
    }
    
      public Optional<List<OrderLineItem>> getVirusLineItems() {
        return Optional.ofNullable(virusLineItems);
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

   public Instant getTimestamp() {
        return timestamp;
      }

}
