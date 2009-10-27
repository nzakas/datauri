/*
 * Copyright (c) 2009 Nicholas C. Zakas. All rights reserved.
 * http://www.nczonline.net/
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
 
package net.nczonline.web.datauri;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import org.apache.commons.codec.binary.Base64;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Generator for Data URIs.
 * @author Nicholas C. Zakas
 */
public class DataURIGenerator {
 

    private static HashMap imageTypes = new HashMap();
    private static HashMap textTypes = new HashMap();
 
    //initialize file types and MIME types
    static {        
        imageTypes.put("gif", "image/gif");
        imageTypes.put("jpg", "image/jpeg");
        imageTypes.put("png", "image/png");
        imageTypes.put("jpeg", "image/jpeg");   
        
        textTypes.put("htm", "text/html");
        textTypes.put("html", "text/html");
        textTypes.put("xml", "application/xml");
        textTypes.put("xhtml", "application/xhtml+xml");  
        textTypes.put("js", "application/x-javascript");
        textTypes.put("css", "text/css");
        textTypes.put("txt", "text/plain");
    }    
    
    //--------------------------------------------------------------------------
    // Generate data URIs from a file
    //--------------------------------------------------------------------------
    
    /**
     * Generates a data URI from a file, outputting it to the given writer. The
     * MIME type is determined from examining the filename.
     * @param file The file from which to generate the data URI.
     * @param out Where to output the data URI.
     * @throws java.io.IOException
     */ 
    public static void generate(File file, Writer out) throws IOException {
        generate(file, out, null, false);        
    }
    
    /**
     * Generates a data URI from a file, outputting it to the given writer. The
     * MIME type is determined from examining the filename.
     * @param file The file from which to generate the data URI.
     * @param out Where to output the data URI.
     * @param verbose Whether to display additional information.
     * @throws java.io.IOException
     */    
    public static void generate(File file, Writer out, boolean verbose) throws IOException {
        generate(file, out, null, verbose);        
    }
    
    /**
     * Generates a data URI from a file, outputting it to the given writer.
     * @param file The file from which to generate the data URI.
     * @param out Where to output the data URI.
     * @param mimeType The MIME type to use for the data URI.
     * @throws java.io.IOException
     */    
    public static void generate(File file, Writer out, String mimeType) throws IOException {
        generate(file, out, null, null, false);        
    }
    
    /**
     * Generates a data URI from a file, outputting it to the given writer.
     * @param file The file from which to generate the data URI.
     * @param out Where to output the data URI.
     * @param mimeType The MIME type to use for the data URI.
     * @param verbose Whether to display additional information.
     * @throws java.io.IOException
     */    
    public static void generate(File file, Writer out, String mimeType, boolean verbose) throws IOException {
        generate(file, out, null, null, verbose);        
    }
    
    /**
     * Generates a data URI from a file, outputting it to the given writer.
     * @param file The file from which to generate the data URI.
     * @param out Where to output the data URI.
     * @param mimeType The MIME type to use for the data URI.
     * @param charset The charset to use for the data URI.
     * @throws java.io.IOException
     */    
    public static void generate(File file, Writer out, String mimeType, String charset) throws IOException {
        generate(file, out, mimeType, charset, false);
    }

    /**
     * Generates a data URI from a file, outputting it to the given writer.
     * @param file The file from which to generate the data URI.
     * @param out Where to output the data URI.
     * @param mimeType The MIME type to use for the data URI.
     * @param charset The charset to use for the data URI.
     * @param verbose Whether to display additional information.
     * @throws java.io.IOException
     */
    public static void generate(File file, Writer out, String mimeType, String charset, boolean verbose) throws IOException {
        generateDataURI(file, out, getMimeType(file.getName(), mimeType, verbose), getCharset(file.getName(), charset, verbose));
    }
  
    //--------------------------------------------------------------------------
    // Generate data URIs from a file
    //--------------------------------------------------------------------------
    
    
    
    //--------------------------------------------------------------------------
    // Helper methods
    //--------------------------------------------------------------------------    
   
