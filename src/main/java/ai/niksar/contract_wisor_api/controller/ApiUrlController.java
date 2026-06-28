package ai.niksar.contract_wisor_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ai.niksar.contract_wisor_api.dto.PasswordDTO;
import ai.niksar.contract_wisor_api.model.ApiUrl;
import ai.niksar.contract_wisor_api.service.ApiUrlService;
import ai.niksar.contract_wisor_api.service.RoleApiUrlService;
import ai.niksar.contract_wisor_api.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/administration/action")
public class ApiUrlController {
    @Autowired
    private ApiUrlService apiUrlService;
    @Autowired
    private RoleApiUrlService roleApiUrlService;

    @PostMapping("/save")
    public ResponseEntity<?> saveApiUrl(@RequestBody ApiUrl apiUrl){
        return ResponseEntity.ok(apiUrlService.updateApiUrl(apiUrl));
    }
    @GetMapping("/list")
    public ResponseEntity<?> getApiUrlList(){
        return ResponseEntity.ok(apiUrlService.getList());
    }
}
