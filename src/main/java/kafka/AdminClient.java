package kafka;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;


public class AdminClient {

    private static final String TOPIC = "com-vishwa-bootcamp-string-user-vishwa";
    private static final int PARTITION = 1;
    private static final short REPLICATION = 1;
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static void main(String[] args) {
        // Create admin client
            try {
                // Define topic
                final NewTopic newTopic = new NewTopic(TOPIC, PARTITION, REPLICATION);

                org.apache.kafka.clients.admin.AdminClient kafkaAdminClient = org.apache.kafka.clients.admin.AdminClient.create(buildDefaultClientConfig());
                // Create topic, which is async call.
                final CreateTopicsResult createTopicsResult = kafkaAdminClient.createTopics(Collections.singleton(newTopic));

                // Since the call is Async, Lets wait for it to complete.
                createTopicsResult.values().get(TOPIC).get();
                System.out.println("TOPIC CREATED SUCCESSFULLY");
            } catch (InterruptedException | ExecutionException e) {
                if (!(e.getCause() instanceof TopicExistsException)) {
                    throw new RuntimeException(e.getMessage(), e);
                } else if ((e.getCause() instanceof TopicExistsException)) {
                    System.out.println("TOPIC Already Exists");
                }
                // TopicExistsException - Swallow this exception, just means the topic already exists.
            }
    }

    /**
     * Internal helper method to build a default configuration.
     */
    private static Map<String, Object> buildDefaultClientConfig() {
        Map<String, Object> defaultClientConfig = Maps.newHashMap();
        defaultClientConfig.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        //defaultClientConfig.put("client.id", "bbootcamp-consumer-id");
        return defaultClientConfig;
    }
}
