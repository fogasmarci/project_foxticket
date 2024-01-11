package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartListDTO;
import com.greenfoxacademy.springwebapp.dtos.CartProductDTO;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdInvalidException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdMissingException;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
  public void addProductToCart(Cart cart, Long productId) {
    if (productId == null) {
      throw new ProductIdMissingException();
    }

    Optional<Product> optionalProduct = productService.findProductById(productId);
    if (optionalProduct.isPresent()) {
      Product productToAdd = optionalProduct.get();
      cart.addProduct(productToAdd);
      cartRepository.save(cart);
    } else {
      throw new ProductIdInvalidException();
    }
  }

  @Override
  public Cart findCartByUser(User user) {
    return cartRepository.findByUser(user);
  }

  @Override
  public List<Product> findProductsInCart(Cart cart) {
    return cart.getProducts();
  }

  @Override
  public CartListDTO createCartListDTO(List<Product> productsInCart) {
    List<CartProductDTO> cartProductDTOs = productsInCart.stream()
        .map(p -> new CartProductDTO(p.getId(), p.getName(), p.getPrice()))
        .collect(Collectors.toList());
    return new CartListDTO(cartProductDTOs);
  }
}
