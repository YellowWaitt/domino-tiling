package application.model.adapter;

public class BoundaryParserException extends Exception {

	private static final long serialVersionUID = -4570724625850236536L;

	public BoundaryParserException() {
		super();
	}

	public BoundaryParserException(String message) {
		super(message);
	}

	public BoundaryParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public BoundaryParserException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BoundaryParserException(Throwable cause) {
		super(cause);
	}
}
