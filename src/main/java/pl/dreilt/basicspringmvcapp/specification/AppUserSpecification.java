package pl.dreilt.basicspringmvcapp.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.dreilt.basicspringmvcapp.user.AppUser;

import javax.persistence.criteria.Predicate;

public class AppUserSpecification {

    private AppUserSpecification() {}

    public static Specification<AppUser> bySearch(String searchQuery) {
        return (root, query, criteriaBuilder) -> {
            Predicate firstName = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + searchQuery.toLowerCase() + "%");
            Predicate lastName = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + searchQuery.toLowerCase() + "%");
            Predicate email = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + searchQuery.toLowerCase() + "%");
            return criteriaBuilder.or(firstName, lastName, email);
        };
    }

    public static Specification<AppUser> bySearch(String searchQuery, String searchQuery2) {
        return (root, query, criteriaBuilder) -> {
            Predicate firstName = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + searchQuery.toLowerCase() + "%");
            Predicate lastName = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + searchQuery2.toLowerCase() + "%");
            return criteriaBuilder.and(firstName, lastName);
        };
    }
}
