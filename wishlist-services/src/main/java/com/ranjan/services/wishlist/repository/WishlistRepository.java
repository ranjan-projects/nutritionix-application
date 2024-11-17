package com.ranjan.services.wishlist.repository;

import com.ranjan.services.wishlist.entity.WishlistEntity;
import com.ranjan.services.wishlist.entity.WishlistId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<WishlistEntity, WishlistId> {

    List<WishlistEntity> findByUsername(String username);

}
