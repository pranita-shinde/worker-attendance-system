package com.example.demo.site;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "sites")
public class Site {

    @Id
    @SequenceGenerator(
            name = "site_sequence",
            sequenceName = "site_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "site_sequence"
    )
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String location;

    @Column(nullable = false)
    private Boolean active = true;
}