    /**
     * Generates a data URI from the specified file and outputs to the given writer.
     * @param file The file to from which to create a data URI.
     * @param out Where to output the data URI.
     * @param mimeType The MIME type to specify in the data URI.
     * @param charset The character set to specify in the data URI.
     * @throws java.io.IOException
     */
    private static void generateDataURI(File file, Writer out, String mimeType, String charset) throws IOException{
        
        //read the bytes from the file
        InputStream in = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        in.read(bytes);
        in.close();        
        
        //actually write
        generateDataURI(bytes, out, mimeType, charset);        
    }

    /**
     * Generates a data URI from a byte array and outputs to the given writer.
     * @param bytes The array of bytes to output to the data URI.
     * @param out Where to output the data URI.
     * @param mimeType The MIME type to specify in the data URI.
     * @param charset The character set to specify in the data URI.
     * @throws java.io.IOException
     */
    private static void generateDataURI(byte[] bytes, Writer out, String mimeType, String charset) throws IOException {
        //create the output
        StringBuffer buffer = new StringBuffer();        
        buffer.append("data:");        
        
        //add MIME type        
        buffer.append(mimeType);
        
        //add charset if necessary
        if (charset != null){
            buffer.append(";charset=" + charset);
        }
        
        //output base64-encoding
        buffer.append(";base64,");
        buffer.append(new String(Base64.encodeBase64(bytes)));
        
        //output to writer
        out.write(buffer.toString());        
    }    
    
    /**
     * Determines if the given filename represents an image file.
     * @param filename The filename to check.
     * @return True if the filename represents an image, false if not.
     */
    private static boolean isImageFile(String filename){
        return imageTypes.containsKey(getFileType(filename));
    }
    
    /**
     * Retrieves the extension for the filename. 
     * @param filename The filename to get the extension from.
     * @return All characters after the final "." in the filename.
     */
    private static String getFileType(String filename){
        String type = "";

        int idx = filename.lastIndexOf('.');
        if (idx >= 0 && idx < filename.length() - 1) {
            type = filename.substring(idx + 1);
        }
        
        return type;
    }

    /**
     * Determines the MIME type to use for the given filename. If a MIME type
     * is passed in, then that is used by default. Otherwise, the filename
     * is inspected to determine the appropriate MIME type.
     * @param filename The filename to check.
     * @param mimeType The provided MIME type or null if nothing was provided.
     * @param verbose Whether or not to display additional information
     * @return The MIME type string to use for the filename.
     * @throws java.io.IOException When no MIME type can be determined.
     */
    private static String getMimeType(String filename, String mimeType, boolean verbose) throws IOException {
        if (mimeType == null){
            
            String type = getFileType(filename);

            //if it's an image type, don't use a charset
            if (imageTypes.containsKey(type)){    
                mimeType = (String) imageTypes.get(type);        
            } else if (textTypes.containsKey(type)){
                mimeType = (String) textTypes.get(type);    
            } else {
                throw new IOException("No MIME type provided and MIME type couldn't be automatically determined.");                
            }

            if (verbose){
                System.err.println("[INFO] No MIME type provided, defaulting to '" + mimeType + "'.");
            }      
        }
        
        return mimeType;      
    }
    
    /**
     * Determines the charset to use for the given filename. If a charset is
     * passed in, then that is used by default. If the filename represents an
     * image, this method always returns null because no charset should be used.
     * @param filename The filename to check. 
     * @param charset The provided charset.
     * @param verbose Whether or not to display additional information.
     * @return The charset string or null if no charset should be used.
     */
    private static String getCharset(String filename, String charset, boolean verbose) {
        if (charset != null){
            
            if (Charset.isSupported(charset)){
                
                if (isImageFile(filename)){
                    if (verbose){
                        System.err.println("[INFO] Image file detected, skipping charset '" + charset + "'.");
                    }             
                    charset = null;
                } else {
                    if (verbose){
                        System.err.println("[INFO] Using charset '" + charset + "'.");
                    }                     
                }

                
            } else {
                charset = null;
                if (verbose){
                    System.err.println("[INFO] Charset '" + charset + "' is invalid, skipping.");
                }                 
            }
           
        } else {
            if (verbose){
                System.err.println("[INFO] Charset not specified, skipping.");
            }
        }
        
        return charset;
    }
            
}
