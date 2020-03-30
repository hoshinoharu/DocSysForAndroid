package com.rehoshi.simple.utils;

/**
 * Created by hoshino on 2019/2/1.
 */

public class NumberParser {

    public static class DoubleParser {
        public static Double valueOf(String number) {
            try {
                if (number != null) {
                    if (number.startsWith(".")) {
                        number = 0 + number;
                    }
                    if (number.endsWith(".")) {
                        number += "0";
                    }
                    return Double.valueOf(number);
                } else {
                    return 0d;
                }
            } catch (Exception e) {
                return 0d;
            }
        }

        public static double parseDouble(String number) {
            try {
                if (number != null) {
                    if (number.startsWith(".")) {
                        number = 0 + number;
                    }
                    if (number.endsWith(".")) {
                        number += 0;
                    }
                    return Double.valueOf(number);
                } else {
                    return 0;
                }
            } catch (Exception e) {
                return 0;
            }
        }
    }

    public static class IntegerParser {
        public static Integer valueOf(String number) {
            try {
                if (number != null) {
                    return Integer.valueOf(number);
                } else {
                    return 0;
                }
            } catch (Exception e) {
                return 0;
            }
        }

        public static int parseInt(String number) {
            try {
                if (number != null) {
                    return Integer.parseInt(number);
                } else {
                    return 0;
                }
            } catch (Exception e) {
                return 0;
            }
        }
    }


}
