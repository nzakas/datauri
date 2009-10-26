/*
 * Copyright (c) 2009 Nicholas C. Zakas. All rights reserved.
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

import jargs.gnu.CmdLineParser;
import java.io.*;
import java.nio.charset.Charset;

/*
 * This file heavily inspired and based on YUI Compressor.
 * http://github.com/yui/yuicompressor
 */

public class DataURI {    

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //default settings
        boolean verbose = false;
        String charset = null;
        String outputFilename = null;
        Writer out = null;
        Reader in = null;
        String mimeType = null;
        String inputCharset = null;

        
        //initialize command line parser
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option verboseOpt = parser.addBooleanOption('v', "verbose");
        CmdLineParser.Option mimeTypeOpt = parser.addStringOption('m', "mime");
        CmdLineParser.Option helpOpt = parser.addBooleanOption('h', "help");
        CmdLineParser.Option charsetOpt = parser.addStringOption("charset");
        CmdLineParser.Option outputFilenameOpt = parser.addStringOption('o', "output");
        
        try {
            
            //parse the arguments
            parser.parse(args);

            //figure out if the help option has been executed
            Boolean help = (Boolean) parser.getOptionValue(helpOpt);
            if (help != null && help.booleanValue()) {
                usage();
                System.exit(0);
            } 
            
            //determine boolean options
            verbose = parser.getOptionValue(verboseOpt) != null;
            
            //check for charset
            charset = (String) parser.getOptionValue(charsetOpt);
            
            //check for MIME type
            mimeType = (String) parser.getOptionValue(mimeTypeOpt);

            //get the file arguments
            String[] fileArgs = parser.getRemainingArgs();
            
            //need to have at least one file
            if (fileArgs.length == 0){
                System.err.println("No files specified.");
                System.exit(1);
            }
            
            String inputFilename = fileArgs[0];
            //inputCharset = DataURIGenerator.getCharset(inputFilename, charset);
           
//            if (DataURIGenerator.isImageFile(inputFilename)){
//                if (verbose){
//                    System.err.println("[INFO] Image file detecting, not using specified charset.");
//                }
//                in = new InputStreamReader(new FileInputStream(inputFilename));
//            } else {
//                in = new InputStreamReader(new FileInputStream(inputFilename), inputCharset);
//            }
            
                                  
            //get output filename
            outputFilename = (String) parser.getOptionValue(outputFilenameOpt);
            
            if (outputFilename == null) {
                if (charset == null){
                    out = new OutputStreamWriter(System.out);
                } else {
                    out = new OutputStreamWriter(System.out, charset);
                }
            } else {
                if (verbose){
                    System.err.println("[INFO] Output file is '" + (new File(outputFilename)).getAbsolutePath() + "'");
                }
                out = new OutputStreamWriter(new FileOutputStream(outputFilename), charset);
            }            


            
            //close just in case out is the same as in
            //in.close(); in = null;
            
            //generate
            DataURIGenerator.generate(new File(inputFilename), out, mimeType, charset, verbose);
        } catch (CmdLineParser.OptionException e) {
            usage();
            System.exit(1);            
        } catch (Exception e) { 
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }            
        }
        
    }
    

    /**
     * Outputs help information to the console.
     */
    private static void usage() {
        System.out.println(
                "\nUsage: java -jar combiner-x.y.z.jar [options] [input files]\n\n"

                        + "Global Options\n"
                        + "  -h, --help            Displays this information.\n"
                        + "  --charset <charset>   Read the input file and write data URI using <charset>.\n"
                        + "  -v, --verbose         Display informational messages and warnings.\n"
                        + "  -m, --mime <type>     Mime type to encode into the data URI.\n"
                        + "  -o <file>             Place the output into <file>. Defaults to stdout.");
    }
}
