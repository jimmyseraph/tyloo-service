package vip.testops.engine.parsers;

public class AssertionParser {

    public static boolean parse(String actual, String op, String expected){
        boolean result = false;
        switch (op){
            case "gt":
                result = Double.parseDouble(actual) > Double.parseDouble(expected);
                break;
            case "ge":
                result = Double.parseDouble(actual) >= Double.parseDouble(expected);
                break;
            case "lt":
                result = Double.parseDouble(actual) < Double.parseDouble(expected);
                break;
            case "le":
                result = Double.parseDouble(actual) <= Double.parseDouble(expected);
                break;
            case "eq":
                result = actual.equals(expected);
                break;
            case "ne":
                result = !actual.equals(expected);
                break;
            case "ct":
                result = actual.contains(expected);
                break;
            case "nc":
                result = !actual.contains(expected);
                break;
        }
        return result;
    }
}
