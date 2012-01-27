package org.open18.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.contexts.Contexts;

/**
 * Override built-in captcha to make it more configurable.
 */
@Name("org.jboss.seam.captcha.captcha")
@BypassInterceptors
public class Captcha extends org.jboss.seam.captcha.Captcha {

	private Color backgroundColor = Color.WHITE;
	
	private Color textColor = Color.BLACK;
	
	private String imageFormatName = "png";
	
	@Override
	public BufferedImage renderChallenge() {
		BufferedImage challenge = new BufferedImage(70, 20, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = challenge.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setFont(new Font("SansSerif", Font.PLAIN, 12));
		graphics.setColor(getChallengeBackgroundColor());
		graphics.fillRect(0, 0, getChallengeImageWidth(), 20);
		graphics.setColor(getChallengeTextColor());
		graphics.drawString(getChallenge(), 5, 14);
		return challenge;
	}

	@Override
	protected Color getChallengeBackgroundColor() {
		return backgroundColor != null ? backgroundColor : super.getChallengeBackgroundColor();
	}

	@Override
	protected Color getChallengeTextColor() {
		return textColor != null ? textColor : super.getChallengeTextColor();
	}
	
	public void setBackgroundColor(String colorValue) {
		this.backgroundColor = Color.decode(colorValue);
	}

	public void setTextColor(String colorValue) {
		this.textColor = Color.decode(colorValue);
	}
	
	public String getImageMIMEType() {
		return "image/" + getImageFormatName();
	}
	
	public String getImageFormatName() {
		return imageFormatName;
	}
	
	public void setImageFormatName(String formatName) {
		if (!ImageIO.getImageWritersByFormatName(formatName).hasNext()) {
			throw new IllegalArgumentException("Image format unknown: " + formatName);
		}
		
		this.imageFormatName = formatName;
	}
	
	public static Captcha instance() {
		if (!Contexts.isSessionContextActive()) {
			throw new IllegalStateException("No session context active");
		}
		return (Captcha) Component.getInstance(Captcha.class, ScopeType.SESSION);
	}
	
}
