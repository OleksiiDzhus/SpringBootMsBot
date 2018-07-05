package com.oleksii;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.joda.deser.DateTimeDeserializer;
import com.microsoft.bot.connector.ConnectorClient;
import com.microsoft.bot.connector.customizations.MicrosoftAppCredentials;
import com.microsoft.bot.connector.implementation.ConnectorClientImpl;
import com.microsoft.bot.schema.models.Activity;
import com.microsoft.bot.schema.models.ResourceResponse;
import com.oleksii.config.BotConfig;
import com.oleksii.creator.ActivityCreator;
import com.oleksii.creator.ResourceResponseCreator;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/messages")
@ComponentScan(basePackageClasses = {BotConfig.class, ActivityCreator.class})
public class BotTaskHandler {

  @Autowired
  private MicrosoftAppCredentials credentials;

  @PostMapping(path = "")
  public ResourceResponse create(@RequestBody @Valid
  @JsonDeserialize(using = DateTimeDeserializer.class) Activity activity) {
    ConnectorClient connector =
        new ConnectorClientImpl(activity.serviceUrl(), credentials);

    Activity notChecked = ActivityCreator.createEchoActivity(activity);
    Activity responseOne = ActivityCreator.createSpellCheckedActivity(activity);

    ResourceResponseCreator.createResponse(connector, activity, notChecked);

    return ResourceResponseCreator.createResponse(connector, activity, responseOne);
  }

}
