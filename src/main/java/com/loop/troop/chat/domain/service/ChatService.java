package com.loop.troop.chat.domain.service;

import com.loop.troop.chat.domain.Message;

import java.util.List;

public interface ChatService {

	void sendMessage(Message msg);

	List<Message> fetchMessages(String roomId);

}