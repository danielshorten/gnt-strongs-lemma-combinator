package combinator.util;

import java.util.regex.Pattern;

public class GreekCharNormalizer {
    private static String alphaOxia = new String(Character.toChars(0x1F71));
    private static String alphaTonos = new String(Character.toChars(0x03AC));
    private static String etaOxia = new String(Character.toChars(0x1F75));
    private static String epsilonOxia = new String(Character.toChars(0x1F73));
    private static String epsilonTonos = new String(Character.toChars(0x03AD));
    private static String etaTonos = new String(Character.toChars(0x03AE));
    private static String iotaOxia = new String(Character.toChars(0x1F77));
    private static String iotaTonos = new String(Character.toChars(0x03AF));
    private static String omicronOxia = new String(Character.toChars(0x1F79));
    private static String omegaOxia = new String(Character.toChars(0x1F7D));
    private static String omegaTonos = new String(Character.toChars(0x03CE));
    private static String omicronTonos = new String(Character.toChars(0x03CC));
    private static String upsilonOxia = new String(Character.toChars(0x1F7B));
    private static String upsilonTonos = new String(Character.toChars(0x03CD));
    private static String upsilonDialytikaOxia = new String(Character.toChars(0x1FE3));
    private static String upsilonDialytikaTonos = new String(Character.toChars(0x03B0));

    private static Pattern alphaPattern = Pattern.compile(alphaOxia);
    private static Pattern epsilonPattern = Pattern.compile(epsilonOxia);
    private static Pattern etaPattern = Pattern.compile(etaOxia);
    private static Pattern iotaPattern = Pattern.compile(iotaOxia);
    private static Pattern omegaPattern = Pattern.compile(omegaOxia);
    private static Pattern omicronPattern = Pattern.compile(omicronOxia);
    private static Pattern upsilonPattern = Pattern.compile(upsilonOxia);
    private static Pattern upsilonDialytikaPattern = Pattern.compile(upsilonDialytikaOxia);

    public static String normalize(String original) {
        String result;
        result = alphaPattern.matcher(original).replaceAll(alphaTonos);
        result = epsilonPattern.matcher(result).replaceAll(epsilonTonos);
        result = etaPattern.matcher(result).replaceAll(etaTonos);
        result = iotaPattern.matcher(result).replaceAll(iotaTonos);
        result = omegaPattern.matcher(result).replaceAll(omegaTonos);
        result = omicronPattern.matcher(result).replaceAll(omicronTonos);
        result = upsilonPattern.matcher(result).replaceAll(upsilonTonos);
        result = upsilonDialytikaPattern.matcher(result).replaceAll(upsilonDialytikaTonos);

        return result;
    }
}
