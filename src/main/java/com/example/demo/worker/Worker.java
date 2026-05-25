package com.example.demo.worker;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(
    name = "workers",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_worker_phone", columnNames = "phone")
    }
)
public class Worker {

    @Id
    @SequenceGenerator(
            name = "worker_sequence",
            sequenceName = "worker_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "worker_sequence"
    )
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;


    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be exactly 10 digits")
    @Column(nullable = false, unique = true)
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Designation designation;

    @NotNull
    @Column(nullable = false)
    private Double dailyWageRate;

    @Column(nullable = false)
    private Boolean active = true;

    public Worker(String name, String phone, Designation designation, Double dailyWageRate) {
        this.name = name;
        this.phone = phone;
        this.designation = designation;
        this.dailyWageRate = dailyWageRate;
    }
}