package com.example.one_vote_service.utils;

import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class QueryUtils {
    public static String buildLikeExp(final String query) {
        if (query == null || !H.isTrue(query.trim())) {
            return null;
        }
        return "%" + query.trim().replaceAll("\\s+", "%") + "%";
    }

    public static <T> Predicate buildLikeFilter(final Root<T> root, final CriteriaBuilder cb, final String keyword, final String... fieldNames) {
        final String likeExp = buildLikeExp(keyword);
        if (!H.isTrue(likeExp) || !H.isTrue(fieldNames)) {
            return cb.and();
        }
        return cb.or(H.collect(Arrays.asList(fieldNames), (index, fieldName) -> cb.like(cb.upper(root.get(fieldName)), likeExp.toUpperCase())).toArray(new Predicate[0]));
    }

    public static <T, P> Predicate buildNotEqFilter(final Root<T> root, final CriteriaBuilder cb, final String fieldName, final P value) {
        return H.isTrue(value) ? cb.notEqual(root.get(fieldName), value) : cb.and();
    }

    public static <T> Predicate buildLikeFilter(final Root<T> root, final CriteriaBuilder cb, final String keyword, final Path... paths) {
        final String likeExp = buildLikeExp(keyword);
        if (!H.isTrue(likeExp)) {
            return cb.and();
        }
        return cb.or(H.collect(Arrays.asList(paths), (index, path) -> cb.like(cb.upper((Expression<String>) path), likeExp.toUpperCase())).toArray(new Predicate[0]));
    }

    public static <T> Predicate buildSimpleLikeFilter(final Root<T> root, final CriteriaBuilder cb, final String keyword, final String... fieldNames) {
        if (!H.isTrue(keyword) || !H.isTrue(fieldNames)) {
            return cb.and();
        }
        return cb.or(H.collect(Arrays.asList(fieldNames), (index, fieldName) -> cb.like(cb.upper(root.get(fieldName)), ("%" + keyword + "%").toUpperCase())).toArray(new Predicate[0]));
    }

    public static <T, P> Predicate buildEqFilter(final Root<T> root, final CriteriaBuilder cb, final String fieldName, final P value) {
        return H.isTrue(value) ? cb.equal(root.get(fieldName), value) : cb.and();
    }

    public static <T, P> Predicate buildEqFilterNull(final Root<T> root, final CriteriaBuilder cb, final String fieldName) {
        return cb.isNull(root.get(fieldName));
    }

    public static <T, P> Predicate isBooleanStatus(final Root<T> root, final CriteriaBuilder cb, final Boolean value) {
        if (value == null) {
            return cb.isTrue(cb.literal(true)); // Trường hợp không truyền giá trị, trả về true để lấy tất cả
        } else {
            return cb.equal(root.get("status"), value); // Sử dụng giá trị truyền vào để so sánh
        }
    }

    public static <T, P> Predicate buildEqFilter(final Root<T> root, final CriteriaBuilder cb, final Path fieldName, final P value) {
        return H.isTrue(value) ? cb.equal(fieldName, value) : cb.and();
    }

    public static <T> Predicate buildIsDeleteFilter(final Root<T> root, final CriteriaBuilder cb) {
        return cb.equal(root.get("isDelete"), false);
    }

    public static <T, P> Predicate buildInFilter(final Root<T> root, final CriteriaBuilder cb, final String fieldName, final Collection<P> values) {
        return H.isTrue(values) ? root.get(fieldName).in(values) : cb.and();
    }

    public static <T, P> Predicate buildInFilter(final Root<T> root, final CriteriaBuilder cb, Path path, final Collection<P> values) {
        return H.isTrue(values) ? path.in(values) : cb.and();
    }

    public static <T, P> Predicate buildInFilter(final Root<T> root, final CriteriaBuilder cb, final String fieldName, String values) {
        return H.isTrue(values) ? root.get(fieldName).in((Object[]) values.split(",")) : cb.and();
    }

    public static <T, P> Predicate buildInFilter(final Root<T> root, final CriteriaBuilder cb, final String fieldName, Subquery<?> subquery) {
        return H.isTrue(subquery) ? root.get(fieldName).in(subquery) : cb.and();
    }

    public static <T, P> Predicate buildNotInFilter(final Root<T> root, final CriteriaBuilder cb, final String fieldName, final Collection<P> values) {
        return H.isTrue(values) ? root.get(fieldName).in(values).not() : cb.and();
    }

    public static <T, P> Predicate buildBetweenFilter(final Root<T> root, final CriteriaBuilder cb, final String fieldName, Date fromDate, Date toDate) {
        Path<Date> dateField = root.get(fieldName);
        return H.isTrue(fromDate) && H.isTrue(toDate) ? cb.between(dateField, fromDate, toDate) : cb.and();
    }

    public static <T, P> Predicate buildEqEnumFilter(final Root<T> root, final CriteriaBuilder cb, final String fieldName, final Enum value) {
        return H.isTrue(value) ? cb.equal(root.get(fieldName), value) : cb.and();
    }
}
