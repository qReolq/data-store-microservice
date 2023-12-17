package qreol.project.datastoremicroservice.service.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import qreol.project.datastoremicroservice.model.Data;
import qreol.project.datastoremicroservice.model.MeasurementType;
import qreol.project.datastoremicroservice.service.CDCEventConsumer;
import qreol.project.datastoremicroservice.service.SummaryService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class DebeziumEventConsumerImpl implements CDCEventConsumer {

    private final SummaryService summaryService;

    @Override
    @KafkaListener(topics = "data")
    public void handle(String message) {
        Data data = parseData(message);
        summaryService.handle(data);
    }

    private Data parseData(String message) {
        Data data = new Data();
        try {
            JsonObject payload = JsonParser.parseString(message)
                    .getAsJsonObject()
                    .get("payload").getAsJsonObject();

            data.setId(payload.get("id").getAsString());
            data.setSensorId(payload.get("sensorId").getAsLong());
            data.setMeasurement(payload.get("measurement").getAsDouble());
            data.setMeasurementType(
                    MeasurementType.valueOf(payload.get("type").getAsString())
            );
            data.setTimestamp(parseLocalDateTime(payload.get("timestamp")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private LocalDateTime parseLocalDateTime(JsonElement timestamp) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(
                        timestamp.getAsLong() / 1000
                ), TimeZone.getDefault().toZoneId()
        );
    }

}
