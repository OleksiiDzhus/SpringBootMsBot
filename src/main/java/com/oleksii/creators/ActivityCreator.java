package com.oleksii.creators;

import com.microsoft.bot.schema.models.Activity;
import com.microsoft.bot.schema.models.ActivityTypes;
import com.oleksii.jazzyspellcheck.JazzySpellChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityCreator {
  private static final String SPELL_CHECKED_RESPONSE_PART = "You have probably meant: ";
  private static final String ECHO_RESPONSE_PART = "You typed: ";

  private final JazzySpellChecker spellChecker;

  @Autowired
  private ActivityCreator(JazzySpellChecker spellChecker) {
    this.spellChecker = spellChecker;
  }

  public Activity createSpellCheckedActivity(Activity activity) {
    return createEmptyActivity(activity)
            .withText(SPELL_CHECKED_RESPONSE_PART + spellChecker.getCorrectedText(activity.text()));
  }

  public Activity createEchoActivity(Activity activity) {
    return createEmptyActivity(activity)
            .withText(ECHO_RESPONSE_PART + activity.text());
  }

  private Activity createEmptyActivity(Activity activity) {
    return new Activity()
            .withType(ActivityTypes.MESSAGE)
            .withRecipient(activity.from())
            .withFrom(activity.recipient());
  }
}
