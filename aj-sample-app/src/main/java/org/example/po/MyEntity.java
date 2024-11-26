package org.example.po;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "entity")
public class MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
