package ai.niksar.contract_wisor_api.specification;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import ai.niksar.contract_wisor_api.dto.DocumentFilterDTO;
import ai.niksar.contract_wisor_api.dto.DocumentMetadataFilterDTO;
import ai.niksar.contract_wisor_api.model.Document;
import ai.niksar.contract_wisor_api.model.DocumentMetadata;

import java.util.ArrayList;
import java.util.List;


public class DocumentSpecification implements Specification<Document> {

    private DocumentFilterDTO filter;
    private DocumentMetadataFilterDTO metadataFilter;
    public DocumentSpecification(DocumentFilterDTO filter, DocumentMetadataFilterDTO metadataFilter) {
        this.filter = filter;
        this.metadataFilter = metadataFilter;
    }

    @Override
    public Predicate toPredicate(Root<Document> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getState() != null) {
            predicates.add(criteriaBuilder.equal(root.get("state"), filter.getState()));
        }

        if (filter.getParseState() != null) {
            predicates.add(criteriaBuilder.equal(root.get("parseState"), filter.getParseState()));
        }

        if (filter.getAnalyzeState() != null) {
            predicates.add(criteriaBuilder.equal(root.get("analyzeState"), filter.getAnalyzeState()));
        }

        if (filter.getName() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + filter.getName().toUpperCase() + "%"));
        }

        if (filter.getPageCountMin() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("pageCount"), filter.getPageCountMin()));
        }

        if (filter.getPageCountMax() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("pageCount"), filter.getPageCountMax()));
        }

        if (filter.getSizeMin() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("size"), filter.getSizeMin()));
        }

        if (filter.getSizeMax() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("size"), filter.getSizeMax()));
        }

        if(filter.getFirstUpdateDate() != null && filter.getLastUpdateDate() != null){
            predicates.add(criteriaBuilder.between(root.get("updateDate"),filter.getFirstUpdateDate(),filter.getLastUpdateDate()));
        }
        if(filter.getFirstCreateDate() != null && filter.getLastCreateDate()!= null){
            predicates.add(criteriaBuilder.between(root.get("createDate"),filter.getFirstCreateDate(),filter.getLastCreateDate()));
        }
        if(filter.getIsMetadataExist() != null && filter.getIsMetadataExist().equals("1")){
            predicates.add(criteriaBuilder.isNotNull(root.get("metadataId")));
        }
        if(filter.getIsMetadataExist() != null && filter.getIsMetadataExist().equals("0")){
            predicates.add(criteriaBuilder.isNull(root.get("metadataId")));
        }
        if (filter.getCreateUser() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("createUser")), "%" + filter.getCreateUser().toUpperCase() + "%"));
        }
        if (filter.getUpdateUser() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("updateUser")), "%" + filter.getUpdateUser().toUpperCase() + "%"));
        }
        if(filter.getDocumentTypeId() != null){
            predicates.add(criteriaBuilder.equal(root.get("documentTypeId"),filter.getDocumentTypeId()));
        }
        // Metadata ile JOIN yapma
        Join<Document, DocumentMetadata> metadataJoin = root.join("metadata", JoinType.LEFT);

        if (metadataFilter.getExpiredContract() != null) {
            predicates.add(criteriaBuilder.equal(metadataJoin.get("expiredContract"), metadataFilter.getExpiredContract()));
        }
        if (metadataFilter.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(metadataJoin.get("status"), metadataFilter.getStatus()));
        }

        if (metadataFilter.getDocumentId() != null) {
            predicates.add(criteriaBuilder.equal(metadataJoin.get("documentId"), metadataFilter.getDocumentId()));
        }

        if (metadataFilter.getCreateDate() != null) {
            predicates.add(criteriaBuilder.equal(metadataJoin.get("createDate"), metadataFilter.getCreateDate()));
        }

        if (metadataFilter.getCreateTime() != null) {
            predicates.add(criteriaBuilder.equal(metadataJoin.get("createTime"), metadataFilter.getCreateTime()));
        }

        if (metadataFilter.getCompany() != null) {
            predicates.add(criteriaBuilder.equal(metadataJoin.get("company"), metadataFilter.getCompany()));
        }

        if (metadataFilter.getExpiredContract() != null) {
            predicates.add(criteriaBuilder.equal(metadataJoin.get("expiredContract"), metadataFilter.getExpiredContract()));
        }

        if (metadataFilter.getRelationType() != null) {
            predicates.add(criteriaBuilder.equal(metadataJoin.get("relationType"), metadataFilter.getRelationType()));
        }

        if (metadataFilter.getExpiryDateFirst() != null && metadataFilter.getExpiryDateLast() != null) {
            predicates.add(criteriaBuilder.between(metadataJoin.get("expiryDate"),metadataFilter.getExpiryDateFirst(),metadataFilter.getExpiryDateLast()));
        }

        if (metadataFilter.getDescription() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(metadataJoin.get("description")), "%" + metadataFilter.getDescription().toUpperCase() + "%"));
        }

        if (metadataFilter.getAmended() != null) {
            predicates.add(criteriaBuilder.equal(metadataJoin.get("amended"), metadataFilter.getAmended()));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
