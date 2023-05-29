package oz.infra.bidi;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;

//import java.text.DecimalFormat;

/**
 * @author t098
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 * 
 * Copied from Leonid (internet banking), on 05/02/2006
 */
public class BidiUtils {

	// private static DecimalFormat dc=new DecimalFormat("0.00");
	protected static byte[] DIGITS = new byte[256];

	protected static byte[] ALPHABET = new byte[256];

	protected static byte[] BRACKET = new byte[256];

	protected static byte[] NEUTRAL = new byte[256];

	static {
		// DIGITS
		DIGITS['#'] = '#';
		DIGITS['$'] = '$';
		DIGITS['%'] = '%';
		DIGITS['0'] = '0';
		DIGITS['1'] = '1';
		DIGITS['2'] = '2';
		DIGITS['3'] = '3';
		DIGITS['4'] = '4';
		DIGITS['5'] = '5';
		DIGITS['6'] = '6';
		DIGITS['7'] = '7';
		DIGITS['8'] = '8';
		DIGITS['9'] = '9';

		// ALPHABET
		ALPHABET['A'] = 'A';
		ALPHABET['B'] = 'B';
		ALPHABET['C'] = 'C';
		ALPHABET['D'] = 'D';
		ALPHABET['E'] = 'E';
		ALPHABET['F'] = 'F';
		ALPHABET['G'] = 'G';
		ALPHABET['H'] = 'H';
		ALPHABET['I'] = 'I';
		ALPHABET['J'] = 'J';
		ALPHABET['K'] = 'K';
		ALPHABET['L'] = 'L';
		ALPHABET['M'] = 'M';
		ALPHABET['N'] = 'N';
		ALPHABET['O'] = 'O';
		ALPHABET['P'] = 'P';
		ALPHABET['Q'] = 'Q';
		ALPHABET['R'] = 'R';
		ALPHABET['S'] = 'S';
		ALPHABET['T'] = 'T';
		ALPHABET['U'] = 'U';
		ALPHABET['V'] = 'V';
		ALPHABET['W'] = 'W';
		ALPHABET['X'] = 'X';
		ALPHABET['Y'] = 'Y';
		ALPHABET['Z'] = 'Z';
		ALPHABET['a'] = 'a';
		ALPHABET['b'] = 'b';
		ALPHABET['c'] = 'c';
		ALPHABET['d'] = 'd';
		ALPHABET['e'] = 'e';
		ALPHABET['f'] = 'f';
		ALPHABET['g'] = 'g';
		ALPHABET['h'] = 'h';
		ALPHABET['i'] = 'i';
		ALPHABET['j'] = 'j';
		ALPHABET['k'] = 'k';
		ALPHABET['l'] = 'l';
		ALPHABET['m'] = 'm';
		ALPHABET['n'] = 'n';
		ALPHABET['o'] = 'o';
		ALPHABET['p'] = 'p';
		ALPHABET['q'] = 'q';
		ALPHABET['r'] = 'r';
		ALPHABET['s'] = 's';
		ALPHABET['t'] = 't';
		ALPHABET['u'] = 'u';
		ALPHABET['v'] = 'v';
		ALPHABET['w'] = 'w';
		ALPHABET['x'] = 'x';
		ALPHABET['y'] = 'y';
		ALPHABET['z'] = 'z';

		// BRACKETS
		BRACKET['('] = '(';
		BRACKET[')'] = ')';
		BRACKET['<'] = '<';
		BRACKET['>'] = '>';
		BRACKET['['] = '[';
		BRACKET[']'] = ']';
		BRACKET['{'] = '{';
		BRACKET['}'] = '}';

		// NEUTRALS
		NEUTRAL[' '] = ' ';
		NEUTRAL['!'] = '!';
		NEUTRAL['"'] = '"';
		NEUTRAL['#'] = '#';
		NEUTRAL['$'] = '$';
		NEUTRAL['%'] = '%';
		NEUTRAL['&'] = '&';
		NEUTRAL['\''] = '\'';
		NEUTRAL['('] = '(';
		NEUTRAL[')'] = ')';
		NEUTRAL['*'] = '*';
		NEUTRAL['+'] = '+';
		NEUTRAL['-'] = '-';
		NEUTRAL['.'] = '.';
		NEUTRAL['/'] = '/';
		NEUTRAL[':'] = ':';
		NEUTRAL[';'] = ';';
		NEUTRAL['<'] = '<';
		NEUTRAL['='] = '=';
		NEUTRAL['>'] = '>';
		NEUTRAL['?'] = '?';
		NEUTRAL['@'] = '@';
		NEUTRAL['['] = '[';
		NEUTRAL['\\'] = '\\';
		NEUTRAL[']'] = ']';
		NEUTRAL['^'] = '^';
		NEUTRAL['_'] = '_';
		NEUTRAL['{'] = '{';
		NEUTRAL['|'] = '|';
		NEUTRAL['}'] = '}';
	}

