package ai.niksar.contract_wisor_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ai.niksar.contract_wisor_api.model.ReferenceData;
import ai.niksar.contract_wisor_api.service.ReferenceDataService;
import ai.niksar.contract_wisor_api.util.ReferenceDataItem;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reference-data")
public class ReferenceDataController {

    @Autowired
    private ReferenceDataService referenceDataService;

    @PostMapping("/save")
    public ResponseEntity<ReferenceData> saveReferenceData(@RequestParam String refId, @RequestBody List<ReferenceDataItem> jsonData) {
        return ResponseEntity.ok(referenceDataService.saveReferenceData(refId, jsonData));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReferenceData> updateReferenceData(@PathVariable UUID id, @RequestBody List<ReferenceDataItem> jsonData) {
        return ResponseEntity.ok(referenceDataService.updateReferenceData(id, jsonData));
    }

    @GetMapping("/{refId}")
    public ResponseEntity<List<ReferenceData>> getReferenceData(@PathVariable String refId) {
        return ResponseEntity.ok(referenceDataService.getReferenceDataByRefId(refId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReferenceData>> getAllReferenceData() {
        return ResponseEntity.ok(referenceDataService.getAllReferenceData());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReferenceData(@PathVariable UUID id) {
        referenceDataService.deleteReferenceData(id);
        return ResponseEntity.noContent().build();
    }
}
