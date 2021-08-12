package com.onegateafrica.payload.Request;

public class OTPSystem {

  private Long mobilenumber;
  private String otp;
  private Long expiretime;

  public Long getMobilenumber() {
    return mobilenumber;
  }

  public void setMobilenumber(Long mobilenumber) {
    this.mobilenumber = mobilenumber;
  }

  public String getOtp() {
    return otp;
  }

  public void setOtp(String otp) {
    this.otp = otp;
  }

  public Long getExpiretime() {
    return expiretime;
  }

  public void setExpiretime(Long expiretime) {
    this.expiretime = expiretime;
  }
}
