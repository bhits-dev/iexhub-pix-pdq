package gov.samhsa.c2s.iexhubpixpdq.service.exception;

/**
 * The Class Hl7v3TransformerException.
 */
public class Hl7v3TransformerException extends Exception {
	
	/**
	 * Instantiates a new c32 to green ccd transformer exception.
	 *
	 * @param msg the msg
	 * @param t the t
	 */
	public Hl7v3TransformerException(String msg, Throwable t) {
		super(msg, t);
	}

	/**
	 * Instantiates a new c32 to hl7v3 transformer exception.
	 *
	 * @param msg the msg
	 */
	public Hl7v3TransformerException(String msg) {
		super(msg);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

}
