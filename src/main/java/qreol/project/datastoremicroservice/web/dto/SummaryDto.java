package qreol.project.datastoremicroservice.web.dto;

import lombok.Getter;
import lombok.Setter;
import qreol.project.datastoremicroservice.model.MeasurementType;
import qreol.project.datastoremicroservice.model.summary.SummaryEntry;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SummaryDto {

    private Long sensor_id;
    private Map<MeasurementType, List<SummaryEntry>> values;

}
