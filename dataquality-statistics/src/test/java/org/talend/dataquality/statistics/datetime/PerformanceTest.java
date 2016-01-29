package org.talend.dataquality.statistics.datetime;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceTest.class);

    private static final int REPLICATE = 333;

    private static final int INTERVAL_COUNT = 5000;

    @Test
    public void testIsDate() throws IOException {

        InputStream stream = SystemDateTimePatternManager.class.getResourceAsStream("DateSampleTable.txt");
        List<String> lines = IOUtils.readLines(stream);

        SystemDateTimePatternManager.isDate("12/02/99");// init DateTimeFormatters
        Date begin = new Date();
        LOGGER.debug("Detect date start at: " + begin);

        long currentMilliseconds = begin.getTime();

        int count = 0;
        for (int i = 1; i < lines.size(); i++) {
            for (int n = 0; n < REPLICATE; n++) {
                String line = lines.get(i);
                if (!"".equals(line.trim())) {
                    String[] sampleLine = line.trim().split("\t");
                    String sample = sampleLine[0];
                    CustomDateTimePatternManager.isDate(sample, Collections.emptyList());
                    count++;
                    if (count % INTERVAL_COUNT == 0) {
                        Date after = new Date();
                        long difference = after.getTime() - currentMilliseconds;
                        currentMilliseconds = after.getTime();
                        System.out.println("count: " + count + "\tinteval: " + difference + "ms");
                    }
                }
            }
        }

        Date end = new Date();
        LOGGER.debug("Detect date end at: " + end);
        // Assert count of matches.
        long difference = end.getTime() - begin.getTime();

        LOGGER.debug("Detect date time diff: " + difference + " ms.");
        System.out.println("Total duration IS_DATE on " + count + " samples: " + difference + "ms");

        assertTrue("The method is slower than expected", difference < 4000);
    }

    @Test
    public void testGetPatterns() throws IOException {

        InputStream stream = SystemDateTimePatternManager.class.getResourceAsStream("DateSampleTable.txt");
        List<String> lines = IOUtils.readLines(stream);

        SystemDateTimePatternManager.datePatternReplace("12/02/99");// init DateTimeFormatters
        Date begin = new Date();
        LOGGER.debug("Detect date start at: " + begin);

        long currentMilliseconds = begin.getTime();

        int count = 0;
        for (int i = 1; i < lines.size(); i++) {
            for (int n = 0; n < REPLICATE; n++) {
                String line = lines.get(i);
                if (!"".equals(line.trim())) {
                    String[] sampleLine = line.trim().split("\t");
                    String sample = sampleLine[0];
                    SystemDateTimePatternManager.datePatternReplace(sample);
                    count++;
                    if (count % INTERVAL_COUNT == 0) {
                        Date after = new Date();
                        long difference = after.getTime() - currentMilliseconds;
                        currentMilliseconds = after.getTime();
                        System.out.println("count: " + count + "\tinteval: " + difference + "ms");
                    }
                }
            }
        }

        Date end = new Date();
        LOGGER.debug("Detect date end at: " + end);
        // Assert count of matches.
        long difference = end.getTime() - begin.getTime();

        LOGGER.debug("Detect date time diff: " + difference + " ms.");
        System.out.println("Total duration GET_PATTERNS on " + count + " samples: " + difference + "ms");

        assertTrue("The method is slower than expected", difference < 5000);
    }

}
