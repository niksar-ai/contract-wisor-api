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
import ai.niksar.contract_wisor_api.util.Constants;

import java.time.Duration;
import java.util.UUID;

public class DocumentParseWorkflowImpl implements DocumentParseWorkflow {
    //@Value("${temporal.parseTaskQueue}")
    private String parseTaskQueue = "contract-wisor-parse-task-queue";
    @Value("${temporal.parseTaskQueueAI}")
    private String parseTaskQueueAI;

    private static final Logger logger = LoggerFactory.getLogger(DocumentParseWorkflowImpl.class);

    private final RetryOptions retryoptions = RetryOptions.newBuilder()
            .setInitialInterval(Duration.ofSeconds(1))
            .setMaximumInterval(Duration.ofSeconds(10))//set to 10 seconds for demo, default is 100 seconds
            .setBackoffCoefficient(2)
            .setMaximumAttempts(3)
            .build();

    private ObjectMapper objectMapper = new ObjectMapper();


    ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(10))
            .setTaskQueue(parseTaskQueueAI)
            .setRetryOptions(retryoptions)
            .build();

    ActivityOptions defaultActivityOptions = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofMinutes(10))
            .setTaskQueue(parseTaskQueue)
            .setRetryOptions(retryoptions)
            .build();



    private final ActivityStub                  parseDocumentActivity       = Workflow.newUntypedActivityStub(options);
    private final ActivityStub                  embedDocumentActivity       = Workflow.newUntypedActivityStub(options);
    private final DocumentParseActivities       documentParseActivities     = Workflow.newActivityStub(DocumentParseActivities.class, defaultActivityOptions);

    @Override
    public String processDocument(UUID docId)  {
        String jobId = UUID.randomUUID().toString();
        String jsonInput;
        String resultParseDocument=null;
        String resultEmbedDocument;

        try {
            jsonInput = this.objectMapper.writeValueAsString(new DocumentWorkflowInput(jobId, docId.toString()));
        } catch (JsonProcessingException e) {
            throw Workflow.wrap(e);
        }
        try {
            documentParseActivities.updateDocumentParseState(docId, Constants.ParseState.PROCESS_IN_PROGRESS);
            logger.info("WORKFLOW: Parsing document for documentId:{}", docId);
             resultParseDocument = parseDocumentActivity.execute("parseDocument", String.class, jsonInput);
            logger.info("WORKFLOW: resultParseDocument:{}", resultParseDocument);

             resultEmbedDocument = embedDocumentActivity.execute("embedDocument", String.class, jsonInput);
            logger.info("WORKFLOW: resultEmbedDocument:{}", resultEmbedDocument);
            documentParseActivities.updateDocumentAndParseState(docId, Constants.DocumentState.PARSED,Constants.ParseState.SUCCESSFUL);
        }
         catch (Exception e) {
            logger.error("WORKFLOW: Error during document processing for documentId:{} - {}", docId, e.getMessage());
             documentParseActivities.updateDocumentParseState(docId, Constants.ParseState.FAULTY);
             throw Workflow.wrap(e);
         }

        return resultParseDocument;
    }
}
