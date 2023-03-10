package nl.ivonet.dto;

import java.util.List;

public record ValidationResponse(ActionMetadata actionMetadata, List<ProcessingData<DataCommon>> processingData) {
}
