/*
 * Copyright 2018 SerVB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.servb.pph.gxlib.gxlcommontpl;

import com.github.servb.pph.util.helpertype.Changeable;
import com.github.servb.pph.util.staticFunction.Tracer;

/**
 * {@code gxl.common.tpl.h}.
 *
 * @author SerVB
 */
public final class Static {

    /** Prevents from creating an instance of the class. */
    private Static() {}

    /**
     * Swaps values: "b" becomes "a" and "a" becomes "b" simultaneously.
     *
     * @param <T>   Type of the values.
     * @param a     The first value.
     * @param b     The second value.
     */
    public static final <T> void iSwap(final Changeable<T> a, final Changeable<T> b) {
        Tracer.check(a.value == null || b.value == null || a.value.getClass() == b.value.getClass());

        final Changeable<T> tmp = new Changeable(a.value);
        a.value = b.value;
        b.value = tmp.value;
    }

    //<editor-fold defaultstate="collapsed" desc="iCLAMP">
    /**
     * Clamps {@code vl} between {@code mn} and {@code mx}.
     * If {@code min <= value <= max} then {@code value} is returned.
     * If {@code value < min} then {@code min} is returned.
     * Otherwise {@code max} is returned.
     *
     * @param mn    The minimum limit.
     * @param mx    The maximum limit, should be not less than the minimum limit.
     * @param vl    The value to be clamped.
     * @return      The clamped value.
     */
    public static final byte iCLAMP(final byte mn, final byte mx, final byte vl) {
        Tracer.check(mn <= mx);

        if (vl >= mn && vl <= mx ) {
            return vl;
        }
        if (vl < mn) {
            return mn;
        }
        return mx;
    }

    /**
     * Clamps {@code vl} between {@code mn} and {@code mx}.
     * If {@code min <= value <= max} then {@code value} is returned.
     * If {@code value < min} then {@code min} is returned.
     * Otherwise {@code max} is returned.
     *
     * @param mn    The minimum limit.
     * @param mx    The maximum limit, should be not less than the minimum limit.
     * @param vl    The value to be clamped.
     * @return      The clamped value.
     */
    public static final short iCLAMP(final short mn, final short mx, final short vl) {
        Tracer.check(mn <= mx);

        if (vl >= mn && vl <= mx ) {
            return vl;
        }
        if (vl < mn) {
            return mn;
        }
        return mx;
    }

    /**
     * Clamps {@code vl} between {@code mn} and {@code mx}.
     * If {@code min <= value <= max} then {@code value} is returned.
     * If {@code value < min} then {@code min} is returned.
     * Otherwise {@code max} is returned.
     *
     * @param mn    The minimum limit.
     * @param mx    The maximum limit, should be not less than the minimum limit.
     * @param vl    The value to be clamped.
     * @return      The clamped value.
     */
    public static final int iCLAMP(final int mn, final int mx, final int vl) {
        Tracer.check(mn <= mx);

        if (vl >= mn && vl <= mx ) {
            return vl;
        }
        if (vl < mn) {
            return mn;
        }
        return mx;
    }

    /**
     * Clamps {@code vl} between {@code mn} and {@code mx}.
     * If {@code min <= value <= max} then {@code value} is returned.
     * If {@code value < min} then {@code min} is returned.
     * Otherwise {@code max} is returned.
     *
     * @param mn    The minimum limit.
     * @param mx    The maximum limit, should be not less than the minimum limit.
     * @param vl    The value to be clamped.
     * @return      The clamped value.
     */
    public static final long iCLAMP(final long mn, final long mx, final long vl) {
        Tracer.check(mn <= mx);

        if (vl >= mn && vl <= mx ) {
            return vl;
        }
        if (vl < mn) {
            return mn;
        }
        return mx;
    }

    /**
     * Clamps {@code vl} between {@code mn} and {@code mx}.
     * If {@code min <= value <= max} then {@code value} is returned.
     * If {@code value < min} then {@code min} is returned.
     * Otherwise {@code max} is returned.
     *
     * @param mn    The minimum limit.
     * @param mx    The maximum limit, should be not less than the minimum limit.
     * @param vl    The value to be clamped.
     * @return      The clamped value.
     */
    public static final float iCLAMP(final float mn, final float mx, final float vl) {
        Tracer.check(mn <= mx);

        if (vl >= mn && vl <= mx ) {
            return vl;
        }
        if (vl < mn) {
            return mn;
        }
        return mx;
    }

    /**
     * Clamps {@code vl} between {@code mn} and {@code mx}.
     * If {@code min <= value <= max} then {@code value} is returned.
     * If {@code value < min} then {@code min} is returned.
     * Otherwise {@code max} is returned.
     *
     * @param mn    The minimum limit.
     * @param mx    The maximum limit, should be not less than the minimum limit.
     * @param vl    The value to be clamped.
     * @return      The clamped value.
     */
    public static final double iCLAMP(final double mn, final double mx, final double vl) {
        Tracer.check(mn <= mx);

        if (vl >= mn && vl <= mx ) {
            return vl;
        }
        if (vl < mn) {
            return mn;
        }
        return mx;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="iDIF">
    /**
     * Returns the positive difference between two values.
     *
     * @param x The first value.
     * @param y The second value.
     * @return  The positive difference.
     */
    public static final byte iDIF(final byte x, final byte y) {
        return (byte) ((x > y) ? (x - y) : (y - x));
    }

    /**
     * Returns the positive difference between two values.
     *
     * @param x The first value.
     * @param y The second value.
     * @return  The positive difference.
     */
    public static final short iDIF(final short x, final short y) {
        return (short) ((x > y) ? (x - y) : (y - x));
    }

    /**
     * Returns the positive difference between two values.
     *
     * @param x The first value.
     * @param y The second value.
     * @return  The positive difference.
     */
    public static final int iDIF(final int x, final int y) {
        return (int) ((x > y) ? (x - y) : (y - x));
    }

    /**
     * Returns the positive difference between two values.
     *
     * @param x The first value.
     * @param y The second value.
     * @return  The positive difference.
     */
    public static final long iDIF(final long x, final long y) {
        return (long) ((x > y) ? (x - y) : (y - x));
    }

    /**
     * Returns the positive difference between two values.
     *
     * @param x The first value.
     * @param y The second value.
     * @return  The positive difference.
     */
    public static final float iDIF(final float x, final float y) {
        return (float) ((x > y) ? (x - y) : (y - x));
    }

    /**
     * Returns the positive difference between two values.
     *
     * @param x The first value.
     * @param y The second value.
     * @return  The positive difference.
     */
    public static final double iDIF(final double x, final double y) {
        return (double) ((x > y) ? (x - y) : (y - x));
    }
    //</editor-fold>

    /**
     *
     *
     * @param val   The value to be aligned. Should be not negative.
     * @param al    Should be positive.
     * @return      Aligned value.
     */
    public static final int iALIGN(final int val, final int al) {
        return ((val + al - 1) / al) * al;
    }

    /**
     *
     *
     * @param val   The value to be wrapped.
     * @param minv  Should be less than {@code wrap}.
     * @param wrap
     * @return      Wrapped value.
     */
    public static final short iWRAP(final short val, final short minv, final short wrap) {
        Tracer.check(minv < wrap);

        if (val < minv) {
            return (short) (val + wrap - minv);
        }
        if (val >= wrap) {
            return (short) (val - wrap + minv);
        }
        return val;
    }
}
