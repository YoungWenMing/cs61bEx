public class Palindrome {

    /*disassemble a word, return a deque with characters one by one*/
    public Deque<Character> wordToDeque(String word){
        //return null; just for instantiation
        LinkedListDeque<Character> palindromeRe = new LinkedListDeque<>();
        for(int i = 0; i < word.length(); i ++)
            palindromeRe.addLast(word.charAt(i));
        return palindromeRe;
    }

    /* test whether it is palindrome recursively */
    private boolean isPalindromeHelper(Deque<Character> dequeChar){
        if(dequeChar.size() == 0 || dequeChar.size() == 1){
            return true;
        }else {
            Character first = dequeChar.removeFirst();
            Character last = dequeChar.removeLast();
            if(first.equals(last))
                return isPalindromeHelper(dequeChar);
            return false;
        }
    }

    /*test whether an word is a palindrome */
    public boolean isPalindrome(String word){
        Deque<Character> deque = wordToDeque(word);
        return isPalindromeHelper(deque);
    }

}
