package ro.stefanhalus.android.blindtransport.Models;

public class LinesFoundModel {

    private String lineName;
    private Integer lineStart;
    private Integer lineEnd;
    private Double lineDistanceBack;
    private Double lineDistanceFront;

    public LinesFoundModel(){
        this.lineName = "";
        this.lineStart = 0;
        this.lineEnd = 0;
        this.lineDistanceBack = .0;
        this.lineDistanceFront = .0;
    }

    public LinesFoundModel(String lineName, Integer lineStart, Integer lineEnd, Double lineDistanceBack, Double lineDistanceFront){
        this.lineName = lineName;
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.lineDistanceBack = lineDistanceBack;
        this.lineDistanceFront = lineDistanceFront;
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

    public Double getLineDistanceBack() {
        return lineDistanceBack;
    }

    public void setLineDistanceBack(Double lineDistanceBack) {
        this.lineDistanceBack = lineDistanceBack;
    }

    public Double getLineDistanceFront() {
        return lineDistanceFront;
    }

    public void setLineDistanceFront(Double lineDistanceFront) {
        this.lineDistanceFront = lineDistanceFront;
    }
}
