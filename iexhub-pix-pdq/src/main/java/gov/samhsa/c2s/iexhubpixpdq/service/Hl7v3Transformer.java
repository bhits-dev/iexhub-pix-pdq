/*******************************************************************************
 * Open Behavioral Health Information Technology Architecture (OBHITA.org)
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package gov.samhsa.c2s.iexhubpixpdq.service;

import gov.samhsa.c2s.iexhubpixpdq.exception.Hl7v3TransformerException;

/**
 * The Interface Hl7v3Transformer.
 */
public interface Hl7v3Transformer {


	/**
	 * Transform to hl7v3 pix xml.
	 *
	 * @param xml
	 *            the xml
	 * @param XSLTURI
	 *            the xslturi
	 * @return the string
	 * @throws Hl7v3TransformerException
	 *             the hl7v3 transformer exception
	 */
	public String transformToHl7v3PixXml(String xml, String XSLTURI);

	/**
	 * Transform c32 to green ccd.
	 *
	 * @param mrn
	 *            the medical record no of patient
	 * @param mrnDomain
	 *            the eHRdomain id
	 * @param xsltUri
	 *            the xsl for pixquery
	 * @return the string
	 * @throws Hl7v3TransformerException
	 *             the hl7v3 transformer exception
	 */
	public String getPixQueryXml(String mrn, String mrnDomain, String xsltUri)
			throws Hl7v3TransformerException;
}
