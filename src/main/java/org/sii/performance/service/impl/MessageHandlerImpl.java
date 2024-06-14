package org.sii.performance.service.impl;

import lombok.RequiredArgsConstructor;
import org.sii.performance.service.api.MessageHandler;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageHandlerImpl implements MessageHandler {
    private final MessageSource messageSource;
    @Override
    public String getMessage(String message) {
        return messageSource.getMessage(message, null, Locale.ENGLISH);
    }
}
