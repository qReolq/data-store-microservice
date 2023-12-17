package qreol.project.datastoremicroservice.model.summary;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SummaryEntry {
    private SummaryType type;
    private double value;
    private long counter;

    public SummaryEntry(SummaryType type) {
        this.type = type;
    }
}
