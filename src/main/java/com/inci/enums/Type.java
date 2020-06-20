package com.inci.enums;

import lombok.Getter;

@Getter
public enum Type {
    // PEH types
    E("E - emolientowa"),
    H("H - humentantowa"),
    P("P - proteinowa"),
    PE("PE - proteinowo-emolientowta"),
    PH("PH - proteinowo-humektantowa"),
    EH("EH - emolientowo-humentantowa"),
    EP("EP - emolientowo-proteinowa"),
    HE("HE - humektantowo-emolientowa"),
    HP("HP - humektantowo-proteinowa"),
    PEH("PEH - proteinowo-emolientowo-humektatnowa"),

    // detergent types
    HARSH_DETERGENT("Rypacz"),
    MILD_DETERGENT("Łagodny szampon"),

    // warnings
    ALLERGEN("wykryto conajmniej jeden alergen"),

    // CG
    CG("Kosmetyk jest zgodny z CG");

    private String value;

    private Type(String value) {
        this.value = value;
    }
}
