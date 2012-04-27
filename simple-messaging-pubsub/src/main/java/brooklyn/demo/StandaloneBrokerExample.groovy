package brooklyn.demo

import java.util.List

import brooklyn.entity.basic.AbstractApplication
import brooklyn.entity.messaging.MessageBroker;
import brooklyn.entity.messaging.amqp.AmqpServer
import brooklyn.entity.messaging.qpid.QpidBroker
import brooklyn.location.basic.CommandLineLocations

/** This example starts a Qpid broker, waits for a keypress, then stops it. */
public class StandaloneBrokerExample extends AbstractApplication {

    public static final List<String> DEFAULT_LOCATIONS = [ CommandLineLocations.newLocalhostLocation() ]
    public static final String CONFIG_PATH = "classpath://custom-config.xml"
        
    public static void main(String[] args) {
        StandaloneBrokerExample app = new StandaloneBrokerExample()
        
	    // Configure the Qpid broker entity
	    QpidBroker broker = new QpidBroker(app,
	        amqpVersion:AmqpServer.AMQP_0_10,
	        runtimeFiles:[ QpidBroker.CONFIG_XML: CONFIG_PATH ],
	        queue:testQueue)
	
        log.info("starting broker")
        app.start(DEFAULT_LOCATIONS)
        String url = app.broker.getAttribute(MessageBroker.BROKER_URL)
        log.info("started broker on ${url}")
        log.info("press enter to exit");
        System.in.read();
        log.info("stopping broker");
        app.stop();
    }
}
