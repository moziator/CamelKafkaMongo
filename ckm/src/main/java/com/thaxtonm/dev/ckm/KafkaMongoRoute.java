package com.thaxtonm.dev.ckm;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaMongoRoute extends RouteBuilder{
    
    private static final Logger LOG = LoggerFactory.getLogger(KafkaMongoRoute.class);

    @Override
    public void configure() throws Exception {
        from("kafka:{{consumer.topic}}"
            + "?brokers={{consumer.bootstrap.url}}"
            + "&maxPollRecords={{consumer.maxPollRecords}}"
            + "&consumersCount={{consumer.consumersCount}}"
            + "&seekTo={{consumer.seekTo}}"
            + "&groupId={{consumer.groupId}}"
            + "&autoOffsetReset={{consumer.autoOffsetReset}}"
            + "&clientId={{consumer.clientId}}")
            .routeId("FromKafka")
            .log("Message received from Kafka : ${body}")
            .log("  on the topic ${headers[kafka.TOPIC]}")
            .log("  on the partition ${headers[kafka.PARTITION]}")
            .log("  with the offset ${headers[kafka.OFFSET]}")
            .log("  with the key ${headers[kafka.KEY]}")
            .choice()
            .when(simple("$headers[kafka.TOPIC]} == 'processEvents'"))
            .to("mongodb:tmdMongo?database=myMDB&collection=BPMN_ProcessEvents&operation=save")
            .when(simple("$headers[kafka.TOPIC]} == 'taskEvents'"))
            .to("mongodb:tmdMongo?database=myMDB?collection=BPMN_TaskEvents&operation=save")
            .when(simple("$headers[kafka.TOPIC]} == 'signalEvents'"))
            .to("mongodb:tmdMongo?database=myMDB?collection=BPMN_SignalEvents&operation=save")
            .otherwise()
            .log("----No Matching TOPIC found for Mongo Route----");
    }
}
