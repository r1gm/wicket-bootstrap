package de.agilecoders.wicket.extensions.markup.html.bootstrap.form.validation;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.feedback.ErrorLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessages;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.util.lang.Args;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * <h1>Component behavior.</h1>
 * <p>This behavior adds an attribute to dom element with feedback message.</p>
 */
public class FeedbackMessageBehavior extends Behavior {

    private static final long serialVersionUID = 3116618186507530804L;
    private String attributeName;

    /**
     * Construct.
     *
     * @param attribute name of tag attribute
     */
    public FeedbackMessageBehavior(String attribute) {
        Args.notNull(attribute, "attribute");
        this.attributeName = attribute;
    }

    @Override
    public void onComponentTag(Component component, ComponentTag tag) {
        FeedbackMessages messages = component.getFeedbackMessages();
        if (messages != null) {
            for (FeedbackMessage message : messages.messages(ErrorLevelFeedbackMessageFilter.ALL)) {
                String messageString = message.getMessage().toString();
                if (!isNullOrEmpty(messageString)) {
                    tag.getAttributes().put(attributeName, messageString);
                }
            }
        }
    }
}
