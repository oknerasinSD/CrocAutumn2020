import java.util.HashMap;
import java.util.Map;

public class WordCount {

    /**
     * Выделение слов из текста.
     * @param string - текст.
     * @return - массив, элементами которого являются все слова текста с повторениями.
     */
    static String[] formatString(String string) {
        return string
                .replaceAll("[^a-zA-Zа-яА-Я ]", "")
                .toLowerCase()
                .split(" +");
    }

    /**
     * Подсчет количества вхожлений каждого слова в текст.
     * @param string - текст.
     * @return - мапа, в которой ключами являются уникальные слова из текста, а значениями - количества вхождений слов.
     */
    static public Map<String, Integer> countWords(String string) {
        String[] parsedString = formatString(string);
        Map<String, Integer> words = new HashMap<>();
        for (String element : parsedString) {
            if ("".equals(element)) {
                continue;
            }
            if (words.containsKey(element)) {
                words.put(element, words.get(element) + 1);
            } else {
                words.put(element, 1);
            }
        }
        return words;
    }
}
