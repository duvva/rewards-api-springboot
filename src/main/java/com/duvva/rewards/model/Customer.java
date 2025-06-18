package com.duvva.rewards.model;

public class Customer {
    private Long id;
    private String name;
    public Customer() {
        // Default constructor needed for Jackson
    }
    public Customer(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
}
