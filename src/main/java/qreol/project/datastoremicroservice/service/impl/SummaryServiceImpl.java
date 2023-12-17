package qreol.project.datastoremicroservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import qreol.project.datastoremicroservice.model.Data;
import qreol.project.datastoremicroservice.model.exception.SensorNotFoundException;
import qreol.project.datastoremicroservice.model.MeasurementType;
import qreol.project.datastoremicroservice.model.summary.Summary;
import qreol.project.datastoremicroservice.model.summary.SummaryType;
import qreol.project.datastoremicroservice.repository.SummaryRepository;
import qreol.project.datastoremicroservice.service.SummaryService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private final SummaryRepository summaryRepository;

    @Override
    public Summary get(
            Long sensorId,
            Set<MeasurementType> measurementTypes,
            Set<SummaryType> summaryTypes
    ) {
        if (measurementTypes == null)
            measurementTypes = Set.of(MeasurementType.values());

        if (summaryTypes == null)
            summaryTypes = Set.of(SummaryType.values());


        return summaryRepository.findBySensorId(
                sensorId,
                measurementTypes,
                summaryTypes
        ).orElseThrow(SensorNotFoundException::new);
    }

    @Override
    public void handle(Data data) {
        summaryRepository.handle(data);
    }
}
