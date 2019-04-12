package wk2.activity2.jdbc.models;

import wk2.activity2.jdbc.core.Validate;

public class GetStudentRequestModel implements Validate {
    String name;

    public GetStudentRequestModel() { }

    public GetStudentRequestModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isValid() {
        return (name.length() > 0);
    }
}
