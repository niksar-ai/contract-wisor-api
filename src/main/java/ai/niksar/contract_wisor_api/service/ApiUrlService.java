package ai.niksar.contract_wisor_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.model.ApiUrl;
import ai.niksar.contract_wisor_api.repository.ApiUrlRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ApiUrlService {
    @Autowired
    ApiUrlRepository apiUrlRepository;
    @Autowired
    @Lazy
    private RoleApiUrlService roleApiUrlService;

    public ApiUrl saveApiUrl(ApiUrl apiUrl){
        apiUrl.setUpdateDate(LocalDateTime.now());
        return apiUrlRepository.save(apiUrl);
    }
    public ApiUrl updateApiUrl(ApiUrl apiUrl){
        ApiUrl existApiUrl=getApiUrlById(apiUrl.getId());
        apiUrl.setCreateDate(existApiUrl.getCreateDate());
        apiUrl.setUrl(existApiUrl.getUrl());
        apiUrl.setMethod(existApiUrl.getMethod());
        apiUrl.setController(existApiUrl.getController());
        apiUrl.setUpdateDate(LocalDateTime.now());
        return apiUrlRepository.save(apiUrl);
    }
    public void checkUrlApi(List<ApiUrl> urlList){
    List<ApiUrl> savedApiUrlList=getList();
    savedApiUrlList.forEach(item->{
        if(urlList.stream().noneMatch(it->it.getUrl().equals(item.getUrl()) && it.getController().equals(item.getController()) && it.getMethod().equals(item.getMethod()))){
            roleApiUrlService.deleteRoleApiUrl(item.getId());
            apiUrlRepository.delete(item);
        }
    });
    }
    public List<ApiUrl> getList(){
        return apiUrlRepository.urlListAsc();
    }
    public List<String> apiUrlList(){return apiUrlRepository.urlPathList();}
    public boolean isExistApiUrl(String method,String url,String controller){
        return apiUrlRepository.existsByMethodAndUrlAndController(method,url,controller);
    }
    public ApiUrl getApiUrlById(UUID id){
        return apiUrlRepository.findById(id).orElseThrow(ContractWisorException.E029::new);
    }
    public ApiUrl getExistApiUrlDesc(String controller){
        return apiUrlRepository.findByController(controller);
    }
    public ApiUrl getExistApiUrlMethod(String method,String url){
        return apiUrlRepository.findByUrlAndMethod(url,method);
    }
    public void collectEndPoints(String method, String url, String controller,String code){
        ApiUrl existApiUrl=apiUrlRepository.findByUrlMethodOrCodeOrController(url,method,code,controller);
        if(existApiUrl != null){
            existApiUrl.setUrl(url);
            existApiUrl.setMethod(method);
            existApiUrl.setController(controller);
            existApiUrl.setCode(code);
            existApiUrl.setUpdateDate(LocalDateTime.now());
            saveApiUrl(existApiUrl);
        }
        else{
            ApiUrl apiUrl=new ApiUrl();
            apiUrl.setUrl(url);
            apiUrl.setMethod(method);
            apiUrl.setController(controller);
            apiUrl.setCode(code);
            saveApiUrl(apiUrl);
        }
    }
}
