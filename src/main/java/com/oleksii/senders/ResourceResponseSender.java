package com.oleksii.senders;

import com.microsoft.bot.connector.Conversations;
import com.microsoft.bot.schema.models.Activity;
import com.microsoft.bot.schema.models.ResourceResponse;
import org.springframework.stereotype.Component;

@Component
public class ResourceResponseSender {

  private ResourceResponseSender(){

  }

  public static ResourceResponse send(Conversations conversations,
      Activity requestActivity, Activity responseActivity) {
    return conversations.
        sendToConversation(requestActivity.conversation().id(), responseActivity);
  }
}
