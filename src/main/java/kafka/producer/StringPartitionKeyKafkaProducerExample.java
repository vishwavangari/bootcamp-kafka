package kafka.producer;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class StringPartitionKeyKafkaProducerExample {

    private final static String TOPIC = "store-partitions";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private final static String CLIENT_ID_CONFIG = "string-producer-vishwa-store";

    private static Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID_CONFIG);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    static void runProducer(final int sendMessageCount) throws Exception {
        final Producer<String, String> producer = createProducer();
        List<String> partionKeyList = Lists.newArrayList("STORE1", "STORE2", "STORE3", "STORE4",  "STORE5", "STORE6");
        try {
            for (int index = 0; index < sendMessageCount; index++) {
                final ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, partionKeyList.get(new Random().nextInt(partionKeyList.size())), "Hello! this is sample producer message:" + index);

                RecordMetadata metadata = producer.send(record).get();

                System.out.printf("sent record(key=%s value=%s) " + "meta(partition=%d, offset=%d)\n",
                        record.key(), record.value(), metadata.partition(), metadata.offset());
            }
        } finally {
            producer.flush();
            producer.close();
        }
    }

    public static void main(String[] args) throws Exception {
        runProducer(1000);
    }

}
