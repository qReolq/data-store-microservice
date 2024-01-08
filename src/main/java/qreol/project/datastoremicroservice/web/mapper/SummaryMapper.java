package qreol.project.datastoremicroservice.web.mapper;

import org.mapstruct.Mapper;
import qreol.project.datastoremicroservice.model.summary.Summary;
import qreol.project.datastoremicroservice.web.dto.SummaryDto;

@Mapper(componentModel = "spring")
public interface SummaryMapper extends Mappable<Summary, SummaryDto> {
}
