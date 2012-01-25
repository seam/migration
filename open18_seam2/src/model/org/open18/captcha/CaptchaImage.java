package org.open18.captcha;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.contexts.ServletLifecycle;

/**
 * Override built-in component to create png instead of jpg
 */
@Name("org.jboss.seam.captcha.captchaImage")
@BypassInterceptors
public class CaptchaImage extends org.jboss.seam.captcha.CaptchaImage {

	@Override
	public void getResource(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ServletLifecycle.beginRequest(request);
		Captcha captcha = Captcha.instance();
		try {
			ImageIO.write(captcha.renderChallenge(), captcha.getImageFormatName(), out);
		} finally {
			ServletLifecycle.endRequest(request);
		}

		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType(captcha.getImageMIMEType());
		response.getOutputStream().write(out.toByteArray());
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
}
