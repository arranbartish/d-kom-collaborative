package com.hybris.util;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class ReflectiveBean {

    private static final Boolean DEFAULT_TEST_TRANSIENT_SETTINGS = TRUE;

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, DEFAULT_TEST_TRANSIENT_SETTINGS);
    }

    @Override
    public boolean equals(Object obj) {
        return reflectionEquals(this, obj, DEFAULT_TEST_TRANSIENT_SETTINGS);
    }

    @Override
    public String toString() {
        return reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
