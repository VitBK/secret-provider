package io.practice.secretprovider.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "secrets")
@Getter
@Setter
@NoArgsConstructor
public class SecretEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "encoded_secret")
    private String encodedSecret;

    public SecretEntity(String encodedSecret) {
        this.encodedSecret = encodedSecret;
    }
}
