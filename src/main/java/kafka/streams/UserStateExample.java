package kafka.streams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;

public class UserStateExample {

  public static void main(final String[] args) {
    final String bootstrapServers = args.length > 0 ? args[0] : "localhost:9092";
    final Properties streamsConfiguration = new Properties();
    // Give the Streams application a unique name.  The name must be unique in the Kafka cluster
    // against which the application is run.
    streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "user-region-lambda-example");
    streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, "user-region-lambda-example-client");
    // Where to find Kafka broker(s).
    streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    // Specify default (de)serializers for record keys and for record values.
    streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    // Records should be flushed every 10 seconds. This is less than the default
    // in order to keep this example interactive.
    streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);

    final Serde<String> stringSerde = Serdes.String();
    final Serde<Long> longSerde = Serdes.Long();

    final StreamsBuilder builder = new StreamsBuilder();

    final KTable<String, String> userRegions = builder.table("UserRegions");

    // Aggregate the user counts of by region
    final KTable<String, Long> regionCounts = userRegions
      // Count by region;
      // no need to specify explicit serdes because the resulting key and value types match our default serde settings
      .groupBy((userId, region) -> KeyValue.pair(region, region))
      .count()
      // discard any regions with only 1 user
      .filter((regionName, count) -> count >= 2);

    // Note: The following operations would NOT be needed for the actual users-per-region
    // computation, which would normally stop at the filter() above.  We use the operations
    // below only to "massage" the output data so it is easier to inspect on the console via
    // kafka-console-consumer.
    //
    final KStream<String, Long> regionCountsForConsole = regionCounts
      // get rid of windows (and the underlying KTable) by transforming the KTable to a KStream
      .toStream()
      // sanitize the output by removing null record values (again, we do this only so that the
      // output is easier to read via kafka-console-consumer combined with LongDeserializer
      // because LongDeserializer fails on null values, and even though we could configure
      // kafka-console-consumer to skip messages on error the output still wouldn't look pretty)
      .filter((regionName, count) -> count != null);

    // write to the result topic, we need to override the value serializer to for type long
    regionCountsForConsole.to("LargeRegions", Produced.with(stringSerde, longSerde));

    final KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);
    // Always (and unconditionally) clean local state prior to starting the processing topology.
    // We opt for this unconditional call here because this will make it easier for you to play around with the example
    // when resetting the application for doing a re-run (via the Application Reset Tool,
    // http://docs.confluent.io/current/streams/developer-guide.html#application-reset-tool).
    //
    // The drawback of cleaning up local state prior is that your app must rebuilt its local state from scratch, which
    // will take time and will require reading all the state-relevant data from the Kafka cluster over the network.
    // Thus in a production scenario you typically do not want to clean up always as we do here but rather only when it
    // is truly needed, i.e., only under certain conditions (e.g., the presence of a command line flag for your app).
    // See `ApplicationResetExample.java` for a production-like example.
    streams.cleanUp();
    streams.start();

    // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }

}