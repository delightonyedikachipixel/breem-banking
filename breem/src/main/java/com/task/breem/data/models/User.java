package com.task.breem.data.models;

import com.task.breem.data.models.Enums.KycStatus;
import com.task.breem.data.models.Enums.RelationshipStatus;
import com.task.breem.data.models.Enums.Religion;
import com.task.breem.data.models.Enums.Title;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Title title;

    @Column(unique = true, nullable = false)
    private String username;

    private String firstName;

    @Enumerated(EnumType.STRING)
    private Religion religion;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String Bvn;

    // National Identification Number, as shown on the ERD
    @Column(unique = true)
    private String nin;

    @Column(unique = true, nullable = false)
    private String phoneNumber;
    @Transient
    private HashMap<String, String> password;

    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus;

    private String passportUrl;
    private String address;
    private String utilityBill;

    @Transient
    private NextOfKin nextOfKin;

    @Enumerated(EnumType.STRING)
    private RelationshipStatus relationshipStatus;

    private String dateOfBirth;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
