package it.betplus.birs.controller;


import it.betplus.web.framework.managedbeans.GeneralBean;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@ManagedBean(name = "fileUploadController")
@SessionScoped  
public class FileUploadController extends GeneralBean{  
	
	UploadedFile file;
  
    public void handleFileUpload(FileUploadEvent event) {  
    	file = event.getFile();
    	System.out.println(file.getContents());
   	
    	
    	String newFileName = getApplicationPath()+  "images" + File.separator+"giochi"+File.separator+ file.getFileName();
    	
    	log.info(newFileName);
    	FacesMessage msg = new FacesMessage("Ok.", event.getFile().getFileName() + " è stato caricato.");
    	FacesContext.getCurrentInstance().addMessage(null, msg);
    	try
    	{
    	FileOutputStream fos = new FileOutputStream(new File(newFileName));
    	
    	InputStream is = file.getInputstream();
	
    	int BUFFER_SIZE = 8192;
    	byte[] buffer = new byte[BUFFER_SIZE];
    	int a;
    	while(true)
    	{
    	a = is.read(buffer);
    	if(a < 0) break;
    	fos.write(buffer, 0, a);
    	fos.flush();
    	}
    	fos.close();
    	is.close();
    	}
    	catch(IOException e)
    	{ e.printStackTrace();}
    	}
    
    
    
    private static InputStream scaleImage(InputStream p_image, int p_width, int p_height) throws Exception {
    	 
        InputStream imageStream = new BufferedInputStream(p_image);
        Image image = (Image) ImageIO.read(imageStream); 
    
        int thumbWidth = p_width;
           int thumbHeight = p_height;        
    
           // Make sure the aspect ratio is maintained, so the image is not skewed
           double thumbRatio = (double)thumbWidth / (double)thumbHeight;
           int imageWidth = image.getWidth(null);
           int imageHeight = image.getHeight(null);
           double imageRatio = (double)imageWidth / (double)imageHeight;
           if (thumbRatio < imageRatio) {
             thumbHeight = (int)(thumbWidth / imageRatio);
           } else {
             thumbWidth = (int)(thumbHeight * imageRatio);
           }
    
           // Draw the scaled image
           BufferedImage thumbImage = new BufferedImage(thumbWidth, 
             thumbHeight, BufferedImage.TYPE_INT_RGB);
           Graphics2D graphics2D = thumbImage.createGraphics();
           graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
             RenderingHints.VALUE_INTERPOLATION_BILINEAR);
           graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
    
           // Write the scaled image to the outputstream
           ByteArrayOutputStream out = new ByteArrayOutputStream();
           JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
           JPEGEncodeParam param = encoder.
             getDefaultJPEGEncodeParam(thumbImage);
           int quality = 100; // Use between 1 and 100, with 100 being highest quality
           quality = Math.max(0, Math.min(quality, 100));
           param.setQuality((float)quality / 100.0f, false);
           encoder.setJPEGEncodeParam(param);
           encoder.encode(thumbImage);        
           ImageIO.write(thumbImage, "jpg" , out); 
    
           // Read the outputstream into the inputstream for the return value
           ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());        
    
           return bis;        
       }
    
    }  

    
  