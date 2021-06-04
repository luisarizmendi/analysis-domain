package org.larizmen.analysis.domain;

import io.debezium.outbox.quarkus.ExportedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Value object returned from an Order.  Contains the Order aggregate and a List ExportedEvent
 */
public class OrderEventResult {

  private Order order;
  private List<ExportedEvent<?, ?>> outboxEvents;
  private List<OrderTicket> regularTickets;
  private List<OrderTicket> virusTickets;
  private List<OrderUpdate> orderUpdates;

  public OrderEventResult() {
  }

  public Order getOrder() {
    return order;
  }

  public void addEvent(final ExportedEvent<?, ?> event) {
    if (this.outboxEvents == null) {
      this.outboxEvents = new ArrayList<>();
    }
    this.outboxEvents.add(event);
  }

  public void addUpdate(final OrderUpdate orderUpdate) {
    if (this.orderUpdates == null) {
      this.orderUpdates = new ArrayList<>();
    }
    this.orderUpdates.add(orderUpdate);
  }

  public void addRegularTicket(final OrderTicket orderTicket) {
    if (this.regularTickets == null) {
      this.regularTickets = new ArrayList<>();
    }
    this.regularTickets.add(orderTicket);
  }

  public void addVirusTicket(final OrderTicket orderTicket) {
    if (this.virusTickets == null) {
      this.virusTickets = new ArrayList<>();
    }
    this.virusTickets.add(orderTicket);
  }

  public Optional<List<OrderTicket>> getRegularTickets() {
    return Optional.ofNullable(this.regularTickets);
  }

  public Optional<List<OrderTicket>> getVirusTickets() {
    return Optional.ofNullable(this.virusTickets);
  }



  @Override
  public String toString() {
    return "OrderEventResult{" +
      "order=" + order +
      ", outboxEvents=" + outboxEvents +
      ", regularTickets=" + regularTickets +
      ", virusTickets=" + virusTickets +
      ", orderUpdates=" + orderUpdates +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof OrderEventResult)) return false;

    OrderEventResult that = (OrderEventResult) o;

    if (getOrder() != null ? !getOrder().equals(that.getOrder()) : that.getOrder() != null) return false;
    if (outboxEvents != null ? !outboxEvents.equals(that.outboxEvents) : that.outboxEvents != null) return false;
    if (regularTickets != null ? !regularTickets.equals(that.regularTickets) : that.regularTickets != null)
      return false;
    if (virusTickets != null ? !virusTickets.equals(that.virusTickets) : that.virusTickets != null)
      return false;
    return orderUpdates != null ? orderUpdates.equals(that.orderUpdates) : that.orderUpdates == null;
  }

  @Override
  public int hashCode() {
    int result = getOrder() != null ? getOrder().hashCode() : 0;
    result = 31 * result + (outboxEvents != null ? outboxEvents.hashCode() : 0);
    result = 31 * result + (regularTickets != null ? regularTickets.hashCode() : 0);
    result = 31 * result + (virusTickets != null ? virusTickets.hashCode() : 0);
    result = 31 * result + (orderUpdates != null ? orderUpdates.hashCode() : 0);
    return result;
  }

  public List<ExportedEvent<?, ?>> getOutboxEvents() {
    return outboxEvents;
  }

  public void setOutboxEvents(List<ExportedEvent<?, ?>> outboxEvents) {
    this.outboxEvents = outboxEvents;
  }

  public void setRegularTickets(List<OrderTicket> regularTickets) {
    this.regularTickets = regularTickets;
  }

  public void setVirusTickets(List<OrderTicket> virusTickets) {
    this.virusTickets = virusTickets;
  }

  public List<OrderUpdate> getOrderUpdates() {
    return orderUpdates;
  }

  public void setOrderUpdates(List<OrderUpdate> orderUpdates) {
    this.orderUpdates = orderUpdates;
  }

  public void setOrder(final Order order) {
    this.order = order;
  }
}
