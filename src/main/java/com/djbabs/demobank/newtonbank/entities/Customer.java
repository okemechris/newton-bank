package com.djbabs.demobank.newtonbank.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Column(nullable = false, length = 15,unique = true)
    private String bvn;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdated;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdded;


    @PrePersist
    private void setCreatedAt() {
        dateAdded = new Date();
    }

    @PreUpdate
    private void setUpdatedAt() {
        dateUpdated = new Date();
    }

    @Override
    public boolean equals(Object customer) {
        return this.id.equals(((Customer)customer).getId()) ;

    }

    @Override
    public String toString(){
        return String.format("%s %s", lastName,firstName);
    }

}
