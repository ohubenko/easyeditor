package org.easyeditor;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;


public class TestRead {
    @Test
    public void testEn() throws Exception {
        TextFile textFile = new TextFile();
        File file = new File("testEn.txt");
        textFile.load(file);
        String textInFile = textFile.getTextInFile();
        Assert.assertEquals("This is English\n", textInFile);
    }

    @Test
    public void testRu() throws Exception {
        TextFile textFile = new TextFile();
        textFile.load(new File("testRu.txt"));
        String textInFile = textFile.getTextInFile();
        String need = "Это русский\n";
        need = new String(need.getBytes(), StandardCharsets.UTF_8);
        Assert.assertEquals(need, textInFile);
    }

    @Test
    public void testUa() throws Exception {
        TextFile textFile = new TextFile();
        textFile.load(new File("testUa.txt"));
        String textInFile = textFile.getTextInFile();
        String need = "Це Українська\n";
        need = new String(need.getBytes(), StandardCharsets.UTF_8);
        Assert.assertEquals(need, textInFile);
    }
}
