import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    } //Uncomment this class once you've created your Palindrome class./* */

    @Test
    public void testIsPalindrome(){
        String word = "wonderful";
        String word0 = "eye";
        String word1 = "a";
        assertEquals(palindrome.isPalindrome(word), false);
        assertEquals(palindrome.isPalindrome(word0), true);
        assertTrue(palindrome.isPalindrome(word1));
    }
}
