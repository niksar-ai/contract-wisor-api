package ai.niksar.contract_wisor_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ai.niksar.contract_wisor_api.dto.ApplicationSetupDTO;
import ai.niksar.contract_wisor_api.model.ApiUrl;
import ai.niksar.contract_wisor_api.service.ApplicationSetupService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/app")
public class ApplicationSetupController {
    @Autowired
    private ApplicationSetupService applicationSetupService;

    @PostMapping("/setup")
    public void setup(@RequestBody ApplicationSetupDTO applicationSetupDTO){
        applicationSetupService.setupApplication(applicationSetupDTO);
    }

    @GetMapping("/setup")
    public ResponseEntity<?> getSetupControl(){
        Map<String,Object> response=new HashMap<>();
        response.put("isFirstSetup",applicationSetupService.isFirstSetup());
        return ResponseEntity.ok(response);
    }
}
