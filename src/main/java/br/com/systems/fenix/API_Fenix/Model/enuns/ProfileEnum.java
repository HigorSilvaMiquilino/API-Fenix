package br.com.systems.fenix.API_Fenix.Model.enuns;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProfileEnum {

    ADMIN(1, "ROLE_ADMIN"),
    USER(2, "ROLE_USER");

    private Integer code;
    private String description;

    public static ProfileEnum toEnum(Integer code) {

        if (Objects.isNull(code))
            return null;

        for (ProfileEnum enumeration : ProfileEnum.values()) {
            if (code.equals(enumeration.getCode()))
                return enumeration;
        }

        throw new IllegalArgumentException("Invalid code: " + code);
    }

}
