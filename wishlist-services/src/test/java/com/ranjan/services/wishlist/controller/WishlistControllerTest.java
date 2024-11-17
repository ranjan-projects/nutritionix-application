package com.ranjan.services.wishlist.controller;

import com.ranjan.services.wishlist.entity.WishlistEntity;
import com.ranjan.services.wishlist.exception.CodedException;
import com.ranjan.services.wishlist.model.Wishlist;
import com.ranjan.services.wishlist.service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class WishlistControllerTest {

    @InjectMocks
    private WishlistController wishlistController;

    @Mock
    private WishlistService wishlistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllWishlist() {
        // Arrange
        Wishlist wishlist1 = new Wishlist();
        wishlist1.setFoodName("Apple");
        wishlist1.setUsername("user1");

        Wishlist wishlist2 = new Wishlist();
        wishlist2.setFoodName("Banana");
        wishlist2.setUsername("user1");

        List<Wishlist> wishlistList = Arrays.asList(wishlist1, wishlist2);
        when(wishlistService.getAllWishlist(anyString())).thenReturn(wishlistList);

        // Act
        ResponseEntity<List<Wishlist>> response = wishlistController.getAllWishlist("user1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(wishlistList, response.getBody());
        verify(wishlistService, times(1)).getAllWishlist("user1");
    }

    @Test
    void testGetWishlistById() {
        // Arrange
        WishlistEntity wishlistEntity = new WishlistEntity();
        wishlistEntity.setFoodName("Apple");

        when(wishlistService.getWishlistById(anyString(), anyString())).thenReturn(wishlistEntity);

        // Act
        ResponseEntity<WishlistEntity> response = wishlistController.getWishlistById("Apple", "user1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(wishlistEntity, response.getBody());
        verify(wishlistService, times(1)).getWishlistById("Apple", "user1");
    }

    @Test
    void testAddToWishlist() {
        // Arrange
        Wishlist wishlist = new Wishlist();
        wishlist.setFoodName("Apple");
        wishlist.setUsername("user1");

        WishlistEntity wishlistEntity = new WishlistEntity();
        wishlistEntity.setFoodName("Apple");

        when(wishlistService.addToWishlist(any(Wishlist.class))).thenReturn(wishlistEntity);

        // Act
        ResponseEntity<WishlistEntity> response = wishlistController.addToWishlist(wishlist, "user1");

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(wishlistEntity, response.getBody());
        verify(wishlistService, times(1)).addToWishlist(any(Wishlist.class));
    }

    @Test
    void testUpdateWishlist() {
        // Arrange
        WishlistEntity wishlistEntity = new WishlistEntity();
        wishlistEntity.setFoodName("Apple");

        when(wishlistService.updateWishlist(any(WishlistEntity.class))).thenReturn(wishlistEntity);

        // Act
        ResponseEntity<WishlistEntity> response = wishlistController.updateWishlist(wishlistEntity);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(wishlistEntity, response.getBody());
        verify(wishlistService, times(1)).updateWishlist(any(WishlistEntity.class));
    }

    @Test
    void testDeleteWishlist() {
        // Arrange
        WishlistEntity wishlistEntity = new WishlistEntity();
        wishlistEntity.setFoodName("Apple");

        when(wishlistService.deleteWishlist(anyString(), anyString())).thenReturn(wishlistEntity);

        // Act
        ResponseEntity<WishlistEntity> response = wishlistController.deleteWishlist("Apple", "user1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(wishlistEntity, response.getBody());
        verify(wishlistService, times(1)).deleteWishlist("Apple", "user1");
    }

    @Test
    void testHandleCodedException() {
        // Arrange
        CodedException exception = new CodedException(HttpStatus.BAD_REQUEST, "1001", "Error occurred");

        // Act
        ResponseEntity<CodedException> response = wishlistController.handleCodedException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(exception, response.getBody());
    }
}

