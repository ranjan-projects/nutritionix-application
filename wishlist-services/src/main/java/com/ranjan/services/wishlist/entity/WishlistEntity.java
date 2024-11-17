package com.ranjan.services.wishlist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "wishlist")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistEntity {

    @Id
    private WishlistId wishlistId;

    private String username;

    private String foodName;

    private String servingUnit;

    private Integer servingQty;

    private String tagId;

    private String tagName;

    private String commonType;

    private String photoUrl;

    private String locale;

}
