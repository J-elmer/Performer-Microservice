package com.example.se_track_performer.controller.DTO;

import javax.validation.constraints.NotEmpty;

public final class UpdatePerformerDTO {

    @NotEmpty(message = "Name of performer required in order to update")
    private final String name;
    private final int age;
    private final String style;

    public UpdatePerformerDTO(String name, int age, String style) {
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
