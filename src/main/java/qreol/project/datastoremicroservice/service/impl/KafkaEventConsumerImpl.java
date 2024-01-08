package qreol.project.datastoremicroservice.service.impl;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import qreol.project.datastoremicroservice.config.LocalDateTimeDeserializer;
import qreol.project.datastoremicroservice.model.Data;
import qreol.project.datastoremicroservice.model.MeasurementType;
import qreol.project.datastoremicroservice.service.CDCEventConsumer;
import qreol.project.datastoremicroservice.service.SummaryService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class KafkaEventConsumerImpl implements CDCEventConsumer {

    private final SummaryService summaryService;
    private final LocalDateTimeDeserializer localDateTimeDeserializer;

    @Override
    @KafkaListener(topics = {"data-temperature", "data-power", "data-voltage"})
    public void handle(String message) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, localDateTimeDeserializer)
                .create();
        Data data = gson.fromJson(message, Data.class);

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
