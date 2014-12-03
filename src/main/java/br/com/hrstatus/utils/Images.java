/*
    Copyright (C) 2012  Filippe Costa Spolti

	This file is part of Hrstatus.

    Hrstatus is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.hrstatus.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.ioc.Component;

/*
 *  @author spolti
 */

@Component
public class Images {

	Logger log =  Logger.getLogger(Images.class.getCanonicalName());
	
	private File pastaImages;
	private static final int IMG_WIDTH_SETTINGS = 350;
	private static final int IMG_HEIGHT_SETTINGS = 150;
	private static final int IMG_WIDTH_HOME = 500;
	private static final int IMG_HEIGHT_HOME = 300;
	@Autowired
	private HttpServletRequest request;

	public Images() throws IOException {

		String caminhoImagens = (System.getProperty("jboss.server.base.dir") + "/images");
		pastaImages = new File(caminhoImagens);
		log.fine("Local Imagens: " + pastaImages);

		if (pastaImages.isDirectory()) {
			log.fine("Final directory " + pastaImages + " exists.");
		} else {
			log.fine("The final directory" + pastaImages + " don't exists, creating it.");
			pastaImages.mkdir();
		}
	}

	public void save(UploadedFile imagem) {

		File destino = new File(pastaImages, "logo.imagem");
		try {
			IOUtils.copy(imagem.getFile(), new FileOutputStream(destino));
			log.info("Imagem salva em " + destino);
		} catch (IOException e) {
			throw new RuntimeException("Error copying the file", e);
		}
	}

	public void delete() {

		log.info("Removing the logo image");
		File logo = new File(pastaImages, "logo.imagem");
		File logo_home = new File(pastaImages, "logo_login.imagem");
		File logo_settings = new File(pastaImages, "logo_resized.imagem");

		logo.delete();
		logo_home.delete();
		logo_settings.delete();

	}

	@SuppressWarnings("deprecation")
	public File show(String VizualizationType) throws IOException {

		try {

			if (VizualizationType.equals("settings")) {
				// resized image
				BufferedImage originalImage = ImageIO.read(new File(pastaImages	+ "/logo.imagem"));
				int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
				BufferedImage resizeImagePng = resizeImage(originalImage, type,	"settings");
				ImageIO.write(resizeImagePng, "png", new File(pastaImages + "/logo_resized.imagem"));

				log.fine("Resizing oringinal Image to previous vizualization.");

				return new File(pastaImages + "/logo_resized.imagem");

			} else {
				
				log.fine("Resizing original Image to login page vizualization.");
				BufferedImage originalImage = ImageIO.read(new File(pastaImages	+ "/logo.imagem"));
				int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
				BufferedImage resizeImagePng = resizeImage(originalImage, type,	"home");
				ImageIO.write(resizeImagePng, "png", new File(pastaImages + "/logo_login.imagem"));
				return new File(pastaImages + "/logo_login.imagem");
			}

		} catch (Exception ex) {
			
			String imgDefault = request.getRealPath("img/hrstatus.jpg");
			log.fine("Logo Image  " + imgDefault);
			File file = new File(imgDefault);
			BufferedImage originalImage = ImageIO.read(file);
			int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			BufferedImage resizeImagePng = resizeImage(originalImage, type,	VizualizationType);
			ImageIO.write(resizeImagePng, "png", new File(pastaImages + "/logo_login.imagem"));
			return new File(pastaImages + "/logo_login.imagem");
		}
		
	}

	private static BufferedImage resizeImage(BufferedImage originalImage, int type, String target) {

		if (target.equals("settings")) {
			
			BufferedImage resizedImage = new BufferedImage(IMG_WIDTH_SETTINGS, IMG_HEIGHT_SETTINGS, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, IMG_WIDTH_SETTINGS, IMG_HEIGHT_SETTINGS, null);
			g.dispose();
			return resizedImage;

		} else if (target.equals("home")) {
			
			BufferedImage resizedImage = new BufferedImage(IMG_WIDTH_HOME, IMG_HEIGHT_HOME, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, IMG_WIDTH_HOME, IMG_HEIGHT_HOME,null);
			g.dispose();
			return resizedImage;

		} else
			return null;
	}
}