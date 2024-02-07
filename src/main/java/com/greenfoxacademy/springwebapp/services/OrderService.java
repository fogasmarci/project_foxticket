package com.greenfoxacademy.springwebapp.services;

import com.google.zxing.WriterException;
import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderedItemDTO;
import com.greenfoxacademy.springwebapp.models.OrderedItem;

import java.io.IOException;
import java.util.List;

public interface OrderService {
  OrderListDTO listAllPurchases();

  OrderedItem saveOrder(OrderedItem orderedItem);

  List<OrderedItemDTO> mapOrdersIntoListOfOrderDTOs(List<OrderedItem> orderedItems);

  OrderedItemDTO activateItem(Long orderId);

  byte[] getQrCode(Long orderId) throws IOException, WriterException;
}
