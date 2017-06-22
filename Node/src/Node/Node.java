package Node;

import java.io.IOException;

public interface Node {
	public void readInput() throws IOException;
	public void PrintHT();
	public Boolean isDigit(char input);
	public Boolean isLetter(char input);
	public Boolean isSeparator(char input);
	public void hash(int nextid, int nextfree);
	public int lookupHT(int nextid, int nextfree);
	public void insertHT();
	public void mainLogic() throws IOException;
}
