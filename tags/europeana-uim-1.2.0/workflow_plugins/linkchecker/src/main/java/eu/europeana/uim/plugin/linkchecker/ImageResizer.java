package eu.europeana.uim.plugin.linkchecker;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;


public class ImageResizer {

	
	  public static void scale(String src, int width, int height, String dest)
	     throws IOException {
	   BufferedImage bsrc = ImageIO.read(new File(src));
	   BufferedImage bdest =
	      new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	   Graphics2D g = bdest.createGraphics();
	   AffineTransform at =
	      AffineTransform.getScaleInstance((double)width/bsrc.getWidth(),
	          (double)height/bsrc.getHeight());
	   g.drawRenderedImage(bsrc,at);
	   ImageIO.write(bdest,"JPG",new File(dest));
	  }
}
