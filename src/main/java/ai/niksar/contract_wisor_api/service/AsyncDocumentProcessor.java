package ai.niksar.contract_wisor_api.service;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.api.enums.v1.WorkflowExecutionStatus;
import io.temporal.api.workflowservice.v1.DescribeWorkflowExecutionRequest;
import io.temporal.api.workflowservice.v1.DescribeWorkflowExecutionResponse;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowExecutionAlreadyStarted;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.batch.RetryManager;
import ai.niksar.contract_wisor_api.model.Document;
import ai.niksar.contract_wisor_api.model.DocumentContent;
import ai.niksar.contract_wisor_api.repository.DocumentRepository;
import ai.niksar.contract_wisor_api.util.Constants;
import ai.niksar.contract_wisor_api.util.Util;
import ai.niksar.contract_wisor_api.workflow.DocumentAnalysisWorkflow;
import ai.niksar.contract_wisor_api.workflow.DocumentParseWorkflow;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncDocumentProcessor {

    @Autowired
    private DocumentParserService documentParserService;
    @Autowired
    private WorkflowClient workflowClient;
    @Autowired
    @Lazy
    private DocumentService documentService;
    @Autowired
    private DocumentRepository documentRepository;
    @Value("${temporal.parseTaskQueue}")
    private String parseTaskQueue;
    @Value("${temporal.taskQueue}")
    private String taskQueue;
    private static final Logger logger = LoggerFactory.getLogger(AsyncDocumentProcessor.class);
    private WorkflowExecutionStatus status;

    @Async
    public void processDocumentAsync(DocumentContent savedDocument) {
        String workflowId = savedDocument.getId().toString();
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setWorkflowId(workflowId)
                .setWorkflowExecutionTimeout(Duration.ofMinutes(20))
                .build();

        DocumentParseWorkflow workflow = workflowClient.newWorkflowStub(DocumentParseWorkflow.class, options);
        try {
            WorkflowClient.start(workflow::processDocument, savedDocument.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
