package pl.dreilt.basicspringmvcapp.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AppUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String visibleName;
    private String description;

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

    public String getVisibleName() {
        return visibleName;
    }

    public void setVisibleName(String visibleName) {
        this.visibleName = visibleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AppUserRole{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", visibleName='" + visibleName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
