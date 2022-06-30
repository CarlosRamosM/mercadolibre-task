package mx.com.ml.rebell.alliance.message.serive;

import mx.com.ml.rebell.alliance.message.exception.MessageException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MessageService {

  public String getMessage(List<List<String>> msgList) throws MessageException {
    List<String> msgPhrases = getMsgPhrases(msgList);
    if(!validateMessagesSize(msgList, msgPhrases.size())) {
      throw new MessageException("Tamaño del mensaje incorrecto");
    }
    removeGap(msgList, msgPhrases.size());
    String message = completeMessage(msgList);
    if(!validateMessagePhrases(msgPhrases,message)) {
      throw new MessageException("No se puede conocer el mensaje");
    }
    return message;
  }

  private List<String>getMsgPhrases(List<List<String>> msgList) {
    List<String> listWords = new ArrayList<>();
    for(List<String> msg : msgList) {
      listWords = Stream.concat(listWords.stream(), msg.stream())
          .distinct()
          .collect(Collectors.toList());
    }
    listWords.remove("");
    return listWords;
  }

  private void removeGap(List<List<String>> msgList, int gapSize) {
    int s;
    for(int i = 0; i < msgList.size(); i++) {
      s = msgList.get(i).size();
      msgList.set(i, msgList.get(i).subList(s-gapSize, s));
    }
  }

  private String completeMessage(List<List<String>> msgList) {
    var phrase = "";
    for(List<String> m : msgList) {
      if(!m.isEmpty() && !m.get(0).equals("")) {
        phrase = (m.size() == 1) ? m.get(0) : m.get(0) + " ";
        msgList.forEach(s -> s.remove(0));
        return  phrase + completeMessage(msgList);
      }
    }
    return phrase;
  }

  private boolean validateMessagesSize(List<List<String>> messages, int size) {
    for(List<String> m : messages) {
      if(m.size() < size){
        return false;
      }
    }
    return true;
  }
  private boolean validateMessagePhrases(List<String> phrases, String message) {
    List<String> msg = new ArrayList<>(Arrays.stream(message.split(" ")).toList());
    Collections.sort(phrases);
    Collections.sort(msg);
    return Arrays.equals(phrases.toArray(), msg.toArray());
  }
}
