package com.oleksii;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.joda.deser.DateTimeDeserializer;
import com.microsoft.bot.connector.customizations.CredentialProvider;
import com.microsoft.bot.connector.customizations.CredentialProviderImpl;
import com.microsoft.bot.connector.customizations.MicrosoftAppCredentials;
import com.microsoft.bot.connector.implementation.ConnectorClientImpl;
import com.microsoft.bot.schema.models.Activity;
import com.microsoft.bot.schema.models.ActivityTypes;
import com.microsoft.bot.schema.models.ResourceResponse;
import java.util.logging.Logger;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/messages")
public class BotTaskHandler {

  private static final Logger LOGGER = Logger.getLogger(App.class.getName());
  private static String appId = "";
  private static String appPassword = "";
  private MicrosoftAppCredentials credentials = new MicrosoftAppCredentials(appId, appPassword);

  @PostMapping(path = "")
  public ResourceResponse create(@RequestBody @Valid
  @JsonDeserialize(using = DateTimeDeserializer.class) Activity activity) {
    ConnectorClientImpl connector = new ConnectorClientImpl(activity.serviceUrl(), credentials);
    Activity activityOne = new Activity()
        .withType(ActivityTypes.MESSAGE)
        .withText("you post :" + activity.text())
        .withRecipient(activity.from())
        .withFrom(activity.recipient());
    ResourceResponse response = connector.conversations()
        .sendToConversation(activity.conversation().id(),
            activityOne
        );
    return response;
  }
}
