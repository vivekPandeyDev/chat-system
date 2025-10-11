package com.loop.troop.chat.application.usecase;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public interface FileUseCase {

	void uploadFile(String filePath, InputStream inputStream, long size, String contentType);

	String generatePresignedUrl(String filePath, long expiry, TimeUnit unit);

}
