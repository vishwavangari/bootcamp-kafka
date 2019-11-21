package kafka.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class TransactionalConsumer {

    private final static String TOPIC = "transactional-topic1";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private final static String CLIENT_ID_CONFIG = "trans-string-consumer-vishwa-new";
    private final static String GROUP_ID_CONFIG = "trans-string-cg-vishwa-new";

    public static Consumer<String, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, CLIENT_ID_CONFIG);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Transaction Properties
        props.put("enable.auto.commit", "false");
        props.put("isolation.level", "read_uncommitted");

        // Create the consumer using props.
        final Consumer<String, String> consumer = new KafkaConsumer<>(props);

        return consumer;
    }

    static void runConsumer() {
        final Consumer<String, String> consumer = createConsumer();
        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(TOPIC));

        try {

            while (true) {
                final ConsumerRecords<String, String> consumerRecords =
                        consumer.poll(Duration.ofSeconds(1000));

                if (consumerRecords.isEmpty()) {
                    System.out.println("No Records found for ...." + CLIENT_ID_CONFIG);
                    continue;
                }

                consumerRecords.forEach(record ->
                        System.out.printf("Consumer Record:(%s, %s, %d, %d)\n",
                                record.key(), record.value(), record.partition(), record.offset())
                );

                consumer.commitSync();
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
