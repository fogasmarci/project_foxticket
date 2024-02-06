package com.greenfoxacademy.springwebapp.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.greenfoxacademy.springwebapp.dtos.OrderQrCodeDTO;

import java.io.IOException;

public class QrCodeDataSerializer extends JsonSerializer<OrderQrCodeDTO> {
  @Override
  public void serialize(OrderQrCodeDTO data, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartObject();

    jsonGenerator.writeObjectFieldStart("user");
    jsonGenerator.writeStringField("name", data.getUser().name());
    jsonGenerator.writeStringField("email", data.getUser().email());
    jsonGenerator.writeEndObject();

    jsonGenerator.writeObjectFieldStart("product");
    jsonGenerator.writeStringField("name", data.getProduct().getName());
    jsonGenerator.writeStringField("duration", data.getProduct().getDuration());
    jsonGenerator.writeStringField("type", data.getProduct().getType());
    jsonGenerator.writeStringField("status", data.getProduct().getStatus());
    jsonGenerator.writeStringField("expirationDate", data.getProduct().getExpirationDate());
    jsonGenerator.writeEndObject();

    jsonGenerator.writeEndObject();
  }
}