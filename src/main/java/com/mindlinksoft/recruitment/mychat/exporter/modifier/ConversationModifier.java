package com.mindlinksoft.recruitment.mychat.exporter.modifier;

import com.mindlinksoft.recruitment.mychat.exporter.datastructure.Conversation;
import com.mindlinksoft.recruitment.mychat.exporter.modifier.filter.FilterKeyWord;
import com.mindlinksoft.recruitment.mychat.exporter.modifier.filter.FilterUser;
import com.mindlinksoft.recruitment.mychat.exporter.modifier.hide.HideCreditCardPhoneNumbers;
import com.mindlinksoft.recruitment.mychat.exporter.modifier.hide.HideKeyWord;
import com.mindlinksoft.recruitment.mychat.exporter.modifier.obfuscate.ObfuscateUsers;
import com.mindlinksoft.recruitment.mychat.exporter.modifier.report.ReportActiveSenders;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mindlinksoft.recruitment.mychat.exporter.modifier.Modifier.*;

/**
 * An implementation of the ConversationModifierService. Responsible for
 * choosing the correct modifier according to the given modifier type
 * (and modifier argument, if specified).
 */
public class ConversationModifier implements ConversationModifierService {

    /**
     * The order in which modifications are applied
     */
    public static final List<Modifier> orderOfModifications =
            List.of(FILTER_USER, FILTER_KEYWORD, HIDE_KEYWORD,
                    HIDE_CREDIT_CARD_AND_PHONE_NUMBERS, OBFUSCATE_USERS, REPORT_ACTIVE_USERS);

    /**
     * The given conversation which will be modified
     */
    private final Conversation conversation;

    /**
     * The modifiers specified in the command line
     */
    private final Set<Modifier> modifiers;

    /**
     * Sub modifiers that specify key words or senders
     */
    private final Map<Modifier, List<String>> modifierArguments;

    /**
     * Returns an implementation of the ConversationModifierService, which will
     * modify a conversation according to the modifier type and arguments
     *
     * @param conversation      the conversation you wish to modify
     * @param modifiers         the type of modification
     * @param modifierArguments which arguments e.g. users/words you wish to find/redact
     */
    public ConversationModifier(Conversation conversation, Set<Modifier> modifiers, Map<Modifier, List<String>> modifierArguments) {
        this.conversation = conversation;
        this.modifiers = modifiers;
        this.modifierArguments = modifierArguments;
    }

    /**
     * Applies the relevant ModifierBase class(es) to modify the conversation
     * according to the given modifier types and arguments. The modifications
     * are applied in the order given by orderOfModifications.
     *
     * @return modified conversation
     */
    public Conversation modify() {
        Conversation result = conversation;

        for (Modifier modifier : orderOfModifications) {
            if (modifiers.contains(modifier)) {
                ModifierBase modification = chooseModification(modifier, result);
                result = modification.modify();
            }
        }

        return result;
    }

    /**
     * Chooses the modifier class according to this class' modifier type
     *
     * @return instance of the relevant modifier class
     */
    public ModifierBase chooseModification(Modifier modifier, Conversation conversation) {
        switch (modifier) {
            case FILTER_USER:
                return new FilterUser(conversation, modifierArguments.get(modifier));
            case FILTER_KEYWORD:
                return new FilterKeyWord(conversation, modifierArguments.get(modifier));
            case HIDE_KEYWORD:
                return new HideKeyWord(conversation, modifierArguments.get(modifier));
            case HIDE_CREDIT_CARD_AND_PHONE_NUMBERS:
                return new HideCreditCardPhoneNumbers(conversation);
            case OBFUSCATE_USERS:
                return new ObfuscateUsers(conversation);
            case REPORT_ACTIVE_USERS:
                return new ReportActiveSenders(conversation);
            default:
                throw new IllegalStateException("System error. The specified modifier has not been integrated.");
        }
    }

    public Conversation getConversation() {
        return conversation;
    }
}