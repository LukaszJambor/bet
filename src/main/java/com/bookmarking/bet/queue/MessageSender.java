package com.bookmarking.bet.queue;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessageSender {

    private JmsTemplate jmsTemplate;

    public MessageSender(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendBet(final Map<String, String> map) {
        jmsTemplate.convertAndSend("send.bet.message.queue", map);
    }

    public void sendResult(final Map<String, String> map) {
        jmsTemplate.convertAndSend("send.result.message.queue", map);
    }
}