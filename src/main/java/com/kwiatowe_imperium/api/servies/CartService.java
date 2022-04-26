package com.kwiatowe_imperium.api.servies;


import com.kwiatowe_imperium.api.models.*;
import com.kwiatowe_imperium.api.repo.CartRepository;
import com.kwiatowe_imperium.api.repo.OrderItemRepository;
import com.kwiatowe_imperium.api.repo.ProductRepository;
import com.kwiatowe_imperium.api.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class CartService {

    UserDetailsServices userDetailsServices;

    UserRepository repository;

    ProductRepository productRepository;

    OrderItemRepository orderItemRepository;

    CartRepository cartRepository;

    private OrderItem getProduct(ProductRequest request){
        Product product = null;
        OrderItem item = new OrderItem();

        if(productRepository.existsById(request.getProduct())){
            product = productRepository.getById(request.getProduct());
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            orderItemRepository.save(item);
        }
        else{
            return null;
        }

        return item;
    }

    public ResponseEntity<?> dropCart(String jwt){
        UserModel userModel = userDetailsServices.jwtUser(jwt);
        cartRepository.delete(userModel.getCart());
        userModel.setCart(new Cart());
        repository.save(userModel);
        return new ResponseEntity<>("cart droped",HttpStatus.OK);
    }

    public ResponseEntity<?> showCart(String jwt,String lang){
        UserModel userModel = userDetailsServices.jwtUser(jwt);
        List<OrderItem> orderItemList =  userModel.getCart().getProducts();
        List<ProductDTO> productDTOList = new LinkedList<>();
        for(int i =0;i<orderItemList.size();i++){
            productDTOList.add(orderItemList.get(i).getDTO(lang));
        }
        return new ResponseEntity<>(productDTOList,HttpStatus.OK);
    }

    public ResponseEntity<?> addToCart(ProductRequest productItem, String jwt) {
        UserModel userModel = userDetailsServices.jwtUser(jwt);
        OrderItem product = getProduct(productItem);
        if(product == null){
            return new ResponseEntity<>("product not found",HttpStatus.NOT_FOUND);
        }
        if(userModel.getCart() == null){
            userModel.setCart(new Cart());
        }

        product.setCart(userModel.getCart());
        repository.save(userModel);
        return new ResponseEntity<>(userModel,HttpStatus.OK);
    }

}
