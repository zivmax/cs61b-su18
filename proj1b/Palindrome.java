public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> wordDeque = new ArrayDeque<>();

        for (int i = 0; i < word.length(); i++) {
            wordDeque.addLast(word.charAt(i));
        }

        return wordDeque;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> wordDeque = wordToDeque(word);
        for (int i = 0, j = wordDeque.size() - 1; i <= wordDeque.size() / 2;) {
            if (wordDeque.get(i) != wordDeque.get(j)) {
                return false;
            }
            i++;
            j--;
        }

        return true;
    }

    // public boolean isPalindromeNoDeque(String word) {
    // if (word.length() <= 1) {
    // return true;
    // } else if (word.charAt(0) == word.charAt(word.length() - 1)) {
    // if (word.length() == 2) {
    // return true;
    // } else {
    // return isPalindromeNoDeque(word.substring(1, word.length() - 1));
    // }
    // }

    // return false;
    // }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> wordDeque = wordToDeque(word);
        for (int i = 0, j = wordDeque.size() - 1; i <= wordDeque.size() / 2;) {
            if (!cc.equalChars(wordDeque.get(i), wordDeque.get(j))) {
                return false;
            }
            i++;
            j--;
        }

        return true;
    }

}
