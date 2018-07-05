package com.oleksii.creator;

import com.microsoft.bot.connector.ConnectorClient;
import com.microsoft.bot.schema.models.Activity;
import com.microsoft.bot.schema.models.ResourceResponse;
import org.springframework.stereotype.Component;

@Component
public class ResourceResponseCreator {

  private ResourceResponseCreator(){

  }

  public static ResourceResponse createResponse(ConnectorClient client,
      Activity requestActivity, Activity responseActivity){


    return client.conversations()
        .sendToConversation(requestActivity.conversation().id(), responseActivity);
  }

}
