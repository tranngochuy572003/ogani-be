package com.example.mapper;

import com.example.dto.ContactDto;
import com.example.entity.Contact;

public class ContactMapper {
  public static ContactDto toDto(Contact contact) {
    ContactDto contactDto = new ContactDto();
    contactDto.setAddress(contact.getAddress());
    contactDto.setEmail(contact.getEmail());
    contactDto.setPhone(contact.getPhone());
    contactDto.setOpenTime(contact.getOpenTime());

    return contactDto;
  }

  public static Contact toEntity(ContactDto contactDto) {
    Contact contact = new Contact();
    contact.setAddress(contactDto.getAddress());
    contact.setEmail(contactDto.getEmail());
    contact.setPhone(contactDto.getPhone());
    contact.setOpenTime(contactDto.getOpenTime());
    return contact;
  }
}
