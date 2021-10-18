package application.model.adapter;

import java.util.ArrayDeque;

public class BoundaryParser {

	private String word;
	private StringBuilder result = new StringBuilder();
	private ArrayDeque<String> stack = new ArrayDeque<>();

	public BoundaryParser(String word) {
		this.word = word;
	}
	
	private int caseChar(int i, char c) throws BoundaryParserException {
		if (Character.isDigit(c)) {
			throw new BoundaryParserException(
					msgErreur("Caractère invalide", i));
		}
		result.append(c);
		return i + 1;
	}

	private int caseExp(int i) throws BoundaryParserException {
		if (result.length() > 0) {
			result.deleteCharAt(result.length() - 1);
		} else {
			throw new BoundaryParserException(
					msgErreur("Placement invalide de ^", i));
		}
		return caseExp(i, String.valueOf(word.charAt(i - 1)));
	}
	
	private int caseExp(int i, String s) throws BoundaryParserException {
		int start = i + 1;
		int stop = start;
		while (stop < word.length() && Character.isDigit(word.charAt(stop))) {
			++stop;
		}
		if (start == stop) {
			throw new BoundaryParserException(
					msgErreur("Un nombre était attendu", stop));
		}
		int n = Integer.parseInt(word.substring(start, stop));
		for (int j = 0; j < n; ++j) {
			result.append(s);
		}
		return stop;
	}
	
	private int caseLeftPar(int i) {
		stack.push(result.toString());
		result.setLength(0);
		return i + 1;
	}
	
	private int caseRightPar(int i) throws BoundaryParserException {
		if (stack.isEmpty()) {
			throw new BoundaryParserException(
					msgErreur("Parenthèse fermante en trop", i));
		}
		++i;
		if (i < word.length() && word.charAt(i) == '^') {
			String s = result.toString();
			result.setLength(0);
			i = caseExp(i, s);
		}
		result.insert(0, stack.pop());
		return i;
	}
	
	private String msgErreur(String msg, int pos) {
		StringBuilder str = new StringBuilder();
		str.append('\n' + msg + " :\n" + word + '\n');
		for (int i = 0; i < pos; ++i) {
			str.append(' ');
		}
		str.append('^');
		return str.toString();
	}
	
	public String parse() throws BoundaryParserException {
		int i = 0;
		while (i < word.length()) {
			char c = word.charAt(i);
			switch (c) {
			case ' ':
				++i;
				break;
			case '(':
				i = caseLeftPar(i);
				break;
			case ')':
				i = caseRightPar(i);
				break;
			case '^':
				i = caseExp(i);
				break;
			default:
				i = caseChar(i, c);
				break;
			}
		}
		if (!stack.isEmpty()) {
			throw new BoundaryParserException(
					msgErreur("Parenthèse fermante manquante", i));
		}
		return result.toString();
	}
}
