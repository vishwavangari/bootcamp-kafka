package kafka.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class StringManualCommitKafkaConsumerExample {

    private final static String TOPIC = "partition-topic-vishwa";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private final static String CLIENT_ID_CONFIG = "string-consumer-vishwa-manual-exception-again1";
    private final static String GROUP_ID_CONFIG = "string-cg-vishwa-manual-sleep-exception-again1";

    static int count = 0;

    public static Consumer<String, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, CLIENT_ID_CONFIG);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

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
                try {
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

                    count++;
                    process();
                    consumer.commitSync();
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        } catch (Exception exception) {
            System.err.println("Error in Consumer..." + exception);
        } finally {
            consumer.close();
            System.out.println("DONE");
        }
    }

    private static void process() {
        try {
            if (count == 2) {
                throw new RuntimeException();
            }
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Processing.....");
    }

    public static void main(String[] args) {
        runConsumer();
    }
}