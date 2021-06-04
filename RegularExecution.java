package org.larizmen.analysis.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;


@ApplicationScoped
public class RegularExecution {

    static final Logger logger = LoggerFactory.getLogger(RegularExecution.class);

    private String madeBy;

    @PostConstruct
    void setHostName() {
        try {
            madeBy = InetAddress.getLocalHost().getHostName();
        } catch (IOException e) {
            logger.debug("unable to get hostname");
            madeBy = "unknown";
        }
    }

    public ProcessTicket process(final OrderTicket orderTicket){

        logger.debug("Processing: " + orderTicket.getItem());

            int delay;
            switch (orderTicket.getItem()) {
                case "HEMOGLOBIN":
                    delay = 13;
                    break;
                case "POTASSIUM":
                    delay = 17;
                    break;
                case "SODIUM":
                    delay = 18;
                    break;
                case "CHOLESTEROL":
                    delay = 21;
                    break;
                case "PLATELET":
                    delay = 10;
                    break;
                default:
                    delay = 15;
                    break;
            };
            return execute(orderTicket, delay);
    }

    /*
        Delay for the specified time and then return the completed TicketUp
     */
    private ProcessTicket execute(final OrderTicket orderTicket, int seconds) {
        logger.debug("Waiting: " + seconds + " seconds for " + orderTicket.getItem());
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        return new ProcessTicket(
                orderTicket.getOrderId(),
                orderTicket.getLineItemId(),
                orderTicket.getItem(),
                orderTicket.getName(),
                Instant.now(),
                madeBy);
    }

}
