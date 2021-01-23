package vip.testops.manager.entities.vto;

import lombok.Data;

@Data
public class BugVTO {
    private String date;
    private int bugs;

    public BugVTO() {
    }

    public BugVTO(String date, int bugs) {
        this.date = date;
        this.bugs = bugs;
    }
}
