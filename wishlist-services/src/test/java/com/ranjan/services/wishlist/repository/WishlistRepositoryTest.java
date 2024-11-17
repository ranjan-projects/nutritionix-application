package com.ranjan.services.wishlist.repository;

import com.ranjan.services.wishlist.entity.WishlistEntity;
import com.ranjan.services.wishlist.entity.WishlistId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistRepositoryTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUsername() {
        // Arrange
        WishlistEntity wishlist1 = new WishlistEntity();
        wishlist1.setUsername("user1");
        wishlist1.setFoodName("Apple");

        WishlistEntity wishlist2 = new WishlistEntity();
        wishlist2.setUsername("user1");
        wishlist2.setFoodName("Banana");

        List<WishlistEntity> wishlistEntities = List.of(wishlist1, wishlist2);

        when(wishlistRepository.findByUsername("user1")).thenReturn(wishlistEntities);

        // Act
        List<WishlistEntity> result = wishlistRepository.findByUsername("user1");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Apple", result.get(0).getFoodName());
        verify(wishlistRepository, times(1)).findByUsername("user1");
    }

    @Test
    void testFindById_Success() {
        // Arrange
        WishlistId wishlistId = new WishlistId("user1", "Apple");
        WishlistEntity wishlistEntity = new WishlistEntity();
        wishlistEntity.setWishlistId(wishlistId);

        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.of(wishlistEntity));

        // Act
        Optional<WishlistEntity> result = wishlistRepository.findById(wishlistId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(wishlistEntity, result.get());
        verify(wishlistRepository, times(1)).findById(wishlistId);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        WishlistId wishlistId = new WishlistId("user1", "Apple");
        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.empty());

        // Act
        Optional<WishlistEntity> result = wishlistRepository.findById(wishlistId);

        // Assert
        assertFalse(result.isPresent());
        verify(wishlistRepository, times(1)).findById(wishlistId);
    }

    @Test
    void testSaveWishlist() {
        // Arrange
        WishlistEntity wishlistEntity = new WishlistEntity();
        wishlistEntity.setUsername("user1");
        wishlistEntity.setFoodName("Apple");

        when(wishlistRepository.save(wishlistEntity)).thenReturn(wishlistEntity);

        // Act
        WishlistEntity result = wishlistRepository.save(wishlistEntity);

        // Assert
        assertNotNull(result);
        assertEquals("Apple", result.getFoodName());
        verify(wishlistRepository, times(1)).save(wishlistEntity);
    }

    @Test
    void testDeleteWishlist() {
        // Arrange
        WishlistId wishlistId = new WishlistId("user1", "Apple");
        doNothing().when(wishlistRepository).deleteById(wishlistId);

        // Act
        wishlistRepository.deleteById(wishlistId);

        // Assert
        verify(wishlistRepository, times(1)).deleteById(wishlistId);
    }
}
