package com.bookmarking.bet.route.messageSender;

import com.bookmarking.bet.queue.MessageSender;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.stream.Stream;

@Component
public class MessageSenderProcessor implements Processor {

    private static final String MESSAGE = "message";
    private static final String BET = "BET";
    private MessageSender messageSender;

    public MessageSenderProcessor(final MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void process(final Exchange exchange) throws Exception {
        final GenericFile genericFile = exchange.getIn().getBody(GenericFile.class);
        try (final BufferedReader bufferedReader = new BufferedReader(new FileReader((File) genericFile.getBody()))) {
            final Stream<String> lines = bufferedReader.lines();
            lines.forEach(this::sendMessage);
        }
    }

    private void sendMessage(final String line) {
        final HashMap<String, String> map = new HashMap<>();
        map.put(MESSAGE, line);
        if (line.contains(BET)) {
            messageSender.sendBet(map);
        } else {
            messageSender.sendResult(map);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}