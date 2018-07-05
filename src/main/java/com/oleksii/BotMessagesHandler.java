package com.oleksii;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.joda.deser.DateTimeDeserializer;
import com.microsoft.bot.connector.ConnectorClient;
import com.microsoft.bot.connector.Conversations;
import com.microsoft.bot.connector.customizations.MicrosoftAppCredentials;
import com.microsoft.bot.connector.implementation.ConnectorClientImpl;
import com.microsoft.bot.schema.models.Activity;
import com.oleksii.creator.ActivityCreator;
import com.oleksii.creator.ConversationCreator;
import com.oleksii.sender.ResourceResponseSender;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/messages")
public class BotMessagesHandler {

  @Autowired
  private MicrosoftAppCredentials credentials;

  @PostMapping(path = "")
  public void create(@RequestBody @Valid
  @JsonDeserialize(using = DateTimeDeserializer.class) Activity activity) {
    ConnectorClient connector =
        new ConnectorClientImpl(activity.serviceUrl(), credentials);

    Activity echoActivity = ActivityCreator.createEchoActivity(activity);
    Activity checkedActivity = ActivityCreator.createSpellCheckedActivity(activity);
    Conversations conversation = ConversationCreator.createResponseConversation(connector);

    ResourceResponseSender.send(conversation, activity, echoActivity);
    ResourceResponseSender.send(conversation, activity, checkedActivity);
  }
}
