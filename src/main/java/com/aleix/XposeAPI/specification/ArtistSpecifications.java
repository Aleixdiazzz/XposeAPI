package com.aleix.XposeAPI.specification;

import com.aleix.XposeAPI.model.Artist;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Specification class for Artist entities.
 * Provides methods to create dynamic queries for filtering Artists based on various criteria.
 */
public class ArtistSpecifications {
    /**
     * Creates a specification for filtering Artists based on provided criteria.
     * 
     * @param name Optional name filter (case-insensitive, accent-insensitive)
     * @param surname Optional surname filter (case-insensitive, accent-insensitive)
     * @param artisticName Optional artistic name filter (case-insensitive, accent-insensitive)
     * @return Specification for filtering Artists
     */
    public static Specification<Artist> filterArtists(String name, String surname, String artisticName) {
        return (Root<Artist> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
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
            if (artisticName != null && !artisticName.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("unaccent", String.class, root.get("artisticName"))),
                        "%" + removeAccents(artisticName.toLowerCase()) + "%"
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
                .replaceAll("\\p{M}", "");
    }
}
