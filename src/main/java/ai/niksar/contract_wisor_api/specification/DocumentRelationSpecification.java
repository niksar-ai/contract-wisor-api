package ai.niksar.contract_wisor_api.specification;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import ai.niksar.contract_wisor_api.dto.DocumentFilterDTO;
import ai.niksar.contract_wisor_api.dto.DocumentMetadataFilterDTO;
import ai.niksar.contract_wisor_api.model.*;

import java.util.ArrayList;
import java.util.List;


public class DocumentRelationSpecification implements Specification<DocumentRelation> {

    private DocumentFilterDTO filter;
    private DocumentMetadataFilterDTO metadataFilter;
    public DocumentRelationSpecification(DocumentFilterDTO filter, DocumentMetadataFilterDTO metadataFilter) {
        this.filter = filter;
        this.metadataFilter = metadataFilter;
    }

    @Override
    public Predicate toPredicate(Root<DocumentRelation> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        Join<Analyze, Document> documentJoin = root.join("child", JoinType.LEFT);
        Join<Document, DocumentMetadata> metadataJoin = documentJoin.join("metadata", JoinType.LEFT);

        if (filter.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(documentJoin.get("status"), filter.getStatus()));
        }
        if (filter.getState() != null) {
            predicates.add(criteriaBuilder.equal(documentJoin.get("state"), filter.getState()));
        }

        if (filter.getName() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(documentJoin.get("name")), "%" + filter.getName().toUpperCase() + "%"));
        }

        if (filter.getPageCountMin() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(documentJoin.get("pageCount"), filter.getPageCountMin()));
        }

        if (filter.getPageCountMax() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(documentJoin.get("pageCount"), filter.getPageCountMax()));
        }

        if (filter.getSizeMin() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(documentJoin.get("size"), filter.getSizeMin()));
        }

        if (filter.getSizeMax() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(documentJoin.get("size"), filter.getSizeMax()));
        }

        if(filter.getFirstUpdateDate() != null && filter.getLastUpdateDate() != null){
            predicates.add(criteriaBuilder.between(documentJoin.get("updateDate"),filter.getFirstUpdateDate(),filter.getLastUpdateDate()));
        }
        if(filter.getFirstCreateDate() != null && filter.getLastCreateDate()!= null){
            predicates.add(criteriaBuilder.between(documentJoin.get("createDate"),filter.getFirstCreateDate(),filter.getLastCreateDate()));
        }
        if(filter.getIsMetadataExist() != null && filter.getIsMetadataExist().equals("1")){
            predicates.add(criteriaBuilder.isNotNull(documentJoin.get("metadataId")));
        }
        if(filter.getIsMetadataExist() != null && filter.getIsMetadataExist().equals("0")){
            predicates.add(criteriaBuilder.isNull(documentJoin.get("metadataId")));
        }
        if (filter.getCreateUser() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(documentJoin.get("createUser")), "%" + filter.getCreateUser().toUpperCase() + "%"));
        }
        if (filter.getUpdateUser() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(documentJoin.get("updateUser")), "%" + filter.getUpdateUser().toUpperCase() + "%"));
        }
        if(filter.getDocumentTypeId() != null){
            predicates.add(criteriaBuilder.equal(root.get("id"),filter.getDocumentTypeId()));
        }
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
