package com.pranav.departmentemployee.kafka.producer;

import com.pranav.departmentemployee.event.EmployeeCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmployeeProducer {

    private final KafkaTemplate<String, EmployeeCreatedEvent> kafkaTemplate;

    private static final String TOPIC = "employee-events";


    public void publishEmployeeCreatedEvent(EmployeeCreatedEvent event) {

        ProducerRecord<String, EmployeeCreatedEvent> record =
                new ProducerRecord<>(TOPIC, event);

        record.headers().add("eventType",
                "EmployeeCreated".getBytes(StandardCharsets.UTF_8));

        record.headers().add("systemId",
                "HRMS".getBytes(StandardCharsets.UTF_8));

        kafkaTemplate.send(record);
    }

    }

