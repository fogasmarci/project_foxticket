package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartListDTO;
import com.greenfoxacademy.springwebapp.dtos.CartProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductIdDTO;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdInvalidException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdMissingException;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.greenfoxacademy.springwebapp.models.CartSpecifications.hasUserId;

@Service
public class CartServiceImpl implements CartService {
  private CartRepository cartRepository;
  private ProductService productService;

  @Autowired
  public CartServiceImpl(CartRepository cartRepository, ProductService productService) {
    this.cartRepository = cartRepository;
    this.productService = productService;
  }

  @Override
  @Transactional
  public void addProductToCart(Cart cart, ProductIdDTO productIdDTO) {
    Long productId = productIdDTO.getProductId();
    if (productId == null) {
      throw new ProductIdMissingException();
    }

    int amount = productIdDTO.getAmount();
    if (amount <= 0) {
      return;
    }

    Optional<Product> optionalProduct = productService.findProductById(productId);
    if (optionalProduct.isPresent()) {
      Product productToAdd = optionalProduct.get();
      for (int i = 0; i < amount; i++) {
        cart.addProduct(productToAdd);
      }

      cartRepository.save(cart);

    } else {
      throw new ProductIdInvalidException();
    }
  }

  @Override
  public Cart findCartByUser(User user) {
    return cartRepository.findByUser(user);
  }

  public CartListDTO getCartWithProducts(Long userId) {
    Specification<Cart> specification = hasUserId(userId);
    List<Cart> carts = cartRepository.findAll(specification);

    List<CartProductDTO> productsInCart = carts.stream()
        .flatMap(cart -> cart.getProducts().stream()
            .map(CartProductDTO::new))
        .toList();

    return new CartListDTO(productsInCart);
  }
}
