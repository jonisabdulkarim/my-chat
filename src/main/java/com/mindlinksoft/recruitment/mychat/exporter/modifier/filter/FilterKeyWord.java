package com.mindlinksoft.recruitment.mychat.exporter.modifier.filter;

import java.util.ArrayList;
import java.util.List;

import com.mindlinksoft.recruitment.mychat.exporter.datastructure.Conversation;
import com.mindlinksoft.recruitment.mychat.exporter.datastructure.Message;
import com.mindlinksoft.recruitment.mychat.exporter.modifier.ModifierBase;

/**
 * Represents a filter that selects certain key words
 */
public class FilterKeyWord extends ModifierBase {

    /**
     * The key words to filter
     */
    private final List<String> keyWords;

    /**
     * Returns a filter that selects the given key word
     * @param conversation contains the messages you wish to filter
     * @param keyWords the key words to filter
     */
    public FilterKeyWord(Conversation conversation, List<String> keyWords) {
        super(conversation);
        this.keyWords = keyWords;
    }

    @Override
    protected Conversation modify() {
        return filter();
    }

    /**
     * Creates a new Conversation that only contains
     * messages with this key word
     * @return filtered Conversation
     */
    public Conversation filter() {
        List<Message> messages = conversation.getMessages();
        List<Message> newMessages = filterMessages(messages);
        return new Conversation(conversation.getName(), newMessages, conversation.getActiveUsers());
    }

    /**
     * Helper method which adds old messages to the new messages
     * if it contains this keyword
     * @param oldMessages the messages to be filtered
     * @return resultMessages the messages filtered by this sender
     */
    private List<Message> filterMessages(List<Message> oldMessages) {
        List<Message> resultMessages = new ArrayList<>();

        for (Message message : oldMessages) {
            String content = message.getContent();
            for (String keyWord : keyWords) {
                if (content.contains(keyWord)) {
                    resultMessages.add(message);
                }
            }
        }

        return resultMessages;
    }
}