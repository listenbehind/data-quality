// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.statistics.frequency;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class StatisticsFrequencyTest {

    private DataTypeFrequencyStatistics stats;

    @Before
    public void setUp() throws Exception {
        stats = new DataTypeFrequencyStatistics();
        for (int i = 1; i <= 10 ; i++) {
            for (int j = 0; j < i; j++) {
                stats.add(Integer.toString(i));
            }
        }
    }

    @Test
    public void testGetPage() {
        Map<String, Long> page0 = stats.getPage(0, 2);
        Map<String, Long> page2 = stats.getPage(2, 2);
        Assert.assertEquals(2, page0.keySet().size());
        Assert.assertEquals(Long.valueOf(10), page0.get("10"));
        Assert.assertEquals(Long.valueOf(9), page0.get("9"));
        // page 1 should be 8 and 7
        Assert.assertEquals(2, page2.keySet().size());
        Assert.assertEquals(Long.valueOf(6), page2.get("6"));
        Assert.assertEquals(Long.valueOf(5), page2.get("5"));

        // another page and size
        Map<String, Long> page3 = stats.getPage(3, 3);
        Assert.assertEquals(1, page3.keySet().size());
        Assert.assertEquals(Long.valueOf(1), page3.get("1"));
    }

    @Test
    public void getTopK() {
        Map<String, Long> results = stats.getTopK(3);
        Assert.assertEquals(3, results.keySet().size());
        Assert.assertEquals(Long.valueOf(10), results.get("10"));
        Assert.assertEquals(Long.valueOf(9), results.get("9"));
        Assert.assertEquals(Long.valueOf(8), results.get("8"));
    }

}
