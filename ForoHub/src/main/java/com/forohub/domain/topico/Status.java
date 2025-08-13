package com.forohub.domain.topico;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Status {
    ABIERTO,
    RESUELTO,
    CERRADO,
    SPAM;

    public static String valoresPermitidos() {
        return Arrays.stream(Status.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
