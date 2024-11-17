package com.ranjan.services.wishlist.controller;

import com.ranjan.services.wishlist.entity.WishlistEntity;
import com.ranjan.services.wishlist.exception.CodedException;
import com.ranjan.services.wishlist.model.Wishlist;
import com.ranjan.services.wishlist.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@Tag(name = "Wishlist Services", description = "Operations related to wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping
    @Operation(summary = "Get all wishlist", description = "Retrieve a list of all wishlist")
    public ResponseEntity<List<Wishlist>> getAllWishlist(@RequestHeader("username") String username) {
        return ResponseEntity.ok(wishlistService.getAllWishlist(username));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get wishlist by ID",
            description = "Retrieve a wishlist by it's ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Wishlist details", content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WishlistEntity.class))),
                    @ApiResponse(responseCode = "404", description = "Wishlist not found"),
                    @ApiResponse(responseCode = "500", description = "Something's gone wrong")
            }
    )
    public ResponseEntity<WishlistEntity> getWishlistById(@PathVariable String foodName, @RequestHeader("username") String username) {
        return ResponseEntity.ok(wishlistService.getWishlistById(foodName, username));
    }

    @PostMapping
    @Operation(summary = "Add a new wishlist", description = "Add a new wishlist to the system")
    public ResponseEntity<WishlistEntity> addToWishlist(@RequestBody Wishlist wishlist, @RequestHeader("username") String username) {
        wishlist.setUsername(username);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlistService.addToWishlist(wishlist));
    }

    @PutMapping
    @Operation(summary = "Update an existing wishlist", description = "Update details of an existing wishlist by it's ID")
    public ResponseEntity<WishlistEntity> updateWishlist(@RequestBody WishlistEntity wishlistEntity) {
        return ResponseEntity.status(HttpStatus.OK).body(wishlistService.updateWishlist(wishlistEntity));
    }

    @DeleteMapping("/{foodName}")
    @Operation(summary = "Delete a wishlist", description = "Remove a wishlist from the system by foodName for the user")
    public ResponseEntity<WishlistEntity> deleteWishlist(@PathVariable String foodName, @RequestHeader("username") String username) {
        return ResponseEntity.status(HttpStatus.OK).body(wishlistService.deleteWishlist(foodName, username));
    }

    @ExceptionHandler(CodedException.class)
    public ResponseEntity<CodedException> handleCodedException(CodedException ex) {
        return new ResponseEntity<>(ex, ex.getResponseCode());
    }
}
