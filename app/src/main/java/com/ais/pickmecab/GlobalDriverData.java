package com.ais.pickmecab;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class GlobalDriverData
        {

            private String id;
            private Base64 photo;
            private String firstName;
            private String lastName;
            private String AKA;
            private String address;
            private String email;
            private String mobilePhone;
            private String otherPhone;
            private String sex;
            private String ethnicity;
            private String paymentCard;
            private Date cardExpiryDate;
            private String loginId;
            private String password;
            private String siid;
            private String vehicle;
            private String devicePhone;
            private String status;
            private Date lastActive;
            private Date lastBooking;
            private String driverType;
            private Base64 driverLicense;
            private String notes;
            private String device;
            private String invoiceOptions;
            private String paymentOptions;
            private Map<String,String> driverDocuments;
            private boolean deleted;
            private Date createdAt;
            private String createdBy;
            private Date lastUpdated;
            private String updatedBy;
            private String latitude;
            private String longitude;
            private String BloodGroup;
            private String DOB;
            private String Marraid;

            public String getDOB() {
                return DOB;
            }

            public void setDOB(String DOB) {
                this.DOB = DOB;
            }

            public String getBloodGroup() {
                return BloodGroup;
            }

            public void setBloodGroup(String bloodGroup) {
                BloodGroup = bloodGroup;
            }

            public String getMarraid() {
                return Marraid;
            }

            public void setMarraid(String marraid) {
                Marraid = marraid;
            }
            public Base64 getPhoto() {
                return photo;
            }

            public void setPhoto(Base64 photo) {
                this.photo = photo;
            }




            public String getId() {
                return id;
            }
            public void setId(String id) {
                this.id = id;
            }
            public String getFirstName() {
                return firstName;
            }
            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }
            public String getLastName() {
                return lastName;
            }
            public void setLastName(String lastName) {
                this.lastName = lastName;
            }
            public String getAKA() {
                return AKA;
            }
            public void setAKA(String aKA) {
                AKA = aKA;
            }
            public String getAddress() {
                return address;
            }
            public void setAddress(String address) {
                this.address = address;
            }
            public String getEmail() {
                return email;
            }
            public void setEmail(String email) {
                this.email = email;
            }
            public String getMobilePhone() {
                return mobilePhone;
            }
            public void setMobilePhone(String mobilePhone) {
                this.mobilePhone = mobilePhone;
            }
            public String getOtherPhone() {
                return otherPhone;
            }
            public void setOtherPhone(String otherPhone) {
                this.otherPhone = otherPhone;
            }
            public String getSex() {
                return sex;
            }
            public void setSex(String sex) {
                this.sex = sex;
            }
            public String getEthnicity() {
                return ethnicity;
            }
            public void setEthnicity(String ethnicity) {
                this.ethnicity = ethnicity;
            }
            public String getPaymentCard() {
                return paymentCard;
            }
            public void setPaymentCard(String paymentCard) {
                this.paymentCard = paymentCard;
            }
            public Date getCardExpiryDate() {
                return cardExpiryDate;
            }
            public void setCardExpiryDate(Date cardExpiryDate) {
                this.cardExpiryDate = cardExpiryDate;
            }
            public String getLoginId() {
                return loginId;
            }
            public void setLoginId(String loginId) {
                this.loginId = loginId;
            }
            public String getPassword() {
                return password;
            }
            public void setPassword(String password) {
                this.password = password;
            }
            public String getSiid() {
                return siid;
            }
            public void setSiid(String siid) {
                this.siid = siid;
            }
            public String getVehicle() {
                return vehicle;
            }
            public void setVehicle(String vehicle) {
                this.vehicle = vehicle;
            }
            public String getDevicePhone() {
                return devicePhone;
            }
            public void setDevicePhone(String devicePhone) {
                this.devicePhone = devicePhone;
            }
            public String getStatus() {
                return status;
            }
            public void setStatus(String status) {
                this.status = status;
            }
            public Date getLastActive() {
                return lastActive;
            }
            public void setLastActive(Date lastActive) {
                this.lastActive = lastActive;
            }
            public String getDriverType() {
                return driverType;
            }
            public void setDriverType(String driverType) {
                this.driverType = driverType;
            }
            public Base64 getDriverLicense() {
                return driverLicense;
            }
            public void setDriverLicense(Base64 driverLicense) {
                this.driverLicense = driverLicense;
            }

            public String getNotes() {
                return notes;
            }
            public void setNotes(String notes) {
                this.notes = notes;
            }
            public String getDevice() {
                return device;
            }
            public void setDevice(String device) {
                this.device = device;
            }
            public String getInvoiceOptions() {
                return invoiceOptions;
            }
            public void setInvoiceOptions(String invoiceOptions) {
                this.invoiceOptions = invoiceOptions;
            }
            public String getPaymentOptions() {
                return paymentOptions;
            }
            public void setPaymentOptions(String paymentOptions) {
                this.paymentOptions = paymentOptions;
            }
            public Map<String, String> getDriverDocuments() {
                return driverDocuments;
            }
            public void setDriverDocuments(Map<String, String> driverDocuments) {
                this.driverDocuments = driverDocuments;
            }



            public Date getLastBooking() {
                return lastBooking;
            }
            public void setLastBooking(Date lastBooking) {
                this.lastBooking = lastBooking;
            }
            public Date getCreatedAt() {
                return createdAt;
            }
            public void setCreatedAt(Date createdAt) {
                this.createdAt = createdAt;
            }
            public String getCreatedBy() {
                return createdBy;
            }
            public void setCreatedBy(String createdBy) {
                this.createdBy = createdBy;
            }
            public Date getLastUpdated() {
                return lastUpdated;
            }
            public void setLastUpdated(Date lastUpdated) {
                this.lastUpdated = lastUpdated;
            }
            public String getUpdatedBy() {
                return updatedBy;
            }
            public void setUpdatedBy(String updatedBy) {
                this.updatedBy = updatedBy;
            }

            public String getlatitude() {
                return this.latitude ;
            }
            public void setlatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getlongitude() {
                return this.longitude ;
            }
            public void setlongitude(String longitude) {
                this.longitude = longitude;
            }



        }