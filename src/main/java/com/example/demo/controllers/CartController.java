package com.example.demo.controllers;

import java.util.stream.IntStream;

import com.example.demo.errorhandling.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@Slf4j
@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
		log.info("POST cart/addToCart");
		log.info("add item to cart: " + request);

		User user = userRepository.findByUsername(request.getUsername()).orElseThrow(()->{return new EntityNotFoundException(User.class,"username", request.getUsername());});

		Item item = itemRepository.findById(request.getItemId()).orElseThrow(()->{return new EntityNotFoundException(Item.class,"id", request.getItemId().toString());});

		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.addItem(item));
		cartRepository.save(cart);

		log.info(cart.toString());
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
		log.info("POST cart/removeFromCart");
		log.info("remove item from cart: " + request);

		User user = userRepository.findByUsername(request.getUsername()).orElseThrow(()->{return new EntityNotFoundException(User.class,"username", request.getUsername());});

		Item item = itemRepository.findById(request.getItemId()).orElseThrow(()->{return new EntityNotFoundException(Item.class,"id", request.getItemId().toString());});;


		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.removeItem(item));
		cartRepository.save(cart);

		log.info(cart.toString());
		return ResponseEntity.ok(cart);
	}
		
}
