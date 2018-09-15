package ro.stefanhalus.android.blindtransport.Models;

public class BeaconFoundModel {
    private String beaconName;
    private String beaconUUID;
    private String beaconProximityUid;
    private Integer beaconMajor;
    private Integer beaconMinor;
    private Double beaconDistance;
    private Integer beaconBattery;
    private Integer lineId;
    private String lineName;
    private Integer lineStart;
    private Integer lineEnd;
    private String lineStartName;
    private String lineEndName;

    public BeaconFoundModel() {
    }

    public BeaconFoundModel(
            String beaconName,
            String beaconUUID,
            String beaconProximityUid,
            Integer beaconMajor,
            Integer beaconMinor,
            Double beaconDistance,
            Integer beaconBattery,
            Integer lineId,
            String lineName,
            Integer lineStart,
            Integer lineEnd,
            String lineStartName,
            String lineEndName) {
        this.beaconName = beaconName;
        this.beaconUUID = beaconUUID;
        this.beaconProximityUid = beaconProximityUid;
        this.beaconMajor = beaconMajor;
        this.beaconMinor = beaconMinor;
        this.beaconDistance = beaconDistance;
        this.beaconBattery = beaconBattery;
        this.lineId = lineId;
        this.lineName = lineName;
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.lineStartName = lineStartName;
        this.lineEndName = lineEndName;
    }

    public String getBeaconName() {
        return beaconName;
    }

    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
    }

    public String getBeaconUUID() {
        return beaconUUID;
    }

    public void setBeaconUUID(String beaconUUID) {
        this.beaconUUID = beaconUUID;
    }

    public String getBeaconProximityUid() {
        return beaconProximityUid;
    }

    public void setBeaconProximityUid(String beaconProximityUid) {
        this.beaconProximityUid = beaconProximityUid;
    }

    public Integer getBeaconMajor() {
        return beaconMajor;
    }

    public void setBeaconMajor(Integer beaconMajor) {
        this.beaconMajor = beaconMajor;
    }

    public Integer getBeaconMinor() {
        return beaconMinor;
    }

    public void setBeaconMinor(Integer beaconMinor) {
        this.beaconMinor = beaconMinor;
    }

    public Double getBeaconDistance() {
        return beaconDistance;
    }

    public void setBeaconDistance(Double beaconDistance) {
        this.beaconDistance = beaconDistance;
    }

    public Integer getBeaconBattery() {
        return beaconBattery;
    }

    public void setBeaconBattery(Integer beaconBattery) {
        this.beaconBattery = beaconBattery;
    }

    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Integer getLineStart() {
        return lineStart;
    }

    public void setLineStart(Integer lineStart) {
        this.lineStart = lineStart;
    }

    public Integer getLineEnd() {
        return lineEnd;
    }

    public void setLineEnd(Integer lineEnd) {
        this.lineEnd = lineEnd;
    }

    public String getLineStartName() {
        return lineStartName;
    }

    public void setLineStartName(String lineStartName) {
        this.lineStartName = lineStartName;
    }

    public String getLineEndName() {
        return lineEndName;
    }

    public void setLineEndName(String lineEndName) {
        this.lineEndName = lineEndName;
    }
}
