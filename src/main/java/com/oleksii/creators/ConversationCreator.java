package com.oleksii.creators;

import com.microsoft.bot.connector.ConnectorClient;
import com.microsoft.bot.connector.Conversations;
import org.springframework.stereotype.Component;

@Component
public class ConversationCreator {

  private ConversationCreator() {

  }

  public static Conversations createResponseConversation(ConnectorClient client) {
    return client.conversations();
  }
}
