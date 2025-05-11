package com.aleix.XposeAPI.specification;

import com.aleix.XposeAPI.model.Asset;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class AssetSpecifications {
    public static Specification<Asset> filterAssets(String name, String type, Boolean active,  String artistId, String serieId) {
        return (Root<Asset> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("unaccent", String.class, root.get("name"))),
                        "%" + removeAccents(name.toLowerCase()) + "%"
                ));
            }
            if (type != null && !type.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("unaccent", String.class, root.get("type"))),
                        "%" + removeAccents(type.toLowerCase()) + "%"
                ));
            }
            if (Boolean.TRUE.equals(active)) {
                predicates.add(criteriaBuilder.isTrue(root.get("active")));
            }
            if(Boolean.FALSE.equals(active)) {
                predicates.add(criteriaBuilder.isFalse(root.get("active")));
            }
            if (artistId != null && !artistId.isEmpty() && !artistId.equals("0")) {
                Join<Object, Object> authorsJoin = root.join("authors");
                predicates.add(criteriaBuilder.equal(authorsJoin.get("id"), Long.parseLong(artistId)));
            }
            if (serieId != null && !serieId.isEmpty() && !serieId.equals("0")) {
                Join<Object, Object> seriesJoin = root.join("series");
                predicates.add(criteriaBuilder.equal(seriesJoin.get("id"), Long.parseLong(serieId)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String removeAccents(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }
}
