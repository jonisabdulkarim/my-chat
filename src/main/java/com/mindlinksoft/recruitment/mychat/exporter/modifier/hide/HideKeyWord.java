package com.mindlinksoft.recruitment.mychat.exporter.modifier.hide;

import java.util.ArrayList;
import java.util.List;

import com.mindlinksoft.recruitment.mychat.exporter.datastructure.Conversation;
import com.mindlinksoft.recruitment.mychat.exporter.datastructure.Message;
import com.mindlinksoft.recruitment.mychat.exporter.modifier.ModifierBase;

/**
 * Represents a modifier that hides certain key words in messages
 */
public class HideKeyWord extends ModifierBase {

    /**
     * The key words to hide
     */
    private final List<String> keyWords;

    /**
     * Returns a modifier that hides certain key words in messages
     *
     * @param conversation contains the messages you wish to hide the key words from
     * @param keyWords     the key words to hide
     */
    public HideKeyWord(Conversation conversation, List<String> keyWords) {
        super(conversation);
        this.keyWords = keyWords;
    }

    @Override
    protected Conversation modify() {
        return hide();
    }

    /**
     * Creates a new Conversation with the key words hidden
     *
     * @return Conversation with key words hidden
     */
    public Conversation hide() {
        List<Message> messages = conversation.getMessages();
        List<Message> resultMessages = hideMessages(messages);
        return new Conversation(conversation.getName(), resultMessages, conversation.getActiveUsers());
    }

    /**
     * Helper method which adds old messages to the new messages
     * if it contains these key words
     *
     * @param oldMessages the messages to be filtered
     * @return resultMessages the messages filtered by this sender
     */
    private List<Message> hideMessages(List<Message> oldMessages) {
        List<Message> resultMessages = new ArrayList<>();

        for (Message message : oldMessages) {
            String content = message.getContent();
            String modifiedContent = modifyString(content);
            Message modifiedMessage = copyMessageExceptContent(message, modifiedContent);
            resultMessages.add(modifiedMessage);
        }

        return resultMessages;
    }

    private Message copyMessageExceptContent(Message message, String newContent) {
        return new Message(message.getTimestamp(), message.getSenderText(), newContent);
    }

    private String modifyString(String content) {
        for (String keyWord : keyWords) {
            content = content.replaceAll("(?i)\\b" + keyWord + "\\b", "*redacted*");
        }
        return content;
    }

}