package io.practice.secretprovider.repositories;

import io.practice.secretprovider.model.SecretEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SecretRepository extends JpaRepository<SecretEntity, UUID> {
}
