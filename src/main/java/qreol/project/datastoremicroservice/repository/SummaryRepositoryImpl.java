package qreol.project.datastoremicroservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import qreol.project.datastoremicroservice.config.RedisSchema;
import qreol.project.datastoremicroservice.model.Data;
import qreol.project.datastoremicroservice.model.MeasurementType;
import qreol.project.datastoremicroservice.model.summary.Summary;
import qreol.project.datastoremicroservice.model.summary.SummaryEntry;
import qreol.project.datastoremicroservice.model.summary.SummaryType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class SummaryRepositoryImpl implements SummaryRepository {

    private final JedisPool jedisPool;

    @Override
    public Optional<Summary> findBySensorId(
            Long sensorId,
            Set<MeasurementType> measurementTypes,
            Set<SummaryType> summaryTypes
    ) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (!recordIsPresent(jedis, sensorId))
                return Optional.empty();

            if (measurementTypes.isEmpty())
                measurementTypes = Set.of(MeasurementType.values());

            if (summaryTypes.isEmpty())
                summaryTypes = Set.of(SummaryType.values());

            return getSummary(
                    sensorId,
                    measurementTypes,
                    summaryTypes,
                    jedis
            );
        }
    }

    private Optional<Summary> getSummary(
            Long sensorId,
            Set<MeasurementType> measurementTypes,
            Set<SummaryType> summaryTypes,
            Jedis jedis
    ) {
        Summary summary = new Summary(sensorId);

        for (MeasurementType mType : measurementTypes) {
            for (SummaryType sType : summaryTypes) {
                SummaryEntry entry = new SummaryEntry(sType);

                String value = jedis.hget(
                        RedisSchema.summaryKey(sensorId, mType),
                        sType.name().toLowerCase()
                );

                String counter = jedis.hget(
                        RedisSchema.summaryKey(sensorId, mType),
                        "counter");

                entry.setCounter((counter != null) ? Long.parseLong(counter) : 0);
                entry.setValue((value != null) ? Double.parseDouble(value) : 0);

                summary.addValue(mType, entry);
            }
        }

        return Optional.of(summary);
    }

    @Override
    public void handle(Data data) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (!recordIsPresent(jedis, data.getSensorId())) {
                jedis.sadd(RedisSchema.sensorKeys(), String.valueOf(data.getSensorId()));
            }

            updateData(data, jedis);
        }
    }

    private void updateData(Data data, Jedis jedis) {
        String key = RedisSchema.summaryKey(
                data.getSensorId(),
                data.getMeasurementType()
        );

        updateMinValue(data, jedis, key);
        updateMaxValue(data, jedis, key);
        updateSumAndAvgValue(data, jedis, key);
    }

    private void updateMinValue(Data data, Jedis jedis, String key) {
        String field = SummaryType.MIN.name().toLowerCase();
        String value = jedis.hget(key, field);

        if (value == null || data.getMeasurement() < Double.parseDouble(value)) {
            jedis.hset(key, field, String.valueOf(data.getMeasurement()));
        }

    }

    private void updateMaxValue(Data data, Jedis jedis, String key) {
        String field = SummaryType.MAX.name().toLowerCase();
        String value = jedis.hget(key, field);

        if (value == null || data.getMeasurement() > Double.parseDouble(value)) {
            jedis.hset(key, field, String.valueOf(data.getMeasurement()));
        }

    }

    private void updateSumValue(Data data, Jedis jedis, String key) {
        String field = SummaryType.SUM.name().toLowerCase();
        String value = jedis.hget(key, field);

        if (value == null) {
            jedis.hset(key, field, String.valueOf(data.getMeasurement()));
        } else {
            jedis.hincrByFloat(key, field, data.getMeasurement());
        }
    }

    public void updateSumAndAvgValue(Data data, Jedis jedis, String key) {
        updateSumValue(data, jedis, key);
        String counter = jedis.hget(key, "counter");
        String field = SummaryType.AVG.name().toLowerCase();

        if (counter == null) {
            counter = String.valueOf(
                    jedis.hset(key, "counter", String.valueOf(1))
            );
        } else {
            counter = String.valueOf(
                    jedis.hincrByFloat(key, "counter", 1)
            );
        }

        String sum = jedis.hget(key, SummaryType.SUM.name()).toLowerCase();
        double value = Double.parseDouble(sum) / Double.parseDouble(counter);

        jedis.hset(key, field, String.valueOf(value));
    }

    private boolean recordIsPresent(Jedis jedis, Long sensorId) {
        return jedis.sismember(RedisSchema.sensorKeys(), String.valueOf(sensorId));
    }

}
