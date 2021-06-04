package org.larizmen.analysis.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

@Entity
@Table(name = "Orders")
public class Order extends PanacheEntityBase {

  @Transient
  static Logger logger = LoggerFactory.getLogger(Order.class);

  @Id
  @Column(nullable = false, unique = true, name = "order_id")
 
  private String orderId;
  private String patientId;
  private Instant timestamp;
  private String orderStatus;


  @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = CascadeType.ALL)
  private List<LineItem> regularLineItems;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = CascadeType.ALL)
  private List<LineItem> virusLineItems;

  /**
   * Updates the lineItem corresponding to the ticket, creates the appropriate domain events,
   * creates value objects to notify the system, checks the order to see if all items are completed,
   * and updates the order if necessary
   *
   * All corresponding objects are returned in an OrderEventResult
   *
   * @param processTicket
   * @return OrderEventResult
   */
  public OrderEventResult applyProcessTicket(final ProcessTicket processTicket) {

    // set the LineItem's new status
    if (this.getRegularLineItems().isPresent()) {
      this.getRegularLineItems().get().stream().forEach(lineItem -> {
        if(lineItem.getItemId().equals(lineItem.getItemId())){
          lineItem.setLineItemStatus("FULFILLED");
        }
      });
    }
    if (this.getVirusLineItems().isPresent()) {
      this.getVirusLineItems().get().stream().forEach(lineItem -> {
        if(lineItem.getItemId().equals(lineItem.getItemId())){
          lineItem.setLineItemStatus("FULFILLED");
        }
      });
    }

    // if there are both regular and virus items concatenate them before checking status
    if (this.getRegularLineItems().isPresent() && this.getVirusLineItems().isPresent()) {
      // check the status of the Order itself and update if necessary
      if(Stream.concat(this.regularLineItems.stream(), this.virusLineItems.stream())
              .allMatch(lineItem -> {
                return lineItem.getLineItemStatus().equals("FULFILLED");
              })){
        this.setOrderStatus("FULFILLED");
      };
    } else if (this.getRegularLineItems().isPresent()) {
      if(this.regularLineItems.stream()
              .allMatch(lineItem -> {
                return lineItem.getLineItemStatus().equals("FULFILLED");
              })){
        this.setOrderStatus("FULFILLED");
      };
    }else if (this.getVirusLineItems().isPresent()) {
      if(this.virusLineItems.stream()
              .allMatch(lineItem -> {
                return lineItem.getLineItemStatus().equals("FULFILLED");
              })){
        this.setOrderStatus("FULFILLED");
      };
    }

    // create the domain event
    OrderUpdatedEvent orderUpdatedEvent = OrderUpdatedEvent.of(this);

    // create the update value object
    OrderUpdate orderUpdate = new OrderUpdate(processTicket.getOrderId(), processTicket.getLineItemId(), processTicket.getName(), processTicket.getItem(), "FULFILLED", processTicket.madeBy);

    OrderEventResult orderEventResult = new OrderEventResult();
    orderEventResult.setOrder(this);
    orderEventResult.addEvent(orderUpdatedEvent);
    orderEventResult.setOrderUpdates(new ArrayList<>() {{
      add(orderUpdate);
    }});
    return orderEventResult;
  }

  /**
   * Creates and returns a new OrderEventResult containing the Order aggregate built from the PlaceOrderCommand
   * and an OrderCreatedEvent
   *
   * @param placeOrderCommand PlaceOrderCommand
   * @return OrderEventResult
   */





  public static OrderEventResult process(final PlaceOrderCommand placeOrderCommand) {

    logger.debug("Processing: ", placeOrderCommand.toString() );

    // create the return value
    OrderEventResult orderEventResult = new OrderEventResult();

    // build the order from the PlaceOrderCommand
    Order order = new Order(placeOrderCommand.getId());
    order.setTimestamp(placeOrderCommand.getTimestamp());
    order.setOrderStatus("IN_PROGRESS");

    if (placeOrderCommand.getRegularLineItems().isPresent()) {
      logger.debug("createOrderFromCommand adding regular markers {}", placeOrderCommand.getRegularLineItems().get().size());

      logger.debug("adding Regular LineItems");
      placeOrderCommand.getRegularLineItems().get().forEach(commandItem -> {
        logger.debug("createOrderFromCommand adding regularItem from {}", commandItem.toString());
        LineItem lineItem = new LineItem(commandItem.getItem(), commandItem.getName(), "IN_PROGRESS", order);
        order.addRegularLineItem(lineItem);
        logger.debug("added LineItem: {}", order.getRegularLineItems().get().size());
        orderEventResult.addRegularTicket(new OrderTicket(order.getOrderId(), lineItem.getItemId(), lineItem.getItem(), lineItem.getName()));
        logger.debug("Added Regular Ticket to OrderEventResult: {}", orderEventResult.getRegularTickets().get().size());
        orderEventResult.addUpdate(new OrderUpdate(order.getOrderId(), lineItem.getItemId(), lineItem.getName(), lineItem.getItem(), "IN_PROGRESS"));
        logger.debug("Added Order Update to OrderEventResult: ", orderEventResult.getOrderUpdates().size());
      });
    }
    logger.debug("adding Virus LineItems");
    if (placeOrderCommand.getVirusLineItems().isPresent()) {
      logger.debug("createOrderFromCommand adding virusOrders {}", placeOrderCommand.getVirusLineItems().get().size());
      placeOrderCommand.getVirusLineItems().get().forEach(commandItem -> {
        logger.debug("createOrderFromCommand adding virusItem from {}", commandItem.toString());
        LineItem lineItem = new LineItem(commandItem.getItem(), commandItem.getName(), "IN_PROGRESS", order);
        order.addVirusLineItem(lineItem);
        orderEventResult.addVirusTicket(new OrderTicket(order.getOrderId(), lineItem.getItemId(), lineItem.getItem(), lineItem.getName()));
        orderEventResult.addUpdate(new OrderUpdate(order.getOrderId(), lineItem.getItemId(), lineItem.getName(), lineItem.getItem(), "IN_PROGRESS"));
      });
    }

    orderEventResult.setOrder(order);
    orderEventResult.addEvent(OrderCreatedEvent.of(order));
    logger.debug("Added Order and OrderCreatedEvent to OrderEventResult: {}", orderEventResult);


    order.setPatientId(placeOrderCommand.getPatientId().get());

    logger.debug("returning {}", orderEventResult);
    return orderEventResult;
  }

  /**
   * Convenience method to prevent Null Pointer Exceptions
   *
   * @param lineItem
   */
  public void addRegularLineItem(LineItem lineItem) {
    if (this.regularLineItems == null) {
      this.regularLineItems = new ArrayList<>();
    }
    lineItem.setOrder(this);
    this.regularLineItems.add(lineItem);
  }

  /**
   * Convenience method to prevent Null Pointer Exceptions
   *
   * @param lineItem
   */
  public void addVirusLineItem(LineItem lineItem) {
    if (this.virusLineItems == null) {
      this.virusLineItems = new ArrayList<>();
    }
    lineItem.setOrder(this);
    this.virusLineItems.add(lineItem);
  }

  public Optional<List<LineItem>> getRegularLineItems() {
    return Optional.ofNullable(regularLineItems);
  }

  public void setRegularItems(List<LineItem> regularLineItems) {
    this.regularLineItems = regularLineItems;
  }

  public Optional<List<LineItem>> getVirusLineItems() {
    return Optional.ofNullable(virusLineItems);
  }

  public void setVirusItems(List<LineItem> virusLineItems) {
    this.virusLineItems = virusLineItems;
  }

  public Optional<String> getPatientId() {
    return Optional.ofNullable(this.patientId);
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public Order() {
    this.orderId = UUID.randomUUID().toString();
    this.timestamp = Instant.now();
  }

  public Order(String orderId){
    this.orderId = orderId;
    this.timestamp = Instant.now();
  }

  public Order(String orderId, String orderSource, String location, String patientId, Instant timestamp, String orderStatus, List<LineItem> regularLineItems, List<LineItem> virusLineItems) {
    this.orderId = UUID.randomUUID().toString();
    this.patientId = patientId;
    this.timestamp = timestamp;
    this.orderStatus = orderStatus;
    this.regularLineItems = regularLineItems;
    this.virusLineItems = virusLineItems;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Order.class.getSimpleName() + "[", "]")
            .add("orderId='" + orderId + "'")
            .add("patientId='" + patientId + "'")
            .add("timestamp=" + timestamp)
            .add("orderStatus=" + orderStatus)
            .add("regularLineItems=" + regularLineItems)
            .add("virusLineItems=" + virusLineItems)
            .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Order order = (Order) o;

    if (orderId != null ? !orderId.equals(order.orderId) : order.orderId != null) return false;
    if (patientId != null ? !patientId.equals(order.patientId) : order.patientId != null)
      return false;
    if (timestamp != null ? !timestamp.equals(order.timestamp) : order.timestamp != null) return false;
    if (orderStatus != order.orderStatus) return false;
    if (regularLineItems != null ? !regularLineItems.equals(order.regularLineItems) : order.regularLineItems != null)
      return false;
    return virusLineItems != null ? virusLineItems.equals(order.virusLineItems) : order.virusLineItems == null;
  }

  @Override
  public int hashCode() {
    int result = orderId != null ? orderId.hashCode() : 0;
    result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
    result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
    result = 31 * result + (orderStatus != null ? orderStatus.hashCode() : 0);
    result = 31 * result + (regularLineItems != null ? regularLineItems.hashCode() : 0);
    result = 31 * result + (virusLineItems != null ? virusLineItems.hashCode() : 0);
    return result;
  }

  public String getOrderId() {
    return orderId;
  }


  public String getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }
}
