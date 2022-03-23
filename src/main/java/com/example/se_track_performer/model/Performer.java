package com.example.se_track_performer.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Performer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name="performer_id")
    private Long id;

    @Column(unique = true)
    private String name;
    private int age;
    private String style;

    public Performer() {

    }

    public Performer(String name, int age, String style) {
        this.name = name;
        this.age = age;
        this.style = style;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return "Performer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", style='" + style + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, style);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Performer performer = (Performer) obj;
        return name.equals(performer.getName());
    }
}
