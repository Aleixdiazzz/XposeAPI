package com.aleix.XposeAPI.specification;

import com.aleix.XposeAPI.model.Artist;
import com.aleix.XposeAPI.model.Serie;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class SerieSpecifications {
    public static Specification<Serie> filterSeries(String name, String artistId, Boolean active) {
        return (Root<Serie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("unaccent", String.class, root.get("name"))),
                        "%" + removeAccents(name.toLowerCase()) + "%"
                ));
            }
            if (artistId != null && !artistId.isEmpty()) {
                Join<Serie, Artist> join = root.join("artists");
                predicates.add(criteriaBuilder.equal(join.get("id"), Long.parseLong(artistId)));
            }
            if (Boolean.TRUE.equals(active)) {
                predicates.add(criteriaBuilder.isTrue(root.get("active")));
            }
            if(Boolean.FALSE.equals(active)) {
                predicates.add(criteriaBuilder.isFalse(root.get("active")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String removeAccents(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }
}
