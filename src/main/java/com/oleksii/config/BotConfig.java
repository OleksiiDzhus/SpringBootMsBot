package com.oleksii.config;

import com.microsoft.bot.connector.ConnectorClient;
import com.microsoft.bot.connector.customizations.MicrosoftAppCredentials;
import com.microsoft.bot.connector.implementation.ConnectorClientImpl;
import com.oleksii.jazzyspellcheck.JazzySpellChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class BotConfig {

  @Autowired
  private Environment environment;

  @Bean(name = "credentials")
  public MicrosoftAppCredentials getCredentials() {
    return new MicrosoftAppCredentials(environment.getProperty("bot.appId"),
        environment.getProperty("bot.appPassword"));
  }

}
