package de.janetschel.haushaltsplan.backend.enums;

import lombok.Getter;

public enum Feedback {
    NO_FEEDBACK_GIVEN("NO_FEEDBACK_GIVEN"),
    GOOD("GOOD"),
    OKAY("OKAY"),
    BAD("BAD");

    @Getter
    private final String value;

    Feedback(String value) {
        this.value = value;
    }
}
