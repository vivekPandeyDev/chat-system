package com.loop.troop.chat.domain.service;

import com.loop.troop.chat.domain.message.Message;

import java.util.List;

public sealed interface ChatService permits DirectChatSvc, GroupChatSvc {
    void sendMessage(Message msg);
    List<Message> fetchMessages(String roomId);
}