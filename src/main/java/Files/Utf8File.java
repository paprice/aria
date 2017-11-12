/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author despa
 */
public class Utf8File {

    public static String LoadFileIntoString(String filePath) throws FileNotFoundException, IOException {
        return IOUtils.toString(new FileInputStream(filePath), "UTF-8");
    }

    public static void SaveStringIntoFile(String filePath, String content) throws IOException {
        File f = new File(filePath);
        FileUtils.writeStringToFile(f, content, "UTF-8");
    }
}
