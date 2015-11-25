// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.statistics.type;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.talend.datascience.common.inference.type.SystemDatetimePatternManager;

/**
 * Customized date time pattern manager.
 * 
 * @author mzhao
 *
 */
public final class CustomDatetimePatternManager {

    /**
     * This method call the system date pattern manager to valid it again after the customized pattern does not not
     * match.
     * 
     * @param value
     * @param customizedPattern
     * @param locale
     * @return
     */
    public static boolean isDate(String value, String customizedPattern) {
        boolean isMatch = isMatchCustomizedPattern(value, customizedPattern);
        if (isMatch) {
            return true;
        }
        // validate using system pattern manager
        return SystemDatetimePatternManager.isDate(value);
    }

    public static boolean isTime(String value, String customizedPattern) {
        boolean isMatch = isMatchCustomizedPattern(value, customizedPattern);
        if (isMatch) {
            return true;
        }
        // validate using system pattern manager
        return SystemDatetimePatternManager.isTime(value);
    }

    private static boolean isMatchCustomizedPattern(String value, String customizedPattern) {
        if (customizedPattern == null) {
            return false;
        }
        try {
            DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern(customizedPattern);
            dtFormatter.parse(value);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            // Cannot create DateTimeFormatter, or input data cannot match user defined pattern.
            return false;
        }
        return true;
    }

    public static String datetimePatternReplace(String value, String customizedPattern) {
        if (customizedPattern != null) {
            boolean matchCustomizedPattern = true;
            try {
                DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern(customizedPattern);
                dtFormatter.parse(value);
            } catch (DateTimeParseException | IllegalArgumentException e) {
                // Cannot create DateTimeFormatter, or input data cannot match user defined pattern.
                matchCustomizedPattern = false;
            }
            if (matchCustomizedPattern) {
                return customizedPattern;
            }
        }
        // replace with system date pattern manager.
        return systemPatternReplace(value);
    }

    private static String systemPatternReplace(String value) {
        String pattern = SystemDatetimePatternManager.datePatternReplace(value);
        if (pattern.equals(value)) {
            pattern = SystemDatetimePatternManager.timePatternReplace(value);
        }
        return pattern;
    }
}
