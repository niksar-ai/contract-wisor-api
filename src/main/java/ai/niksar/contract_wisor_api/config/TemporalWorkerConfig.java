package ai.niksar.contract_wisor_api.config;

import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.client.WorkflowClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ai.niksar.contract_wisor_api.service.AnalyzeService;
import ai.niksar.contract_wisor_api.service.DocumentService;
import ai.niksar.contract_wisor_api.workflow.*;

@Configuration
public class TemporalWorkerConfig {

    @Autowired
    private AnalyzeService analyzeService;
    @Autowired
    private DocumentService documentService;
    //@Value("${temporal.analyzeTaskQueue}")
    private String analyzeTaskQueue = "contract-wisor-analyze-task-queue";
    //@Value("${temporal.parseTaskQueue}")
    private String parseTaskQueue = "contract-wisor-parse-task-queue";
    @Value("${temporal.taskQueue}")
    private String taskQueue;

    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);

        Worker worker = factory.newWorker(taskQueue);
        worker.registerWorkflowImplementationTypes(DocumentParseWorkflowImpl.class, DocumentWorkflowAnalysisImpl.class);

        Worker parseWorker = factory.newWorker(parseTaskQueue);
        parseWorker.registerActivitiesImplementations(new DocumentParseActivitiesImpl(documentService));

        Worker analyzeWorker = factory.newWorker(analyzeTaskQueue);
        analyzeWorker.registerActivitiesImplementations(new DocumentAnalyzeActivitiesImpl(analyzeService, documentService));

        factory.start();
        return factory;
    }
}