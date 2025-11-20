package org.example.aui.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cities")
public class City {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column
    private String city;

    @Column
    private String state;

    @Column 
    private String country;
    
    @OneToMany(mappedBy = "city")
    private List<Address> addresses = new ArrayList<>();

}
