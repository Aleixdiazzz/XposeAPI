package com.aleix.XposeAPI.specification;

import com.aleix.XposeAPI.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Specification class for User entities.
 * Provides methods to create dynamic queries for filtering Users based on various criteria.
 */
public class UserSpecifications {
    /**
     * Creates a specification for filtering Users based on provided criteria.
     * 
     * @param name Optional name filter (case-insensitive, accent-insensitive)
     * @param surname Optional surname filter (case-insensitive, accent-insensitive)
     * @param email Optional email filter (case-insensitive, accent-insensitive)
     * @return Specification for filtering Users
     */
    public static Specification<User> filterUsers(String name, String surname, String email) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("unaccent", String.class, root.get("name"))),
                        "%" + removeAccents(name.toLowerCase()) + "%"
                ));
            }
            if (surname != null && !surname.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("unaccent", String.class, root.get("surname"))),
                        "%" + removeAccents(surname.toLowerCase()) + "%"
                ));
            }
            if (email != null && !email.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("unaccent", String.class, root.get("email"))),
                        "%" + removeAccents(email.toLowerCase()) + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Removes accents from a string for accent-insensitive searching.
     * 
     * @param text The text to remove accents from
     * @return The text with accents removed
     */
    private static String removeAccents(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", ""); // Removes diacritical marks
    }
}
