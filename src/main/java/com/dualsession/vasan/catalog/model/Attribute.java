package com.dualsession.vasan.catalog.model;

import jakarta.persistence.*;

@Entity
@Table(name = "attributes")
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private boolean filterable;
    private boolean searchable;
}
