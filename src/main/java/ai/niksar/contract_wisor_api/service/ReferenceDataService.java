package ai.niksar.contract_wisor_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.model.ReferenceData;
import ai.niksar.contract_wisor_api.repository.ReferenceDataRepository;
import ai.niksar.contract_wisor_api.util.ReferenceDataItem;

import java.util.List;
import java.util.UUID;

@Service
public class ReferenceDataService {

    @Autowired
    private ReferenceDataRepository referenceDataRepository;

    public ReferenceData saveReferenceData(String refId, List<ReferenceDataItem> jsonData) {
        ReferenceData referenceData = new ReferenceData();
        referenceData.setRefId(refId);

        referenceData.setData(jsonData);

        return referenceDataRepository.save(referenceData);
    }

    public ReferenceData updateReferenceData(UUID id, List<ReferenceDataItem> jsonData) {
        ReferenceData existingData = referenceDataRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reference data not found."));

        existingData.setData(jsonData);

        return referenceDataRepository.save(existingData);
    }

    public List<ReferenceData> getReferenceDataByRefId(String refId) {
        return referenceDataRepository.findByRefId(refId);
    }

    public List<ReferenceData> getAllReferenceData() {
        return referenceDataRepository.findAll();
    }

    public void deleteReferenceData(UUID id) {
        ReferenceData existingData = referenceDataRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reference data not found."));

        referenceDataRepository.delete(existingData);
    }
}
