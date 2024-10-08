package com.ais.pickmecab;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class NotificationDataParser {


  Double lat_to;
  Double lat_fr;
  Double lang_to;
  Double lang_fr;
  String m_placeName;
  String m_to;
  String m_From;
  List<String> viaAddress = new ArrayList<String>();
  List<LatLng> viaLatLngArray = new ArrayList<LatLng>();
  String m_duration;
  String m_cPhone;
  String m_bPhone;
  String CustomerName;
  String noppl;
  String nobags;
  String Inst;
  String start_time;
  String payment_type;
  String warning_timer;
  String miles;
  String pri;
  String flightNo;
  String BookingNo;

  public String getFlightNo() {
    return flightNo;
  }

  public void setFlightNo(String flightNo) {
    this.flightNo = flightNo;
  }



  public String getBookingNo() {
    return BookingNo;
  }

  public void setBookingNo(String bookingNo) {
    BookingNo = bookingNo;
  }
  public String getMiles() {
    return miles;
  }
  public String getPrice() {
    return pri;
  }

  public void setPrice(String pri) {
    this.pri = pri;
  }
  public void setMiles(String miles) {
    this.miles = miles;
  }

  public String getWarning_timer() {
    return warning_timer;
  }

  public void setWarning_timer(String warning_timer) {
    this.warning_timer = warning_timer;
  }

  public String getPayment_type() {
    return payment_type;
  }

  public void setPayment_type(String payment_type) {
    this.payment_type = payment_type;
  }
  public String getStart_time() {
    return start_time;
  }

  public void setStart_time(String start_time) {
    this.start_time = start_time;
  }

  public String getInst() {
    return Inst;
  }

  public void setInst(String inst) {
    Inst = inst;
  }
  public String getNoppl() {
    return noppl;
  }
  public String getNobags() {
    return nobags;
  }

  public void setNobags(String nobags) {
    this.nobags = nobags;
  }
  public void setNoppl(String noppl) {
    this.noppl = noppl;
  }

  public Double getLat_fr() {
    return lat_fr;
  }

  public void setLat_fr(Double lat_fr) {
    this.lat_fr = lat_fr;
  }
  public Double getLang_fr() {
    return lang_fr;
  }

  public void setLang_fr(Double lang_fr) {
    this.lang_fr = lang_fr;
  }

  public String getCustomerName() {
    return CustomerName;
  }

  public void setCustomerName(String customerName) {
    CustomerName = customerName;
  }




  public String getM_placeName() {
    return m_placeName;
  }

  public void setM_placeName(String m_placeName) {
    this.m_placeName = m_placeName;
  }



  public Double getLang_to() {
    return lang_to;
  }

  public void setLang_to(Double lang_to) {
    this.lang_to = lang_to;
  }

  public String getM_to() {
    return m_to;
  }

  public void setM_to(String m_to) {
    this.m_to = m_to;
  }



  public String getM_From() {
    return m_From;
  }

  public void setM_From(String m_From) {
    this.m_From = m_From;
  }



  public String getM_cPhone() {
    return m_cPhone;
  }

  public void setM_cPhone(String m_cPhone) {
    this.m_cPhone = m_cPhone;
  }



  public String getM_bPhone() {
    return m_bPhone;
  }

  public void setM_bPhone(String m_bPhone) {
    this.m_bPhone = m_bPhone;
  }



  public Double getLat_to() {
    return lat_to;
  }

  public void setLat_to(Double lat_to) {

    this.lat_to = lat_to;
  }



  public String getM_duration() {
    return m_duration;
  }

  public void setM_duration(String m_duration) {
    this.m_duration = m_duration;
  }




}
