public class SensorReading
{
    private String sensorID;
    private String sensorType;
    private String zone;
    private double value;
    private Timestamp timestamp;

    public SensorReading()
    {
        sensorID = "UNKNOWN";
        sensorType = "temperature";
        zone = "UNKNOWN";
        value = 0.0;
        timestamp = new Timestamp();
    }

    public SensorReading(String sensorID, String sensorType, String zone, double value, Timestamp timestamp)
    {
        this.sensorID = other.getSensorID();
        this.sensorType = other.getSensorType();
        this.zone = other.getZone();
        this.value = other.getValue();
        this.timestamp = new Timestamp(other.getTimestamp());
    }

    public String getSensorID()
    {
        return sensorID;
    }

    public String getSensorType()
    {
        return sensorType;
    }

    public String getZone()
    {
        return zone;
    }

    public double getValue()
    {
        return value;
    }

    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    public void setSensorID(String sensorID)
    {
        this.sensorID = sensorID;
    }

    public void setSensorType(String sensorType)
    {
        this.sensorType = sensorType;
    }

    public void setZone(String zone)
    {
        this.zone = zone;
    }

    public void setValue(double value)
    {
        this.value = value;
    }

    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = new Timestamp(timestamp);
    }

    public String toString()
    {
        return timestamp.toString() + ", " + sensorID + ", " + sensorType + ", " + zone + ", " + value;
    }
}