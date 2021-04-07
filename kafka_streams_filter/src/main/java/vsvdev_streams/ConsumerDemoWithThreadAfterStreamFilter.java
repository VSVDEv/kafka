package vsvdev_streams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class ConsumerDemoWithThreadAfterStreamFilter {
    public static void main(String[] args) {
new ConsumerDemoWithThreadAfterStreamFilter().run();

    }

private ConsumerDemoWithThreadAfterStreamFilter(){

}

private void run(){
    Logger logger = LoggerFactory.getLogger(ConsumerDemoWithThreadAfterStreamFilter.class);
    String bootstrapServer = "127.0.0.1:9092";
    String groupId = "thirdapp";
    String topic = "important_tweets";
    CountDownLatch latch = new CountDownLatch(1);
    logger.info("Creating consumer");
    Runnable myConsumer =  new ConsumerRunnable(bootstrapServer,groupId,topic,latch);
Thread myThread = new Thread(myConsumer);
myThread.start();

// add shot down hook
Runtime.getRuntime().addShutdownHook(new Thread(()->{
    logger.info("Caught shutdown hook");
    ((ConsumerRunnable) myConsumer).shutdown();

    try {
        latch.await();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    logger.info("Application has exited");
}));
    try {
        latch.await();
    } catch (InterruptedException e) {
       logger.error("Application interrupted "+ e);
    }finally {
        logger.info("Application is closing");
    }

}


    public class ConsumerRunnable implements Runnable {
        private CountDownLatch latch;
        private KafkaConsumer<String, String> consumer;
       private Logger logger = LoggerFactory.getLogger(ConsumerRunnable.class);
        public ConsumerRunnable(String bootstrapServer,String groupId,String topic, CountDownLatch latch) {
            this.latch = latch;
            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId );
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest" );/// earliest/latest/none
             consumer = new KafkaConsumer<String, String>(properties);

            consumer.subscribe(Arrays.asList(topic));
        }

        @Override
        public void run() {
          try{
            while (true){
                ConsumerRecords<String, String> records= consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord record : records){
                    logger.info("Key: "+ record.key() + " , Value: "+ record.value());
                    logger.info("Partition: "+ record.partition() + " , Offset: "+ record.offset());
                }
            }}catch (WakeupException e){
              logger.info("Recieve shutdown signal");
          }finally {
              consumer.close();
              //tell our code we done with code
              latch.countDown();
          }
        }
        public void shutdown(){
            // interrupt consumer.poll() throw WakeUpException
            consumer.wakeup();
        }
    }
}
