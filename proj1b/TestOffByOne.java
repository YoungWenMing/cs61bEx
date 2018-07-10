import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    @Test
    public void TestEqualChar(){
        assertTrue(offByOne.equalChars('x', 'y'));
        assertTrue(offByOne.equalChars('o', 'p'));
        assertFalse(offByOne.equalChars('a', 'c'));

    }

    // Your tests go here.
    //Uncomment this class once you've created your CharacterComparator interface and OffByOne class. **/
}
