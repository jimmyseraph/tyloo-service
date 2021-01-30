package vip.testops.engine.entities.enums;

public enum SuiteStatues {
    INITIAL(0, "Initial"),
    PASS(1, "Pass"),
    FAIL(2, "Fail"),
    BLOCK(3, "Block");

    private Integer key;
    private String value;

    SuiteStatues(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public static SuiteStatues getByKey(Integer key){
        for(SuiteStatues item : values()){
            if(item.getKey().equals(key)){
                return item;
            }
        }
        return null;
    }

    public static SuiteStatues getByValue(String value) {
        for(SuiteStatues item : values()){
            if(item.getValue().equals(value)){
                return item;
            }
        }
        return null;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
