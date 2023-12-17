package qreol.project.datastoremicroservice.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import qreol.project.datastoremicroservice.model.MeasurementType;
import qreol.project.datastoremicroservice.model.summary.Summary;
import qreol.project.datastoremicroservice.model.summary.SummaryType;
import qreol.project.datastoremicroservice.service.SummaryService;
import qreol.project.datastoremicroservice.web.dto.SummaryDto;
import qreol.project.datastoremicroservice.web.mapper.SummaryMapper;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final SummaryService summaryService;
    private final SummaryMapper summaryMapper;

    @GetMapping("/summary/{sensor_id}")
    public SummaryDto getSummary(
            @PathVariable Long sensor_id,
            @RequestParam(value = "mt", required = false)
            Set<MeasurementType> measurementTypes,
            @RequestParam(value = "st", required = false)
            Set<SummaryType> summaryTypes
    ) {
        Summary summary = summaryService.get(
                sensor_id,
                measurementTypes,
                summaryTypes
        );

        return summaryMapper.toDto(summary);
    }

}
