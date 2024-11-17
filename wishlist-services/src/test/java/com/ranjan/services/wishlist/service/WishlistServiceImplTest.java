package com.ranjan.services.wishlist.service;

import com.ranjan.services.wishlist.entity.WishlistEntity;
import com.ranjan.services.wishlist.entity.WishlistId;
import com.ranjan.services.wishlist.exception.CodedException;
import com.ranjan.services.wishlist.model.Wishlist;
import com.ranjan.services.wishlist.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.ranjan.services.wishlist.exception.ExceptionEnum.WISHLIST_ALREADY_EXIST;
import static com.ranjan.services.wishlist.exception.ExceptionEnum.WISHLIST_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistServiceImplTest {

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    @Mock
    private WishlistRepository wishlistRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllWishlist() {
        // Arrange
        WishlistEntity wishlistEntity1 = new WishlistEntity();
        wishlistEntity1.setFoodName("Apple");
        WishlistEntity wishlistEntity2 = new WishlistEntity();
        wishlistEntity2.setFoodName("Banana");

        List<WishlistEntity> wishlistEntities = List.of(wishlistEntity1, wishlistEntity2);
        when(wishlistRepository.findByUsername(anyString())).thenReturn(wishlistEntities);

        // Act
        List<Wishlist> result = wishlistService.getAllWishlist("user1");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Apple", result.get(0).getFoodName());
        verify(wishlistRepository, times(1)).findByUsername("user1");
    }

    @Test
    void testGetWishlistById_Success() {
        // Arrange
        WishlistId wishlistId = new WishlistId("user1", "Apple");
        WishlistEntity wishlistEntity = new WishlistEntity();
        wishlistEntity.setFoodName("Apple");

        when(wishlistRepository.findById(any(WishlistId.class))).thenReturn(Optional.of(wishlistEntity));

        // Act
        WishlistEntity result = wishlistService.getWishlistById("Apple", "user1");

        // Assert
        assertNotNull(result);
        assertEquals("Apple", result.getFoodName());
        verify(wishlistRepository, times(1)).findById(wishlistId);
    }

    @Test
    void testGetWishlistById_NotFound() {
        // Arrange
        WishlistId wishlistId = new WishlistId("user1", "Apple");
        when(wishlistRepository.findById(any(WishlistId.class))).thenReturn(Optional.empty());

        // Act & Assert
        CodedException exception = assertThrows(CodedException.class, () -> {
            wishlistService.getWishlistById("Apple", "user1");
        });

        assertEquals(WISHLIST_NOT_FOUND.getResponseCode(), exception.getResponseCode());
        verify(wishlistRepository, times(1)).findById(wishlistId);
    }

    @Test
    void testAddToWishlist_Success() {
        // Arrange
        Wishlist wishlist = new Wishlist();
        wishlist.setFoodName("Apple");
        wishlist.setUsername("user1");

        WishlistId wishlistId = new WishlistId("user1", "Apple");
        when(wishlistRepository.findById(any(WishlistId.class))).thenReturn(Optional.empty());
        WishlistEntity savedEntity = new WishlistEntity();
        savedEntity.setFoodName("Apple");

        when(wishlistRepository.save(any(WishlistEntity.class))).thenReturn(savedEntity);

        // Act
        WishlistEntity result = wishlistService.addToWishlist(wishlist);

        // Assert
        assertNotNull(result);
        assertEquals("Apple", result.getFoodName());
        verify(wishlistRepository, times(1)).findById(wishlistId);
        verify(wishlistRepository, times(1)).save(any(WishlistEntity.class));
    }

    @Test
    void testAddToWishlist_AlreadyExists() {
        // Arrange
        Wishlist wishlist = new Wishlist();
        wishlist.setFoodName("Apple");
        wishlist.setUsername("user1");

        WishlistId wishlistId = new WishlistId("user1", "Apple");
        WishlistEntity existingWishlist = new WishlistEntity();
        existingWishlist.setFoodName("Apple");

        when(wishlistRepository.findById(any(WishlistId.class))).thenReturn(Optional.of(existingWishlist));

        // Act & Assert
        CodedException exception = assertThrows(CodedException.class, () -> {
            wishlistService.addToWishlist(wishlist);
        });

        assertEquals(WISHLIST_ALREADY_EXIST.getResponseCode(), exception.getResponseCode());
        verify(wishlistRepository, times(1)).findById(wishlistId);
        verify(wishlistRepository, times(0)).save(any(WishlistEntity.class));
    }

    @Test
    void testUpdateWishlist_Success() {
        // Arrange
        WishlistEntity wishlistEntity = new WishlistEntity();
        wishlistEntity.setWishlistId(new WishlistId("user1", "Apple"));

        when(wishlistRepository.findById(any(WishlistId.class))).thenReturn(Optional.of(wishlistEntity));
        when(wishlistRepository.save(any(WishlistEntity.class))).thenReturn(wishlistEntity);

        // Act
        WishlistEntity result = wishlistService.updateWishlist(wishlistEntity);

        // Assert
        assertNotNull(result);
        assertEquals(wishlistEntity, result);
        verify(wishlistRepository, times(1)).findById(wishlistEntity.getWishlistId());
        verify(wishlistRepository, times(1)).save(wishlistEntity);
    }

    @Test
    void testUpdateWishlist_NotFound() {
        // Arrange
        WishlistEntity wishlistEntity = new WishlistEntity();
        wishlistEntity.setWishlistId(new WishlistId("user1", "Apple"));

        when(wishlistRepository.findById(any(WishlistId.class))).thenReturn(Optional.empty());

        // Act & Assert
        CodedException exception = assertThrows(CodedException.class, () -> {
            wishlistService.updateWishlist(wishlistEntity);
        });

        assertEquals(WISHLIST_NOT_FOUND.getResponseCode(), exception.getResponseCode());
        verify(wishlistRepository, times(1)).findById(wishlistEntity.getWishlistId());
        verify(wishlistRepository, times(0)).save(any(WishlistEntity.class));
    }

    @Test
    void testDeleteWishlist_Success() {
        // Arrange
        WishlistId wishlistId = new WishlistId("user1", "Apple");
        WishlistEntity wishlistEntity = new WishlistEntity();
        wishlistEntity.setWishlistId(wishlistId);

        when(wishlistRepository.findById(any(WishlistId.class))).thenReturn(Optional.of(wishlistEntity));

        // Act
        WishlistEntity result = wishlistService.deleteWishlist("Apple", "user1");

        // Assert
        assertNotNull(result);
        verify(wishlistRepository, times(1)).findById(wishlistId);
        verify(wishlistRepository, times(1)).deleteById(wishlistId);
    }

    @Test
    void testDeleteWishlist_NotFound() {
        // Arrange
        WishlistId wishlistId = new WishlistId("user1", "Apple");
        when(wishlistRepository.findById(any(WishlistId.class))).thenReturn(Optional.empty());

        // Act & Assert
        CodedException exception = assertThrows(CodedException.class, () -> {
            wishlistService.deleteWishlist("Apple", "user1");
        });

        assertEquals(WISHLIST_NOT_FOUND.getResponseCode(), exception.getResponseCode());
        verify(wishlistRepository, times(1)).findById(wishlistId);
        verify(wishlistRepository, times(0)).deleteById(any(WishlistId.class));
    }
}
