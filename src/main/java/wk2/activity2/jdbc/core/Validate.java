package wk2.activity2.jdbc.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "valid" })
public interface Validate {
    boolean isValid();
}
