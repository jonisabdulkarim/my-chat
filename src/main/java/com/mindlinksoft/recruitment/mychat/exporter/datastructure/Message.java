package com.mindlinksoft.recruitment.mychat.exporter.datastructure;

import java.time.Instant;

/**
 * Represents a chat message.
 */
public final class Message {
    /**
     * The message content.
     */
    private final String content;

    /**
     * The message timestamp.
     */
    private final Instant timestamp;

    /**
     * The sender of this message as it appears in text.
     */
    private final String senderText;

    /**
     * Initializes a new instance of the {@link Message} class.
     *
     * @param timestamp  The timestamp at which the message was sent.
     * @param senderText The sender of this message as it appears in text.
     * @param content    The message content.
     */
    public Message(Instant timestamp, String senderText, String content) {
        this.content = content;
        this.timestamp = timestamp;
        this.senderText = senderText;
    }

    public String getContent() {
        return content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getSenderText() {
        return senderText;
    }

}
