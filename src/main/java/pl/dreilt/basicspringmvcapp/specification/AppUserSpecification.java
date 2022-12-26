package pl.dreilt.basicspringmvcapp.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class AppUserSpecification implements Specification<AppUser> {
    private List<SearchCriteria> searchCriteriaList;

    public AppUserSpecification() {
        this.searchCriteriaList = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        searchCriteriaList.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<AppUser> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        for (SearchCriteria criteria : searchCriteriaList) {
            switch (criteria.getOperation()) {
                case LIKE -> predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%")
                );
                case IN -> predicates.add(
                        criteriaBuilder.in(
                                root.get(criteria.getKey())).value(criteria.getValue())
                );
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
