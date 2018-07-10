public class OffByOne implements CharacterComparator {

    @Override
    /* check whether two characters are next to each other in the alphabet */
    public boolean equalChars(char x, char y) {
        int diff = x - y;
        if(diff == -1)
            return true;
        return false;
    }
}
