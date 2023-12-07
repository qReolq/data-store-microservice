package qreol.project.datastoremicroservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import qreol.project.datastoremicroservice.config.RedisSchema;
import qreol.project.datastoremicroservice.model.summary.MeasurementType;
import qreol.project.datastoremicroservice.model.summary.Summary;
import qreol.project.datastoremicroservice.model.summary.SummaryEntry;
import qreol.project.datastoremicroservice.model.summary.SummaryType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Locale;
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
            if (!jedis.sismember(RedisSchema.sensorKeys(), String.valueOf(sensorId)))
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
        Summary summary = new Summary();
        summary.setSensor_id(sensorId);

        for (MeasurementType mType: measurementTypes) {
            for (SummaryType sType: summaryTypes) {
                SummaryEntry entry = new SummaryEntry(sType);

                String value = jedis.hget(
                        RedisSchema.summaryKey(sensorId, mType),
                        sType.name().toLowerCase()
                );

                if (value != null) {
                    entry.setValue(Double.parseDouble(value));
                }
                summary.addValue(mType, entry);
            }
        }

        return Optional.of(summary);
    }
}
