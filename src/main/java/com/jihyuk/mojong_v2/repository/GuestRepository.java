package com.jihyuk.mojong_v2.repository;

import com.jihyuk.mojong_v2.model.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestRepository extends JpaRepository<Guest, UUID> {
    Optional<Guest> findByGuestname(String guestname);
}
