package qreol.project.datastoremicroservice.service;

import qreol.project.datastoremicroservice.model.Data;
import qreol.project.datastoremicroservice.model.MeasurementType;
import qreol.project.datastoremicroservice.model.summary.Summary;
import qreol.project.datastoremicroservice.model.summary.SummaryType;

import java.util.Set;

public interface SummaryService {

    Summary get(
            Long sensorId,
            Set<MeasurementType> measurementTypes,
            Set<SummaryType> summaryTypes
    );

    void handle(Data data);

}
