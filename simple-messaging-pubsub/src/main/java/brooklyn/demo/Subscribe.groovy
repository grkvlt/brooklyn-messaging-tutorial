package brooklyn.demo

import javax.jms.Connection
import javax.jms.MessageConsumer
import javax.jms.Session
import javax.jms.TextMessage

import org.apache.qpid.client.AMQConnectionFactory
import org.apache.qpid.configuration.ClientProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.google.common.base.Preconditions


/** Recieves messages from a queue on a Qpid broker at a given URL. */
public class Subscribes {
    public static final Logger LOG = LoggerFactory.getLogger(Publish)
        
    public static void main(String...argv) {
        Preconditions.checkElementIndex(0, argv.length, "Must specify broker URL")
        String url = argv[0]
        
        System.setProperty(ClientProperties.AMQP_VERSION, "0-10")
        System.setProperty(ClientProperties.DEST_SYNTAX, "ADDR")
        AMQConnectionFactory factory = new AMQConnectionFactory(url)
        Connection connection = factory.createConnection();
        connection.start()
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
        Queue destination = session.createQueue("testQueue")
        MessageConsumer messageConsumer = session.createConsumer(destination)

        int n = 0
        while (true) {
            TextMessage msg = messageConsumer.receive(1000L)
            if (msg == null) break
            LOG.info("got message ${++n} ${msg.text}")
        }

        session.close()
        connection.close()
    }
}
