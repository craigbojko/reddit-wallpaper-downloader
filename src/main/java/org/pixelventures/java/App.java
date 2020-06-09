package org.pixelventures.java;

import com.diogonunes.jcdp.color.*;
import com.diogonunes.jcdp.color.api.Ansi.*;

import org.pixelventures.java.imageGrabber.ImageGrabber;
import java.io.*;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        ImageGrabber grabber = new ImageGrabber();

        File dir = grabber.getDirectory();
        String str = grabber.parser();

        String OUTPUT_DIRECTORY = dir.getPath() + "/";
        log(
            str + 
            "\n" + dir.toPath() + 
            "\n" + OUTPUT_DIRECTORY
        , FColor.CYAN);

        // Boolean fileWriteResponse = grabber.testWriteFile();
        // System.out.println("Write to file: " + fileWriteResponse);
    }

    private static void log (String msg) {
        ColoredPrinter cp = new ColoredPrinter.Builder(0, false).build();
        cp.println(msg, Attribute.NONE, FColor.NONE, BColor.NONE);
    }
    private static void log (String msg, FColor color) {
        ColoredPrinter cp = new ColoredPrinter.Builder(0, false).build();
        cp.println(msg, Attribute.NONE, color, BColor.NONE);
    }
}
