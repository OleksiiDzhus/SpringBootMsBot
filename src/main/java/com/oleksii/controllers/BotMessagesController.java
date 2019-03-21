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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/messages")
public class BotMessagesController {

  private final MicrosoftAppCredentials credentials;
  private final List<ResourceResponse> responses;
  private final ActivityCreator activityCreator;
  private final ResourceResponseSender sender;

  @Autowired
  public BotMessagesController(MicrosoftAppCredentials credentials, List<ResourceResponse> responses,
                               ActivityCreator activityCreator, ResourceResponseSender sender) {
    this.credentials = credentials;
    this.responses = responses;
    this.activityCreator = activityCreator;
    this.sender = sender;
  }

  @PostMapping(path = "")
  public List<ResourceResponse> create(@RequestBody @Valid
                                       @JsonDeserialize(using = DateTimeDeserializer.class) Activity activity) {
    ConnectorClient connector =
            new ConnectorClientImpl(activity.serviceUrl(), credentials);

    Activity echoActivity = activityCreator.createEchoActivity(activity);
    Activity checkedActivity = activityCreator.createSpellCheckedActivity(activity);
    Conversations conversation = ConversationCreator.createResponseConversation(connector);

    ResourceResponse echoResponse =
            sender.send(conversation, activity, echoActivity);
    responses.add(echoResponse);

    ResourceResponse spellCheckedResponse =
            sender.send(conversation, activity, checkedActivity);
    responses.add(spellCheckedResponse);

    return responses;
  }
}
