package com.jamf.regatta.data.query;

import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class RegattaOperationChain {

    private final Set<PathAndValue> sismember = new LinkedHashSet<>();
    private final Set<PathAndValue> orSismember = new LinkedHashSet<>();

    public void sismember(String path, Object value) {
        sismember(new PathAndValue(path, value));
    }

    public void sismember(PathAndValue pathAndValue) {
        sismember.add(pathAndValue);
    }

    public Set<PathAndValue> getSismember() {
        return sismember;
    }

    public void orSismember(PathAndValue pathAndValue) {
        orSismember.add(pathAndValue);
    }

    public void orSismember(Collection<PathAndValue> next) {
        orSismember.addAll(next);
    }

    public Set<PathAndValue> getOrSismember() {
        return orSismember;
    }

    public boolean isEmpty() {
        return sismember.isEmpty() && orSismember.isEmpty();
    }

    public static class PathAndValue {

        private final String path;
        private final Collection<Object> values;

        public PathAndValue(String path, Object singleValue) {

            this.path = path;
            this.values = Collections.singleton(singleValue);
        }

        public PathAndValue(String path, Collection<Object> values) {

            this.path = path;
            this.values = values != null ? values : Collections.emptySet();
        }

        public boolean isSingleValue() {
            return values.size() == 1;
        }

        public String getPath() {
            return path;
        }

        public Collection<Object> values() {
            return values;
        }

        public Object getFirstValue() {
            return values.isEmpty() ? null : values.iterator().next();
        }

        @Override
        public String toString() {
            return path + ":" + (isSingleValue() ? getFirstValue() : values);
        }

        @Override
        public boolean equals(Object o) {

            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PathAndValue that = (PathAndValue) o;

            if (!ObjectUtils.nullSafeEquals(path, that.path)) {
                return false;
            }
            return ObjectUtils.nullSafeEquals(values, that.values);
        }

        @Override
        public int hashCode() {
            int result = ObjectUtils.nullSafeHashCode(path);
            result = 31 * result + ObjectUtils.nullSafeHashCode(values);
            return result;
        }
    }
}
