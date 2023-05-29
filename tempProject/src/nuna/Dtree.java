package nuna;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import nuna.gui.DtreeJFrame;

public class Dtree {

	private Node current;
	private boolean check = true;
	int round;
	private static Dtree d = new Dtree();
	private static String searchResult = "------------------------";

	public Dtree() {
		this.current = new Node();
	}

	public boolean isCheck() {
		return check;
	}

	public void setCurrent(Node current) {
		this.current = current;

	}

	public void insertLetter(Node n, String w) {
		if (this.check == true) {
			char charTemp = w.charAt(n.getLevel());
			int temppoint = n.getpoint();

			System.out.println(w.length());

			Node temp = new Node();
			Node[] tempArray = new Node[26];
			temp.setnods(tempArray);
			if (n.getLevel() == w.length() - 1) {
				temppoint++;
			}
			temp.setpoint(temppoint);
			temp.setLetter(charTemp);
			temp.setLevel(n.getLevel() + 1);
			n.getnods()[n.charIndex(charTemp)] = temp;
			if (n.getLevel() == w.length() - 1) {
				this.check = false;
			}
			insertLetter(n.getnods()[n.charIndex(charTemp)], w);

		}

	}

	public String add(String word) {
		this.current = add(word, current);
		this.check = true;
		round = 0;
		return "word added successfully";
	}

	private Node add(String w, Node n) {
		if (round == w.length()) {
			n.setpoint(n.getpoint() + 1);
			return (n);
		}
		if (n == null) {
			n = new Node();
			return add(w, n);
		}

		else if (((n.getnods()[n.charIndex(w.charAt(round))]) == null) && (round <= w.length())) {
			insertLetter(n, w);
			round++;
			if (round == w.length()) {
				return (n);
			}
			return add(w, n);
		}

		else {
			if (((n.getnods()[n.charIndex(w.charAt(round))]) != null) && (round < w.length())) {
				round++;
				add(w, n.getnods()[n.charIndex(w.charAt(round - 1))]);

			}
		}

		return (n);
	}

	public String searchWord(String w) {
		current = searchWord(current, w);
		round = 0;
		return searchResult;
	}

	public String delWord(String w) {
		current = delWord(current, w);
		round = 0;
		return "word deleted";
	}

	public Node delWord(Node n, String w) {
		if ((round == w.length()) && (n.getpoint() > 0)) {
			delWord(w);
			System.out.println("The word " + w + "Deleted from the dicionery");
			return (n);
		} else if ((round == w.length()) && (n.getpoint() == 0)) {
			System.out.println("The word " + w
					+ "Cannot Delelete because not appear in the dictionery");
			return (n);

		} else if ((n.getnods()[n.charIndex(w.charAt(round))] != null) && (round < w.length() + 1)) {
			round++;
			searchWord(n.getnods()[n.charIndex(w.charAt(round - 1))], w);

		} else {
		}
		System.out.println("The word " + w + " Deleted from the dicionery");

		return (n);
	}

	public Node searchWord(Node n, String w) {
		if ((round == w.length()) && (n.getpoint() > 0)) {
			searchResult = "The word " + w + " appear " + n.getpoint() + " times on the dicionery";
			System.out.println(searchResult);
			return (n);
		} else if ((round == w.length()) && (n.getpoint() == 0)) {
			searchResult = "The word " + w + " is not appear in the dictionery";
			System.out.println(searchResult);
			return (n);

		} else if ((n.getnods()[n.charIndex(w.charAt(round))] != null) && (round < w.length() + 1)) {
			round++;
			searchWord(n.getnods()[n.charIndex(w.charAt(round - 1))], w);

		} else {
			searchResult = "The word " + w + " is not appear in the dictionery";
			System.out.println(searchResult);

		}
		return (n);
	}

	public static void main(String[] args) throws IOException {
		new DtreeJFrame();

		//
		d.add("ram");
		d.add("dan");
		d.add("rina");
		//
		// d.searchWord("ram");
		//
		// d.delWord("ram");

	}

	public static String load(final String path) {
		try {
			d = new Dtree();

			FileReader fr1 = new FileReader(path);
			BufferedReader fileReader1 = new BufferedReader(fr1);

			while (fileReader1.readLine() != null) {
				String line = fileReader1.readLine();
				System.out.print("line: " + line);
				d.add(line);
			}
			return "Dictionary has been successfully loaded";
		} catch (Exception e) {
			e.printStackTrace();
			return "failed to load dictionary ";
		}

	}

	// public static String methodLoad(String text) {
	// return "methodLoad " + text;
	// }

	public static String methodDelete(String text) {
		return "methodDelete " + text;
	}

	public static String methodSearch(String text) {
		return "methodSearch " + text;
	}

	public static Dtree getD() {
		return d;
	}
}
