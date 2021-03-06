/*
 * Copyright 2014-2015 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wplatform.ddal.value;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.wplatform.ddal.message.DbException;
import com.wplatform.ddal.message.ErrorCode;
import com.wplatform.ddal.util.DateTimeUtils;
import com.wplatform.ddal.util.MathUtils;
import com.wplatform.ddal.util.StringUtils;

/**
 * Implementation of the DATE data type.
 */
public class ValueDate extends Value {

    /**
     * The precision in digits.
     */
    public static final int PRECISION = 8;

    /**
     * The display size of the textual representation of a date.
     * Example: 2000-01-02
     */
    public static final int DISPLAY_SIZE = 10;

    private final long dateValue;

    private ValueDate(long dateValue) {
        this.dateValue = dateValue;
    }

    /**
     * Get or create a date value for the given date.
     *
     * @param dateValue the date value
     * @return the value
     */
    public static ValueDate fromDateValue(long dateValue) {
        return (ValueDate) Value.cache(new ValueDate(dateValue));
    }

    /**
     * Get or create a date value for the given date.
     *
     * @param date the date
     * @return the value
     */
    public static ValueDate get(Date date) {
        return fromDateValue(DateTimeUtils.dateValueFromDate(date.getTime()));
    }

    /**
     * Calculate the date value (in the default timezone) from a given time in
     * milliseconds in UTC.
     *
     * @param ms the milliseconds
     * @return the value
     */
    public static ValueDate fromMillis(long ms) {
        return fromDateValue(DateTimeUtils.dateValueFromDate(ms));
    }

    /**
     * Parse a string to a ValueDate.
     *
     * @param s the string to parse
     * @return the date
     */
    public static ValueDate parse(String s) {
        try {
            return fromDateValue(DateTimeUtils.parseDateValue(s, 0, s.length()));
        } catch (Exception e) {
            throw DbException.get(ErrorCode.INVALID_DATETIME_CONSTANT_2,
                    e, "DATE", s);
        }
    }

    /**
     * Append a date to the string builder.
     *
     * @param buff      the target string builder
     * @param dateValue the date value
     */
    static void appendDate(StringBuilder buff, long dateValue) {
        int y = DateTimeUtils.yearFromDateValue(dateValue);
        int m = DateTimeUtils.monthFromDateValue(dateValue);
        int d = DateTimeUtils.dayFromDateValue(dateValue);
        if (y > 0 && y < 10000) {
            StringUtils.appendZeroPadded(buff, 4, y);
        } else {
            buff.append(y);
        }
        buff.append('-');
        StringUtils.appendZeroPadded(buff, 2, m);
        buff.append('-');
        StringUtils.appendZeroPadded(buff, 2, d);
    }

    public long getDateValue() {
        return dateValue;
    }

    @Override
    public Date getDate() {
        return DateTimeUtils.convertDateValueToDate(dateValue);
    }

    @Override
    public int getType() {
        return Value.DATE;
    }

    @Override
    public String getString() {
        StringBuilder buff = new StringBuilder(DISPLAY_SIZE);
        appendDate(buff, dateValue);
        return buff.toString();
    }

    @Override
    public String getSQL() {
        return "DATE '" + getString() + "'";
    }

    @Override
    public long getPrecision() {
        return PRECISION;
    }

    @Override
    public int getDisplaySize() {
        return DISPLAY_SIZE;
    }

    @Override
    protected int compareSecure(Value o, CompareMode mode) {
        return MathUtils.compareLong(dateValue, ((ValueDate) o).dateValue);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof ValueDate
                && dateValue == (((ValueDate) other).dateValue);
    }

    @Override
    public int hashCode() {
        return (int) (dateValue ^ (dateValue >>> 32));
    }

    @Override
    public Object getObject() {
        return getDate();
    }

    @Override
    public void set(PreparedStatement prep, int parameterIndex)
            throws SQLException {
        prep.setDate(parameterIndex, getDate());
    }

}
