package ai.niksar.contract_wisor_api.workflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.ActivityStub;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import ai.niksar.contract_wisor_api.dto.DocumentWorkflowInput;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.util.Constants;

import java.time.Duration;
import java.util.UUID;

public class DocumentWorkflowAnalysisImpl implements DocumentAnalysisWorkflow {
    private ObjectMapper objectMapper = new ObjectMapper();
    //@Value("${temporal.analyzeTaskQueue}")
    private String analyzeTaskQueue = "contract-wisor-analyze-task-queue";
    @Value("${temporal.analyzeTaskQueueAI}")
    private String analyzeTaskQueueAI;

    private static final Logger logger = LoggerFactory.getLogger(DocumentWorkflowAnalysisImpl.class);
    /*Initial Interval     = 1 second
    Backoff Coefficient  = 2.0
    Maximum Interval     = 100 × Initial Interval
    Maximum Attempts     = ∞
    Non-Retryable Errors = []*/

    private final RetryOptions retryoptions = RetryOptions.newBuilder()
            .setInitialInterval(Duration.ofSeconds(1))
            .setMaximumInterval(Duration.ofSeconds(10))
            .setBackoffCoefficient(2)
            .setMaximumAttempts(3)
            .build();

    ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(20))
            .setTaskQueue(analyzeTaskQueueAI)
            .setRetryOptions(retryoptions)
            .build();
    ActivityOptions defaultActivityOptions = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(10))
            .setTaskQueue(analyzeTaskQueue)
            .setRetryOptions(retryoptions)
            .build();

    private final ActivityStub analyzeDocumentActivity = Workflow.newUntypedActivityStub(options);
    private final DocumentAnalyzeActivities documentAnalyzeActivities = Workflow.newActivityStub(DocumentAnalyzeActivities.class, defaultActivityOptions);


    @Override
    public String analyzeDocument(UUID documentId,String username) {
        String jobId = UUID.randomUUID().toString();
        String jsonInput = null;
        String resultAnalyzeDocument = null;

        try {
            jsonInput = this.objectMapper.writeValueAsString(new DocumentWorkflowInput(jobId, documentId.toString()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            documentAnalyzeActivities.updateDocumentAnalyzeState(documentId, Constants.AnalyzeState.FAULTY);
        }
        try {
            String analyzeId = documentAnalyzeActivities.createAnalyze(documentId,username);
            resultAnalyzeDocument = analyzeDocumentActivity.execute("analyzeDocument", String.class, jsonInput);
            documentAnalyzeActivities.updateAnalyze(UUID.fromString(analyzeId),resultAnalyzeDocument,documentId,username);
            documentAnalyzeActivities.updateDocumentStateAndAnalyzeState(documentId, Constants.DocumentState.ANALYZED, Constants.AnalyzeState.SUCCESSFUL);
            logger.info("WORKFLOW: resultAnalyzeDocument:{}", resultAnalyzeDocument);
            logger.info("WORKFLOW: processing completed for documentId: {}", documentId);
        }
        catch (Exception e) {
            logger.error("WORKFLOW: Error during document analyze processing for documentId:{} - {}", documentId, e.getMessage());
            documentAnalyzeActivities.updateDocumentAnalyzeState(documentId, Constants.AnalyzeState.FAULTY);
            throw Workflow.wrap(e);
        }
        return resultAnalyzeDocument;
    }
}