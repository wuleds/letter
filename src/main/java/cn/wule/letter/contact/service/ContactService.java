package cn.wule.letter.contact.service;

import cn.wule.letter.contact.model.ContactInfo;
import cn.wule.letter.contact.model.ContactRequestHandle;

import java.util.List;

public interface ContactService
{
    void addContactList(String userId, String userName);
    void addContactRequest(String fromUserId, String toUserid, String info);

    boolean updateContactList(String userId, String contactId);

    boolean deleteContact(String userId, String contactId);

    boolean handleAddContact(String userId,ContactRequestHandle handle);

    List<ContactInfo> getContactList(String userId);

    ContactInfo searchContact(String contactId);
}