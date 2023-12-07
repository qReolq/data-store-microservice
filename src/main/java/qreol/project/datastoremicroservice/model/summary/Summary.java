package qreol.project.datastoremicroservice.model.summary;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Summary {

    private Long sensor_id;
    private Map<MeasurementType, List<SummaryEntry>> values;

    public Summary() {
        values = new HashMap<>();
    }

    public void addValue(MeasurementType type, SummaryEntry value) {
        if (values.containsKey(type)) {
            List<SummaryEntry> entries = new ArrayList<>(values.get(type));
            entries.add(value);
            values.put(type, entries);
        } else {
            values.put(type, List.of(value));
        }
    }

}
