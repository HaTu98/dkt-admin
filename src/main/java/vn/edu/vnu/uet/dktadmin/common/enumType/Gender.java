package vn.edu.vnu.uet.dktadmin.common.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE("Nam", 1),
    FEMALE("Nữ", 2);

    private final String name;
    private final Integer value;
    
    public static Gender getValue(String value) {
        switch (value) {
            case "Nam" : return MALE;
            case "Nữ": return FEMALE;
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }
}
