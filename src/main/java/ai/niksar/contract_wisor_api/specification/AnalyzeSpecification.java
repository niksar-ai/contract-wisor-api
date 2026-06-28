package ai.niksar.contract_wisor_api.specification;


import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import ai.niksar.contract_wisor_api.dto.AnalyzeFilterDTO;
import ai.niksar.contract_wisor_api.dto.DocumentMetadataFilterDTO;
import ai.niksar.contract_wisor_api.model.Analyze;
import ai.niksar.contract_wisor_api.model.Document;
import ai.niksar.contract_wisor_api.model.DocumentMetadata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnalyzeSpecification implements Specification<Analyze> {
    private AnalyzeFilterDTO filter;
    private DocumentMetadataFilterDTO metadataFilter;
    private boolean isListByDocument;

    public AnalyzeSpecification(AnalyzeFilterDTO filter ,DocumentMetadataFilterDTO metadataFilter,boolean isListByDocument) {
        this.filter = filter;
        this.metadataFilter=metadataFilter;
        this.isListByDocument=isListByDocument;
    }
    @Override
    public Predicate toPredicate(Root<Analyze> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(isListByDocument){
            Subquery<Date> subquery = query.subquery(Date.class);
            Root<Analyze> subAnalyze = subquery.from(Analyze.class);
            subquery.select(criteriaBuilder.greatest(subAnalyze.<Date>get("updatedAt")))
                    .where(criteriaBuilder.equal(subAnalyze.get("documentId"), root.get("documentId")));
            predicates.add(criteriaBuilder.equal(root.get("updatedAt"), subquery));
        }
        query.orderBy(criteriaBuilder.desc(root.get("updatedAt")));
        if (filter.getState() != null) {
            predicates.add(criteriaBuilder.equal(root.get("state"), filter.getState()));
        }
        if (filter.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
        }
        if (filter.getCreatedAtStart() != null && filter.getCreatedAtEnd() != null) {
            predicates.add(criteriaBuilder.between(root.get("createdAt"), filter.getCreatedAtStart(), filter.getCreatedAtEnd()));
        }
        if (filter.getUpdatedAtStart() != null && filter.getUpdatedAtEnd() != null) {
            predicates.add(criteriaBuilder.between(root.get("updatedAt"), filter.getUpdatedAtStart(), filter.getUpdatedAtEnd()));
        }
        if (filter.getProcessStartedStart() != null && filter.getProcessStartedEnd() != null) {
            predicates.add(criteriaBuilder.between(root.get("processStarted"), filter.getProcessStartedStart(), filter.getProcessStartedEnd()));
        }
        if (filter.getProcessFinishedStart() != null && filter.getProcessFinishedEnd() != null) {
            predicates.add(criteriaBuilder.between(root.get("processFinished"), filter.getProcessFinishedStart(), filter.getProcessFinishedEnd()));
        }
        if (filter.getCreateUser() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("createUser")), "%" + filter.getCreateUser().toUpperCase() + "%"));
        }
        if (filter.getUpdateUser() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("updateUser")), "%" + filter.getUpdateUser().toUpperCase() + "%"));
        }
        if(filter.getDocumentId() != null){
            predicates.add(criteriaBuilder.equal(root.get("documentId"),filter.getDocumentId()));
        }

        Join<Analyze, Document> documentJoin = root.join("document", JoinType.LEFT);
        Join<Document, DocumentMetadata> metadataJoin = documentJoin.join("metadata", JoinType.LEFT);

        if(filter.getIsMetadataExist() != null && filter.getIsMetadataExist().equals("1")){
            predicates.add(criteriaBuilder.isNotNull(documentJoin.get("metadataId")));
        }
        if(filter.getIsMetadataExist() != null && filter.getIsMetadataExist().equals("0")){
            predicates.add(criteriaBuilder.isNull(documentJoin.get("metadataId")));
        }
        if(filter.getDocumentTypeId() != null){
            predicates.add(criteriaBuilder.equal(documentJoin.get("documentTypeId"),filter.getDocumentTypeId()));
        }
        if (filter.getDocumentName() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(documentJoin.get("name")), "%" + filter.getDocumentName().toUpperCase() + "%"));
        }
        if (metadataFilter.getExpiredContract() != null) {
            predicates.add(criteriaBuilder.equal(metadataJoin.get("expiredContract"), metadataFilter.getExpiredContract()));
        }
        if (metadataFilter.getCompany() != null) {
            predicates.add(criteriaBuilder.equal(metadataJoin.get("company"), metadataFilter.getCompany()));
        }
        if (metadataFilter.getExpiryDateFirst() != null && metadataFilter.getExpiryDateLast() != null) {
            predicates.add(criteriaBuilder.between(metadataJoin.get("expiryDate"),metadataFilter.getExpiryDateFirst(),metadataFilter.getExpiryDateLast()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
