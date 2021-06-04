package org.larizmen.analysis.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RegisterForReflection
public class DashboardUpdate {


    Logger logger = LoggerFactory.getLogger(DashboardUpdate.class);

    public final String orderId;
    public final String itemId;
    public final String name;
    public final String item;
    public final String status;
    public final String madeBy;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DashboardUpdate(
            @JsonProperty("orderId") final String orderId,
            @JsonProperty("itemId") String itemId,
            @JsonProperty("name") String name,
            @JsonProperty("item") String item,
            @JsonProperty("status") String status,
            @JsonProperty("madeBy") Optional<String> madeBy) {


        this.orderId = orderId;
        this.itemId = itemId;
        this.name = name;
        this.item = item;
        this.status = status;
        if (madeBy.isPresent()) {
            this.madeBy = madeBy.get();
        }else{
            this.madeBy = "";
        }

        logger.debug("Dashboard update: {}", this);
    }
}
