package ai.niksar.contract_wisor_api.workflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface DocumentAnalyzeActivities {
    @ActivityMethod(name = "updateDocumentAnalyzeState")
    String updateDocumentAnalyzeState(UUID documentId, String state);

    @ActivityMethod(name = "updateDocumentState")
    String updateDocumentState(UUID documentId, String state);

    @ActivityMethod(name = "updateDocumentStateAndAnalyzeState")
    String updateDocumentStateAndAnalyzeState(UUID documentId, String state, String analyzeState);

    @ActivityMethod(name = "createAnalyze")
    String createAnalyze(UUID documentId,String username);

    @ActivityMethod(name = "updateAnalyze")
    void updateAnalyze(UUID analyzeId,String result,UUID documentId,String username) throws JsonProcessingException;
}
