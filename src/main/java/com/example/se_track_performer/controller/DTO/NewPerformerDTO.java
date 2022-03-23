package com.example.se_track_performer.controller.DTO;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public final class NewPerformerDTO {

    @NotEmpty(message = "Name is mandatory")
    private final String name;
    @Positive(message = "Age must be provided and must be greater than 0")
    private final int age;
    @NotEmpty(message = "Style is mandatory")
    private final String style;

    public NewPerformerDTO(String name, int age, String style) {
        this.name = name;
        this.age = age;
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getStyle() {
        return style;
    }
}
