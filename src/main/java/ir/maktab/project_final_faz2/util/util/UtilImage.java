package ir.maktab.project_final_faz2.util.util;


import ir.maktab.project_final_faz2.exception.PhotoValidationException;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class UtilImage {
    private static final int KILOBYTE = 1024;
    private static final int SIZE_IMAGE = 300;

    public static byte[] validationImage(byte[] image) {

        if ((image.length / KILOBYTE) > SIZE_IMAGE)
            throw new PhotoValidationException("size image bigger of 300Kb");

        try {
            File file = new File("/path/file");
            FileUtils.writeByteArrayToFile(file, image);
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);
            if (!imageReaders.hasNext()) {
                throw new PhotoValidationException("Image Readers Not Found!!!");
            }
            ImageReader reader = imageReaders.next();
            if (!(reader.getFormatName().equalsIgnoreCase("jpeg") || reader.getFormatName().equalsIgnoreCase("jpg")))
                throw new PhotoValidationException("Photo format not valid should be jpg or jpeg format ");
            imageInputStream.close();
        } catch (IOException e) {
            throw new PhotoValidationException(e.getMessage());
        }
        return image;

    }

    public static File getFileImage(byte[] imageData, File outPutFile) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        BufferedImage read;
        try {
            read = ImageIO.read(bais);
        } catch (IOException e) {
            throw new PhotoValidationException(e.getMessage());
        }
        try {
            ImageIO.write(read, "jpg", outPutFile);
            return outPutFile;
        } catch (IOException e) {
            throw new PhotoValidationException(e.getMessage());
        }
    }
}
