package ai.niksar.contract_wisor_api.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ai.niksar.contract_wisor_api.dto.UserFilterDTO;
import ai.niksar.contract_wisor_api.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {
    private UserFilterDTO filter;
    public UserSpecification(UserFilterDTO filter) {
        this.filter = filter;
    }
    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
        }
        if (filter.getNameTitle() != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("nameTitle")), "%" + filter.getNameTitle().toUpperCase() + "%"));
        }
        if(filter.getCreatedAtStart() != null && filter.getCreatedAtEnd()!= null){
            predicates.add(criteriaBuilder.between(root.get("createDate"),filter.getCreatedAtStart(),filter.getCreatedAtEnd()));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