	public final static int ENG = 1;

	public final static int HEB = 2;

	public final static int NDR = 3;

	public final static int DIG = 4;

	// private
	private byte[] SOURCE;

	private byte[] TARGET;

	private int SOURCE_LENGTH;

	private int IND5; // for digits

	private int IND1, IND2;

	private int LEFT = 0;

	private int RIGHT;

	private int STATUS;

	// Constractor
	private BidiUtils(byte[] b) {

		this.SOURCE = b;

	}

	// TEST ONLY
	public static void main(String[] args) {
		if (args.length > 0) {
			try {
				int k;
				byte b[] = new byte[4096];
				ByteArrayOutputStream baos;
				FileInputStream in = new FileInputStream(args[0]);
				baos = new ByteArrayOutputStream();
				while ((k = in.read(b)) > 0)
					baos.write(b, 0, k);

				System.out.println("\nINPUT\n");
				System.out.println(new String(baos.toByteArray()));
				System.out.println("\nOUTPUT\n");
				System.out.println(new String(BidiUtils.convert(baos.toByteArray())));

			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
		} else {

			String s = "1) ���� ���� " + "\n 1";
			System.out.println("\nINPUT\n");
			System.out.println(s);
			System.out.println("\nOUTPUT\n");
			System.out.println(new String(BidiUtils.convert(s.getBytes())));
			// System.out.println(convert("����"));

			try {
				System.out.println(BidiUtils.convertV2L("� B �"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

	}

	public static String convertV2L(String str)
		throws UnsupportedEncodingException {
		// System.out.println(str);
		String logicalStr = new String(convert(str.getBytes("ISO8859_8")), "ISO8859_8");
			
			/*
			 * v2l has a bug that sometimes insert nulls into string. i'll
			 * replace them with blanks. itamar, 23.04.2009
			 */

			char hex0c = 0;
			logicalStr = logicalStr.replace(hex0c, ' ');

		return logicalStr;
	}

	public static byte[] convert(byte[] input) {

		BidiUtils v2l;
		v2l = new BidiUtils(input);
		return (v2l.start());

	}

	public static boolean isDIGIT(byte b) {
		try {
			if (DIGITS[b] == b)
				return true;
			return false;
		} catch (Exception ex) {
			return false;
		}

	}

	public static boolean isALPHBET(byte b) {
		try {
			if (ALPHABET[b] == b)
				return true;
			return false;
		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean isHEBREW(byte b) {
		if ((b >= -32) && (b <= -6))
			return (true);
		return (false);

	}

	public static boolean isBRACKET(byte b) {
		try {
			if (BRACKET[b] == b)
				return true;
			return false;
		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean isNEUTRAL(byte b) {
		try {
			if (NEUTRAL[b] == b)
				return true;
			return false;
		} catch (Exception ex) {
			return false;
		}
	}

	private byte[] start() {

		SOURCE_LENGTH = SOURCE.length;

		RIGHT = SOURCE_LENGTH - 1;
		// RIGHT = SOURCE_LENGTH;

		if (SOURCE_LENGTH <= 1)
			return (SOURCE);

		TARGET = new byte[SOURCE_LENGTH];

		// Start
		while ((LEFT < SOURCE_LENGTH) && !isHEBREW(SOURCE[LEFT])) {
			TARGET[LEFT] = SOURCE[LEFT];
			LEFT++;
		}

		// only english so far
		if (LEFT == SOURCE_LENGTH)
			return (TARGET);

		IND5 = 0;

		while (!isHEBREW(SOURCE[RIGHT])) {

			TARGET[RIGHT] = SOURCE[RIGHT];
			if (isDIGIT(SOURCE[RIGHT]) && (IND5 == 0)) {

				IND5 = RIGHT;
			}
			if (isALPHBET(SOURCE[RIGHT])) {
				IND5 = 0;
			}

			RIGHT--;
		}

		if (RIGHT == LEFT) {

			TARGET[LEFT] = SOURCE[LEFT];
		} else {

			STATUS = HEB;

			IND1 = LEFT;
			IND2 = 0;

			for (int IND = LEFT; IND <= RIGHT; IND++) {
				if (IND == RIGHT) {

					switch (STATUS) {

					case ENG:
						// move TARGET[IND1: IND-IND1] = SOURCE[IND1:
						// IND-IND1=length]
						// IND
						copy(IND + 1);

						IND1 = IND;
						break;

					case HEB:
						IND2 = 0;
						break;

					}
					continue;

				}

				switch (STATUS) {

				case ENG:

					if (isHEBREW(SOURCE[IND])
							|| (isBRACKET(SOURCE[IND]) || SOURCE[IND] == '&')
							&& isHEBREW(SOURCE[IND + 1])) {
						// move TARGET[IND1: IND-IND1] = SOURCE[IND1: IND-IND1]
						copy(IND);
						IND1 = IND;
						STATUS = HEB;

					}
					break;

				case HEB:

					if (isHEBREW(SOURCE[IND]))
						IND2 = 0;
					// ?
					if (isALPHBET(SOURCE[IND])) {

						// inverse SOURCE[IND1:IND]
						inverse(IND1, IND);

						if (IND2 == 0)
							IND1 = IND;
						else
							IND1 = IND2;

						IND2 = 0;
						STATUS = ENG;
					}

					if (isNEUTRAL(SOURCE[IND]) && (IND2 == 0))
						IND2 = IND;

				}
			}

			if (IND1 != RIGHT) {

				// inverse SOURCE [IND1:IND]
				inverse(IND1, RIGHT + 1);
			}

		}

		return (TARGET);

	}

	private void copy(int IND) {

		// int length = IND-IND1+1;
		int length = IND - IND1;
		int index = IND1;
		// Leon >=???
		while ((length > 0)) {
			TARGET[index] = SOURCE[index];
			index++;
			length--;
		}
	}

	private void inverse(int begin, int end) {

		int i, j;

		if (IND2 == 0)
			end--;
		else
			end = IND2 - 1;

		IND5 = 0;

		for (i = begin; i <= end; i++) {

			if (isDIGIT(SOURCE[i])) {
				if (IND5 == 0)
					IND5 = i;

			} else {
				if (!isDIGIT(SOURCE[i])
						&& !(SOURCE[i] == ',' || SOURCE[i] == '.')) {

					if (IND5 > 0) {

						for (j = 0; j < i - IND5; j++) {
							TARGET[end - i + begin + 1 + j] = SOURCE[IND5 + j];
						}
						IND5 = 0;
					}

				}
				TARGET[end - i + begin] = SOURCE[i];
				if (isBRACKET(SOURCE[i])) {
					// replace bracet
					TARGET[end - i + begin] = replaceBracket(SOURCE[i]);

				}

			}
		}

		if (IND5 > 0) { // if 1

			for (j = 0; j > end - IND5; j++) {
				TARGET[begin + j] = SOURCE[IND5 + j];
			}
			IND5 = 0;

		} // end if 1
	}

	private byte replaceBracket(byte b) {
		byte bb = (byte) ' ';
		switch (b) {
		case ']':
			bb = (byte) '[';
			break;
		case '[':
			bb = (byte) ']';
			break;

		case '}':
			bb = (byte) '{';
			break;
		case '{':
			bb = (byte) '}';
			break;
		case '<':
			bb = (byte) '>';
			break;
		case '>':
			bb = (byte) '<';
			break;
		case ')':
			bb = (byte) '(';
			break;
		case '(':
			bb = (byte) ')';
			break;

		}

		return (bb);

	}
	/*
	 * public static String formatFloat(String number) { return
	 * dc.format(Float.parseFloat(number)); }
	 */
}