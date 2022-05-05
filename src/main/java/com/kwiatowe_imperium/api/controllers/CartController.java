package com.kwiatowe_imperium.api.controllers;


import com.kwiatowe_imperium.api.models.OrderItemRequest;
import com.kwiatowe_imperium.api.servies.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class CartController {

    private CartService cartService;

    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    public ResponseEntity<?> addToCart(@RequestBody OrderItemRequest productItem, @RequestHeader("Authorization") String jwt){
        return cartService.addToCart(productItem,jwt);
    }
    @RequestMapping(value = "/cart/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteFromCart(@PathVariable Long id, @RequestHeader("Authorization") String jwt){
        return cartService.deleteForCart(id,jwt);
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public ResponseEntity<?> showCart(@RequestHeader("Authorization") String jwt,@RequestHeader("accept-language") String lang){
        return cartService.showCart(jwt, lang);
    }

    @RequestMapping(value = "/cart", method = RequestMethod.DELETE)
    public ResponseEntity<?> dropCart(@RequestHeader("Authorization") String jwt){
        return cartService.dropCart(jwt);
    }


}
