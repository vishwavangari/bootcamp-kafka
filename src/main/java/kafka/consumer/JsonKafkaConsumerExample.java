package kafka.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.connect.json.JsonDeserializer;

public class JsonKafkaConsumerExample {

    private final static String TOPIC = "com-vishwa-bootcamp-json-user-vishwa";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private final static String CLIENT_ID_CONFIG = "json-consumer-vishwa";
    private final static String GROUP_ID_CONFIG = "json-cg-vishwa";


    public static Consumer<String, JsonNode> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, CLIENT_ID_CONFIG);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

        // Create the consumer using props.
        final Consumer<String, JsonNode> consumer = new KafkaConsumer<>(props);

        return consumer;
    }

    static void runConsumer() {
        final Consumer<String, JsonNode> consumer = createConsumer();
        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(TOPIC));

        try {

            while (true) {
                final ConsumerRecords<String, JsonNode> consumerRecords =
                        consumer.poll(Duration.ofSeconds(1000));

                if (consumerRecords.isEmpty()) {
                    System.out.println("No Records found for ...." + CLIENT_ID_CONFIG);
                    continue;
                }

                consumerRecords.forEach(record ->
                        System.out.printf("Consumer Record:(%s, %s, %d, %d)\n",
                                record.key(), record.value(), record.partition(), record.offset())
                );
            }
        } catch (Exception exception) {
            System.err.println("Error in Consumer..." + exception);
        } finally {
            consumer.close();
            System.out.println("DONE");
        }
    }

    public static void main(String[] args) {
        runConsumer();
    }
}
