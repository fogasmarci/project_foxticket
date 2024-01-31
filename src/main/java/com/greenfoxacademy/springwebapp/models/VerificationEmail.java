package com.greenfoxacademy.springwebapp.models;

public class VerificationEmail {
  private String recipient;
  private String msgBody;
  private String subject;
  private String attachment;

  public VerificationEmail() {
  }

  public VerificationEmail(String recipient, String url, String username) {
    this.recipient = recipient;
    subject = "Please verify your email";
    msgBody = String.format("Hello, %s!\nPlease click on the link below to verify your e-mail address:\n%s",
        username, url);
  }

//  public VerificationEmail(String recipient, String msgBody, String subject) {
//    this.recipient = recipient;
//    this.msgBody = msgBody;
//    this.subject = subject;
//  }
//
//  public VerificationEmail(String recipient, String msgBody, String subject, String attachment) {
//    this.recipient = recipient;
//    this.msgBody = msgBody;
//    this.subject = subject;
//    this.attachment = attachment;
//  }

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public String getMsgBody() {
    return msgBody;
  }

  public void setMsgBody(String msgBody) {
    this.msgBody = msgBody;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getAttachment() {
    return attachment;
  }

  public void setAttachment(String attachment) {
    this.attachment = attachment;
  }
}
