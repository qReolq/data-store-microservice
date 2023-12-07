package qreol.project.datastoremicroservice.service;

import qreol.project.datastoremicroservice.model.summary.MeasurementType;
import qreol.project.datastoremicroservice.model.summary.Summary;
import qreol.project.datastoremicroservice.model.summary.SummaryType;

import java.util.Set;

public interface SummaryService {
    
    Summary get(
            Long sensorId,
            Set<MeasurementType> measurementTypes,
            Set<SummaryType> summaryTypes
    );
    
}
