package com.oleksii.controllers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.joda.deser.DateTimeDeserializer;
import com.microsoft.bot.connector.ConnectorClient;
import com.microsoft.bot.connector.Conversations;
import com.microsoft.bot.connector.customizations.MicrosoftAppCredentials;
import com.microsoft.bot.connector.implementation.ConnectorClientImpl;
import com.microsoft.bot.schema.models.Activity;
import com.microsoft.bot.schema.models.ResourceResponse;
import com.oleksii.creators.ActivityCreator;
import com.oleksii.creators.ConversationCreator;
import com.oleksii.senders.ResourceResponseSender;
import java.util.List;
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

  @Autowired
  private List<ResourceResponse> responses;

  @PostMapping(path = "")
  public List<ResourceResponse> create(@RequestBody @Valid
  @JsonDeserialize(using = DateTimeDeserializer.class) Activity activity) {
    ConnectorClient connector =
        new ConnectorClientImpl(activity.serviceUrl(), credentials);

    Activity echoActivity = ActivityCreator.createEchoActivity(activity);
    Activity checkedActivity = ActivityCreator.createSpellCheckedActivity(activity);
    Conversations conversation = ConversationCreator.createResponseConversation(connector);

    ResourceResponse echoResponse =
        ResourceResponseSender.send(conversation, activity, echoActivity);
    responses.add(echoResponse);

    ResourceResponse spellCheckedResponse =
        ResourceResponseSender.send(conversation, activity, checkedActivity);
    responses.add(spellCheckedResponse);

    return responses;
  }
}
