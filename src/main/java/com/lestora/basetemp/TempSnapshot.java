package com.lestora.basetemp;

public record TempSnapshot(
        float playerTemp,
        float coldOffset,
        float hotOffset
) {
}