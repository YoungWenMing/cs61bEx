import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {

    static CharacterComparator offByOne = new OffByN(1);
    static CharacterComparator offBy3 = new OffByN(3);

    @Test
    public void TestEqualChar(){
        assertTrue(offByOne.equalChars('x', 'y'));
        assertTrue(offByOne.equalChars('o', 'p'));
        assertFalse(offByOne.equalChars('a', 'c'));

        assertTrue(offBy3.equalChars('a', 'd'));
        assertFalse(offBy3.equalChars('x', 'z'));
    }

}
