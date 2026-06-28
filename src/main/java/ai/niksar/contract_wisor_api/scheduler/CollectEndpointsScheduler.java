package ai.niksar.contract_wisor_api.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import ai.niksar.contract_wisor_api.model.ApiUrl;
import ai.niksar.contract_wisor_api.service.ApiUrlService;
import ai.niksar.contract_wisor_api.util.ActionCode;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class CollectEndpointsScheduler {
    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private ApiUrlService apiUrlService;

    @Value("${action.update}")
    private boolean urlUpdateEnable;

    @EventListener(ApplicationReadyEvent.class)
    public void collectEndpoints() {
        if(urlUpdateEnable){
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
            List<ApiUrl> currentUrlList= new ArrayList<>();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
                if(entry.getKey().getMethodsCondition().isEmpty() || entry.getKey().getPathPatternsCondition().getPatterns().isEmpty()){
                    continue;
                }
                String method = ((LinkedHashSet) entry.getKey().getMethodsCondition().getMethods()).iterator().next().toString();
                String url = ((TreeSet) entry.getKey().getPathPatternsCondition().getPatterns()).first().toString();
                String controller=entry.getValue().toString();

                Method methodObject = entry.getValue().getMethod();
                ActionCode actionCodeAnnotation = methodObject.getAnnotation(ActionCode.class);
                String actionCode = (actionCodeAnnotation != null) ? actionCodeAnnotation.value() : null;
                ApiUrl newApiUrl= new ApiUrl();
                newApiUrl.setUrl(url);
                newApiUrl.setMethod(method);
                newApiUrl.setController(controller);
                newApiUrl.setCode(actionCode);
                currentUrlList.add(newApiUrl);
                apiUrlService.collectEndPoints(method,url,controller,actionCode);
            }
            apiUrlService.checkUrlApi(currentUrlList);
        }
    }
}
