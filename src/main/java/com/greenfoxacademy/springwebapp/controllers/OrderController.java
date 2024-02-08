package com.greenfoxacademy.springwebapp.controllers;

import com.google.zxing.WriterException;
import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderedItemDTO;
import com.greenfoxacademy.springwebapp.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<OrderedItemDTO> activatePurchasedItem(@PathVariable Long orderId) {
    return ResponseEntity.status(200).body(orderService.activateItem(orderId));
  }

  @GetMapping("/api/orders/{orderId}")
  public ResponseEntity<byte[]> getQrCode(@PathVariable Long orderId) throws IOException, WriterException {
    byte[] qrCodeBytes = orderService.getQrCode(orderId);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.IMAGE_PNG);
    headers.setContentLength(qrCodeBytes.length);

    return ResponseEntity.status(200).headers(headers).body(qrCodeBytes);
  }
}
