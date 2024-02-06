package com.greenfoxacademy.springwebapp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.exceptions.order.AlreadyActiveException;
import com.greenfoxacademy.springwebapp.exceptions.order.NotMyOrderException;
import com.greenfoxacademy.springwebapp.models.DurationConverter;
import com.greenfoxacademy.springwebapp.models.OrderedItem;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.greenfoxacademy.springwebapp.models.Status.Active;

@Service
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final UserService userService;

  @Autowired
  public OrderServiceImpl(OrderRepository orderRepository, UserService userService) {
    this.orderRepository = orderRepository;
    this.userService = userService;
  }

  @Override
  public OrderListDTO listAllPurchases() {
    User user = userService.getCurrentUser();
    List<OrderedItemDTO> purchases = mapOrdersIntoListOfOrderDTOs(user.getOrders());
    return new OrderListDTO(purchases);
  }

  @Override
  public OrderedItem saveOrder(OrderedItem orderedItem) {
    return orderRepository.save(orderedItem);
  }

  @Override
  public List<OrderedItemDTO> mapOrdersIntoListOfOrderDTOs(List<OrderedItem> orderedItems) {
    return orderedItems.stream()
        .map(OrderedItemDTO::new)
        .toList();
  }

  @Override
  public OrderedItemDTO activateItem(Long orderId) {
    OrderedItem orderedItemToActivate = getItemByUser(orderId);

    if (orderedItemToActivate.getStatus().equals(Active)) {
      throw new AlreadyActiveException();
    }

    Duration duration = orderedItemToActivate.getProduct().getDuration();
    orderedItemToActivate.setExpiry(LocalDateTime.now().plus(duration));
    orderedItemToActivate.setStatus(Active);
    orderRepository.save(orderedItemToActivate);

    return new OrderedItemDTO(orderedItemToActivate);
  }

  @Override
  public File getQrCode(Long orderId) throws IOException, WriterException {
    User user = userService.getCurrentUser();
    List<OrderedItem> orderedItems = user.getOrders();

    OrderedItem orderedItem = orderedItems.stream()
        .filter(item -> orderId.equals(item.getId()))
        .findFirst()
        .orElseThrow(NotMyOrderException::new);

    NameEmailDTO userDTO = new NameEmailDTO(user.getName(), user.getEmail());

    Product product = orderedItem.getProduct();
    ProductQrCodeDTO productDTO = new ProductQrCodeDTO(product.getName(),
        DurationConverter.convertDurationtoString(product.getDuration()),
        product.getType().getName(),
        orderedItem.getStatus().toString(),
        orderedItem.getExpiry() == null ? "Not yet activated" : orderedItem.getExpiry().toString());

    OrderQrCodeDTO data = new OrderQrCodeDTO(userDTO, productDTO);

    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(data);
    int size = 250;
    String fileType = "png";
    File qrFile = new File("qrcode." + fileType);
    createQrCode(qrFile, json, size, fileType);

    return qrFile;
  }

  @Override
  public void createQrCode(File qrFile, String qrCodeText, int size, String fileType) throws WriterException, IOException {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size);

    BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
    bufferedImage.createGraphics();

    Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, size, size);
    graphics.setColor(Color.BLACK);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (bitMatrix.get(i, j)) {
          graphics.fillRect(i, j, 1, 1);
        }
      }
    }
    ImageIO.write(bufferedImage, fileType, qrFile);
  }

  private OrderedItem getItemByUser(Long orderId) {
    User user = userService.getCurrentUser();
    List<OrderedItem> orderedItems = user.getOrders();

    return orderedItems.stream()
        .filter(orderedItem -> orderedItem.getId().equals(orderId))
        .findFirst()
        .orElseThrow(NotMyOrderException::new);
  }
}
