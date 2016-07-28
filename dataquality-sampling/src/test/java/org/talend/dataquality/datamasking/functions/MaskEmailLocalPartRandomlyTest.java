package org.talend.dataquality.datamasking.functions;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.talend.dataquality.duplicating.RandomWrapper;

public class MaskEmailLocalPartRandomlyTest {

    private String output;

    private MaskEmailLocalPartRandomly maskEmailLocalPart = new MaskEmailLocalPartRandomly();

    private String mail = "jugonzalez@talend.com";

    @Test
    public void testOneGoodInput() {
        maskEmailLocalPart.parse("test.com", false, new RandomWrapper(42));
        output = maskEmailLocalPart.generateMaskedRow(mail);
        Assert.assertEquals(output, "test.com@talend.com");
    }

    @Test
    public void test1OneGoodInputWithSpace() {
        maskEmailLocalPart.parse("", false, new RandomWrapper(42));
        output = maskEmailLocalPart.generateMaskedRow(mail);
        Assert.assertEquals(output, "@talend.com");
    }

    @Test
    public void testServeralGoodInputs() {
        maskEmailLocalPart.parse("aol.com, att.net, comcast.net, facebook.com, gmail.com, gmx.com", false, new RandomWrapper(42));
        for (int i = 0; i < 20; i++) {
            output = maskEmailLocalPart.generateMaskedRow(mail);
            Assert.assertTrue(!output.equals(mail));
        }
    }

    @Test
    public void testServeralGoodInputsWithSpace() {
        maskEmailLocalPart.parse("nelson  ,  quentin, ", false, new RandomWrapper(42));
        List<String> results = Arrays.asList("nelson@talend.com", "quentin@talend.com");
        for (int i = 0; i < 20; i++) {
            output = maskEmailLocalPart.generateMaskedRow(mail);
            Assert.assertTrue(results.contains(output));
        }
    }

    @Test
    public void test1GoodLocalFile() throws URISyntaxException {
        String path = this.getClass().getResource("data/domain.txt").toURI().getPath();
        maskEmailLocalPart.parse(path, false, new RandomWrapper(42));
        for (int i = 0; i < 20; i++) {
            output = maskEmailLocalPart.generateMaskedRow(mail);
            Assert.assertTrue(!output.equals(mail));
        }
    }

    @Test
    public void testNullEmail() {
        maskEmailLocalPart.parse("hehe", false, new RandomWrapper(42));
        output = maskEmailLocalPart.generateMaskedRow(null);
        Assert.assertTrue(output.isEmpty());
    }

    @Test
    public void testNotKeepNullEmail() {
        maskEmailLocalPart.parse("hehe", true, new RandomWrapper(42));
        output = maskEmailLocalPart.generateMaskedRow(null);
        Assert.assertTrue(output == null);
    }

    @Test
    public void testEmptyEmail() {
        output = maskEmailLocalPart.generateMaskedRow("");
        Assert.assertTrue(output.isEmpty());
    }

    @Test
    public void testWrongFormat() {
        output = maskEmailLocalPart.generateMaskedRow("hehe");
        Assert.assertEquals(output, "hehe");
    }
}
