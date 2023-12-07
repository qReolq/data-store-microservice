package qreol.project.datastoremicroservice.config;

import qreol.project.datastoremicroservice.model.summary.MeasurementType;

import java.util.Locale;

public class RedisSchema {

    public static String sensorKeys() {
        return KeyHelper.getKey("sensors");
    }

    public static String summaryKey(
            Long sensorId,
            MeasurementType measurementType
    ) {
        return KeyHelper.getKey(
                "sensors:" + sensorId + ":" + measurementType.name().toLowerCase(Locale.ROOT)
        );
    }

}
