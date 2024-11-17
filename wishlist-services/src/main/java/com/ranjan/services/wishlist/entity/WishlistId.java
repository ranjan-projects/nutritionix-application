package com.ranjan.services.wishlist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class WishlistId implements Serializable {

    private String username;

    private String foodName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishlistId that = (WishlistId) o;
        return Objects.equals(username, that.username) && Objects.equals(foodName, that.foodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, foodName);
    }
}
