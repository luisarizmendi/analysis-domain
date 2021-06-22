package org.larizmen.analysis.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.Instant;
import java.util.StringJoiner;

@RegisterForReflection
public class OrderTicket {

    String orderId;
    String lineItemId;
    String item;
    String name;
    Instant timestamp;

    public OrderTicket() {
        
    }

    public OrderTicket(String orderId, String lineItemId, String item, String name) {
        this.orderId = orderId;
        this.lineItemId = lineItemId;
        this.item = item;
        this.name = name;
        this.timestamp = Instant.now();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrderTicket.class.getSimpleName() + "[", "]")
                .add("orderId='" + orderId + "'")
                .add("lineItemId='" + lineItemId + "'")
                .add("item=" + item)
                .add("name='" + name + "'")
                .add("timestamp=" + timestamp)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderTicket that = (OrderTicket) o;

        if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
        if (lineItemId != null ? !lineItemId.equals(that.lineItemId) : that.lineItemId != null) return false;
        if (item != that.item) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return timestamp != null ? timestamp.equals(that.timestamp) : that.timestamp == null;
    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (lineItemId != null ? lineItemId.hashCode() : 0);
        result = 31 * result + (item != null ? item.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getLineItemId() {
        return lineItemId;
    }

    public String getItem() {
        return item;
    }

    public String getName() {
        return name;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
