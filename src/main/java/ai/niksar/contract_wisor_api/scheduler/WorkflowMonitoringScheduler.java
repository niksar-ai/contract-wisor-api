package ai.niksar.contract_wisor_api.scheduler;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.api.enums.v1.WorkflowExecutionStatus;
import io.temporal.api.workflowservice.v1.DescribeWorkflowExecutionRequest;
import io.temporal.api.workflowservice.v1.DescribeWorkflowExecutionResponse;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ai.niksar.contract_wisor_api.config.TemporalConfig;
import ai.niksar.contract_wisor_api.model.Document;
import ai.niksar.contract_wisor_api.service.DocumentService;

import java.util.*;

@Component
public class WorkflowMonitoringScheduler {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private TemporalConfig temporalConfig;
    private static final Logger logger = LoggerFactory.getLogger(WorkflowMonitoringScheduler.class);
    private WorkflowExecutionStatus status;

    @Value("${temporal.namespace}")
    String temporalNamespace;
    @Async
    public void checkWorkflowStatusPeriodically(UUID docId, WorkflowServiceStubs service) throws InterruptedException {
        boolean isWorkflowCompleted = false;
        while (!isWorkflowCompleted) {
            TimeUnit.SECONDS.sleep(10);
            isWorkflowCompleted = checkWorkflowCompletion(docId, service);
            logger.info("Workflow execute check : " + docId + " - " + status);
            if (isWorkflowCompleted) {
                documentService.updateActiveDocumentState(docId);
            }
        }
    }

    /**
     * Queries the workflow status and returns its completion state.
     */
    private boolean checkWorkflowCompletion(UUID docId, WorkflowServiceStubs service) {
        DescribeWorkflowExecutionRequest request = DescribeWorkflowExecutionRequest.newBuilder()
                .setNamespace(temporalNamespace)
                .setExecution(WorkflowExecution.newBuilder().setWorkflowId(docId.toString()).build())
                .build();
        try {
            DescribeWorkflowExecutionResponse response = service.blockingStub().describeWorkflowExecution(request);
            status = response.getWorkflowExecutionInfo().getStatus();

            return status != WorkflowExecutionStatus.WORKFLOW_EXECUTION_STATUS_RUNNING;
        } catch (Exception e) {
            logger.error("An error occurred while checking the workflow status: " + e.getMessage());
            documentService.updateActiveDocumentState(docId);
            return true;
        }
    }
    /**
     * When the server starts up...
     */
    @EventListener(ApplicationReadyEvent.class)
    public void monitorAllWorkflows() {
        List<Document> activeDocuments = documentService.activeDocumentList();
        for (Document document : activeDocuments) {
            try {
                checkWorkflowStatusPeriodically(document.getId(),temporalConfig.workflowServiceStubs());
            } catch (Exception e) {
                logger.error("Error while checking the document workflow: " + e.getMessage());
            }
        }
    }
}
