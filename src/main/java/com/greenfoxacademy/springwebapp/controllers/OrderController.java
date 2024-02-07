package com.greenfoxacademy.springwebapp.controllers;

import com.google.zxing.WriterException;
import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.exceptions.order.AlreadyActiveException;
import com.greenfoxacademy.springwebapp.exceptions.order.NotMyOrderException;
import com.greenfoxacademy.springwebapp.exceptions.producttype.InvalidProductTypeException;
import com.greenfoxacademy.springwebapp.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class OrderController {
  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("/api/orders")
  public ResponseEntity<OrderListDTO> listAllPurchasedItems() {
    return ResponseEntity.status(200).body(orderService.listAllPurchases());
  }

  @PatchMapping("/api/orders/{orderId}")
  public ResponseEntity<?> activatePurchasedItem(@PathVariable Long orderId) {
    try {
      return ResponseEntity.status(200).body(orderService.activateItem(orderId));
    } catch (InvalidProductTypeException | NotMyOrderException | AlreadyActiveException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @GetMapping("/api/orders/{orderId}")
  public ResponseEntity<?> getQrCode(@PathVariable Long orderId) {
    try {
      byte[] qrCodeBytes = orderService.getQrCode(orderId);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.IMAGE_PNG);
      headers.setContentLength(qrCodeBytes.length);

      return ResponseEntity.status(200).headers(headers).body(qrCodeBytes);
    } catch (IOException | WriterException e) {
      return ResponseEntity.status(500).body(new ErrorMessageDTO("QR code creation failed"));
    } catch (UsernameNotFoundException | NotMyOrderException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}
