package kafka.producer;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;

public class ExactlyOnceProducer {

    private final static String TOPIC = "com-vishwa-exactly-once-producer-2";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private final static String CLIENT_ID_CONFIG = "com-vishwa-exactly-once-test-group125";

    private static Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID_CONFIG);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Exactly Once
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 2);
        props.put(ProducerConfig.ACKS_CONFIG, "all");


        return new KafkaProducer<>(props);
    }

    static void runProducer(final int sendMessageCount) throws Exception {
        final Producer<String, String> producer = createProducer();
        try {
            for (int index = 0; index < sendMessageCount; index++) {
                    final ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, Integer.toString(index), "Hello! this is sample producer message:" + index);
                    producer.send(record);
            }
        } catch (KafkaException exception) {
            System.err.println("Exception...");
        } finally {
            producer.flush();
            producer.close();
        }
    }

    public static void main(String[] args) throws Exception {
        runProducer(100);
    }
}

