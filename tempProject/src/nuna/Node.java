package nuna;

class Node {
	private Node[] nods = new Node[26];
	private char letter;
	private int point;
	private int level;

	public Node() {
		for (int i = 0; i < 26; i++) {
			nods[i] = null;
		}
		this.letter = ' ';
		this.point = 0;
		this.level = 0;
	}

	public Node[] getnods() {
		return nods;
	}

	public void setnods(Node[] nods) {
		this.nods = nods;
	}

	public char getLetter() {
		return letter;
	}

	public void setLetter(char letter) {
		this.letter = letter;
	}

	public int getpoint() {
		return point;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public Node[] getNods() {
		return nods;
	}

	public void setNods(Node[] nods) {
		this.nods = nods;
	}

	public void setpoint(int point) {
		this.point = point;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean arrayIsEmpty(Node n) {
		boolean checker = true;
		for (int i = 0; i < n.getnods().length; i++) {
			if (n.getnods()[i] != null) {
				checker = false;
			}
		}

		return checker;
	}

	public int charIndex(char letter) {
		int index = 1;
		String cha = "abcdefghigklmnopqrstuvwxyz";

		while (cha.charAt(index) == (letter))
			;
		{
			index = index + 1;
		}
		return index;

	}

}
