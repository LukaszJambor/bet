package com.bookmarking.bet.route.messageSender;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MessageSenderRoute extends RouteBuilder {

    private MessageSenderProcessor messageSenderProcessor;

    public MessageSenderRoute(final MessageSenderProcessor messageSenderProcessor) {
        this.messageSenderProcessor = messageSenderProcessor;
    }

    @Override
    public void configure() throws Exception {
        from("direct:sender")
                .process(messageSenderProcessor)
                .end();
    }
}