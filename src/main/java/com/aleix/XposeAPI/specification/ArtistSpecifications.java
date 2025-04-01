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

public class ArtistSpecifications {
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

    private static String removeAccents(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }
}
