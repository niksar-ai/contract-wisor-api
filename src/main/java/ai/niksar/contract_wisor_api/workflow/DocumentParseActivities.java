package ai.niksar.contract_wisor_api.workflow;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface DocumentParseActivities {
    @ActivityMethod(name = "updateDocumentParseState")
    String updateDocumentParseState(UUID documentId, String state);

    @ActivityMethod(name = "updateDocumentState")
    String updateDocumentState(UUID documentId, String state);

    @ActivityMethod(name = "updateDocumentAndParseState")
    String updateDocumentAndParseState(UUID documentId, String state, String parseState);
}
