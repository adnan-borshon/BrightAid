package com.example.Bright_Aid.Entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.usertype.UserType;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class UserProfile extends BaseEntity {

    @Id
    @Column(name = "profile_id")
    @EqualsAndHashCode.Include
    private Integer profileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @MapsId
    @NotNull
    private User user;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Lob
    @Column(name = "address")
    private String address;



    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;



    public enum UserStatus {
        ACTIVE, INACTIVE, PENDING, SUSPENDED
    }
}
