package vip.testops.manager.entities.vto;

import lombok.Data;

@Data
public class CoverageVTO {
    private String date;
    private double coverage;

    public CoverageVTO() {
    }

    public CoverageVTO(String date, double coverage) {
        this.date = date;
        this.coverage = coverage;
    }
}
