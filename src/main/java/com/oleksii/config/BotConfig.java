package com.oleksii.config;

import com.microsoft.bot.connector.ConnectorClient;
import com.microsoft.bot.connector.customizations.MicrosoftAppCredentials;
import com.microsoft.bot.schema.models.ResourceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class BotConfig {
  @Value("${bot.appId}")
  private String botAppId;

  @Value("${bot.appPassword}")
  private String botAppPassword;

  @Bean
  public MicrosoftAppCredentials getCredentials() {
    return new MicrosoftAppCredentials(botAppId, botAppPassword);
  }

  @Bean
  public List<ResourceResponse> getResponses() {
    return new ArrayList<>();
  }

  @Bean
  public ClassPathResource getDictionary(){
    return new ClassPathResource("dictionary.txt");
  }
}
