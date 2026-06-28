package ai.niksar.contract_wisor_api.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

import java.util.UUID;

@WorkflowInterface
public interface DocumentAnalysisWorkflow {
    @WorkflowMethod
    String analyzeDocument(UUID documentId,String username);
}


