package com.oleksii.jazzyspellcheck;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JazzySpellChecker implements SpellCheckListener {

  private SpellChecker spellChecker;
  private List<String> misspelledWords;

  @Autowired
  private ClassPathResource dictionaryResource;

  @PostConstruct
  private void init() {
    try {
      File dictionary = dictionaryResource.getFile();
      spellChecker = new SpellChecker(new SpellDictionaryHashMap(dictionary));
      misspelledWords = new ArrayList<>();
      spellChecker.addSpellCheckListener(this);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getCorrectedLine(String line) {
    List<String> misSpelledWords = getMisspelledWords(line);

    for (String misSpelledWord : misSpelledWords) {
      List<String> suggestions = getSuggestions(misSpelledWord);
      if (suggestions.size() == 0) {
        continue;
      }
      String bestSuggestion = suggestions.get(0);
      line = line.replace(misSpelledWord, bestSuggestion);
    }

    return line;
  }

  public String getCorrectedText(String line) {
    StringBuilder builder = new StringBuilder();
    String[] tempWords = line.split(" ");

    for (String tempWord : tempWords) {
      if (!spellChecker.isCorrect(tempWord)) {
        List<Word> suggestions = spellChecker.getSuggestions(tempWord, 0);
        if (suggestions.size() > 0) {
          builder.append(spellChecker.getSuggestions(tempWord, 0).get(0).toString());
        } else {
          builder.append(tempWord);
        }
      } else {
        builder.append(tempWord);
      }
      builder.append(" ");
    }

    return builder.toString().trim();
  }


  public void spellingError(SpellCheckEvent event) {
    event.ignoreWord(true);
    misspelledWords.add(event.getInvalidWord());
  }

  private List<String> getMisspelledWords(String text) {
    StringWordTokenizer texTok = new StringWordTokenizer(text,
            new TeXWordFinder());
    spellChecker.checkSpelling(texTok);
    return misspelledWords;
  }

  private List<String> getSuggestions(String misspelledWord) {
    @SuppressWarnings("unchecked")
    List<Word> suggestedWords = spellChecker.getSuggestions(misspelledWord, 0);

    return suggestedWords.stream().map(Word::getWord).collect(Collectors.toList());

  }
}