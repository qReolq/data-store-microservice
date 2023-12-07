package qreol.project.datastoremicroservice.repository;

import qreol.project.datastoremicroservice.model.summary.MeasurementType;
import qreol.project.datastoremicroservice.model.summary.Summary;
import qreol.project.datastoremicroservice.model.summary.SummaryType;

import java.util.Optional;
import java.util.Set;

public interface SummaryRepository {

    Optional<Summary> findBySensorId(
            Long sensorId,
            Set<MeasurementType> measurementTypes,
            Set<SummaryType> summaryTypes
    );

}
