package com.onegateafrica.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onegateafrica.payload.Request.OTPSystem;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/mobilenumbers")
public class OtpController {

  private final static String ACCOUNT_SID = "ACfde7dc5b14db8384a0b85dc1a95162d6";
  private final static String AUTH_ID = "a617cf6d3620c5144cec45b323037e4d";

  static {
    Twilio.init(ACCOUNT_SID, AUTH_ID);
  }

  private Map<Long, OTPSystem> otp_data = new HashMap<>();

  @PostMapping("/otp")
  public ResponseEntity<Object> sendOTP(@RequestBody Long mobilenumber) {
    OTPSystem otp = new OTPSystem();
    otp.setMobilenumber(mobilenumber);
    otp.setOtp(String.valueOf(((int) (Math.random() * (10000 - 1000))) + 1000));
    otp.setExpiretime(System.currentTimeMillis() + 30000);
    otp_data.put(mobilenumber, otp);
    Message.creator(new PhoneNumber("+216" + mobilenumber), new PhoneNumber("+12674406711"), "Your OTP is: " + otp.getOtp()).create();
    return new ResponseEntity<>("OTP is sent successufully", HttpStatus.OK);
  }

  @PutMapping("/otp")
  public ResponseEntity<Object> verifyOTP(@RequestBody OTPSystem requestBodyOTPSystem) {

    if (requestBodyOTPSystem.getOtp() == null || requestBodyOTPSystem.getOtp().trim().length() <= 0) {
      return new ResponseEntity<>("Please provide OTP", HttpStatus.OK);
    }

    if (otp_data.containsKey(requestBodyOTPSystem.getMobilenumber())) {
      OTPSystem otp = otp_data.get(requestBodyOTPSystem.getMobilenumber());
      if (otp != null) {
        if (otp.getExpiretime() >= System.currentTimeMillis()) {
          if (requestBodyOTPSystem.getOtp().equals(otp.getOtp())) {
            otp_data.remove(requestBodyOTPSystem.getMobilenumber());
            return new ResponseEntity<>("OTP is verified Successfully", HttpStatus.OK);
          }
          return new ResponseEntity<>("Invalid OTP", HttpStatus.OK);
        }
        return new ResponseEntity<>("OTP is expired ", HttpStatus.OK);
      }
      return new ResponseEntity<>("Somthing went wrong .. !!", HttpStatus.OK);
    }
    return new ResponseEntity<>("Mobile number not found", HttpStatus.OK);

  }
}
