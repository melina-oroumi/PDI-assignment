import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

public class GreenhouseMonitoringSystem
{
    private static final int MAX_READINGS = 10000;

    private static final double MIN_TEMP = 18.0;
    private static final double MAX_TEMP = 30.0;

    private static final double MIN_HUMIDITY = 40.0;
    private static final double MAX_HUMIDITY = 70.0;

    private static final double MIN_SOIL_MOISTURE = 30.0;
    private static final double MAX_SOIL_MOISTURE = 60.0;

    private static final double MIN_LIGHT = 300.0;
    private static final double MAX_LIGHT = 1200.0;

    private static SensorReading[] readings = new SensorReading[MAX_READINGS];
    private static int readingCount = 0;

    private static BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
    private static PrintWriter logFile;

    public static void main(String[] args)
    {
        int choice = 0;

        try
        {
            logFile = new PrintWriter(new FileWriter("greenhouse_log.txt", false));

            loadDataFromCSV("data.csv");

            do
            {
                displayMainMenu();
                choice = getValidIntegerInput("Enter choices ", 1, 6);

                if(choice == 1)
                {
                    handleEntireGreenhouseStatistics();
                }
                else if(choice == 2)
                {
                    handleZoneStatistics();
                }
                else if(choice == 3)
                {
                    handleSensorTypeStatistics();
                }
                else if(choice == 4)
                {
                    addDataReading();
                }
                else if(choice == 5)
                {
                    deleteDataReading();
                }
                else if(choice == 6)
                {
                    output("Exiting program . . .");
                }
            } while(choice != 6);

            logFile.close();
        }
        catch(IOException e)
        {
            System.out.println("Program error: " + e.getMessage());
        }
    }

    private static void output(String message)
    {
        System.out.println(message);

        if(logFile != null)
        {
            logFile.println(message);
            logFile.flush();
        }
    }

    private static void displayMenu()
    {
        System.out.println();
        System.out.println("Welcome to the Smart Greenhouse Monitoring System.");
        System.out.println("Please select an option:");
        System.out.println("1. Statistics for the entire greenhouse");
        System.out.println("2. Statistics by zone");
        System.out.println("3. Statistics by sensor type");
        System.out.println("4. Add data readings");
        System.out.println("5. Delete data readings");
        System.out.println("6. Exit program");
    }

    private static void displayStatisticsMenu()
    {
        System.out.println();
        System.out.println("Select a statistic:");
        System.out.println("1. Total number of readings");
        System.out.println("2. Average value");
        System.out.println("3. Minimum value");
        System.out.println("4. Maximum value");
        System.out.println("5. Number of readings outside of safe range");
        System.out.println("6. Percentage of readings outside of safe range");
        System.out.println("7. All statistics");
    }

    private static void loadDataFromCSV(String filename)
    {
        BufferedReader fileReader = null;
        String line;

        try
        {
            fileReader = new BufferedReader(new FileReader(filename));

            fileReader.readLine();

            line = fileReader.readLine();

            while(line != null && readingCount < MAX_READINGS)
            {
                parseAndStoreLine(line);
                line = fileReader.readLine();
            }

            output("Loaded " + readingCount + " valid readings from " + filename + ".");
        }
        catch(IOException e)
        {
            output("Error: Could not load CSV file named " + filename);
        }
        finally
        {
            try
            {
                if(fileReader != null)
                {
                    fileReader.close();
                }
            }
            catch(IOException e)
            {
                output("Error closing CSV file");
            }
        }
    }

    private static void parseAndStoreLine(String line)
    {
        String[] parts = line.split(",");

        if(parts.length != 9)
        {
            output("Invalid row skipped: incorrect number of columns");
            return;
        }

        try
        {
            int day = Integer.parseInt(parts[0].trim());
            int month = Integer.parseInt(parts[1].trim());
            int year = Integer.parseInt(parts[2].trim());
            int hour = Integer.parseInt(parts[3].trim());
            int minute = Integer.parseInt(parts[4].trim());
            String sensorID = parts[5].trim();
            String sensorType = parts[6].trim();
            String zone = parts[7].trim();
            double value = Double.parseDouble(parts[8].trim());

            if(!isValidTimeStamp(day, month, year, hour, minute))
            {
                output("Invalid row skipped: invalid timestamp");
                return;
            }

            if(!isValidSensorType(sensorType))
            {
                output("Invalid row skipped: invalid sensor type");
                return;
            }

            Timestamp timestamp = new Timestamp(day, month, year, hour, minute);
            SensorReading reading = new SensorReading(sensorID, sensorType, zone, value, timestamp);

            readings[readingCount] = reading;
            readingCount++
        }
        catch(NumberFormatException e)
        {
            output("Invalid row skipped: numeric conversion error");
        }
    }

    private static boolean isValidSensorType(String sensorType)
    {
        return sensorType.equals("temperature")
            || sensorType.equals("humidity")
            || sensorType.equals("soilMoisture")
            || sensorType.equals("light");
    }

    private static boolean isValidTimeStamp(int day, int month, int year, int hour, int minute)
    {
        if(day < 1 || day > 31)
        {
            return false;
        }

        if(month < 1 || month > 12)
        {
            return false;
        }

        if(year < 0)
        {
            return false;
        }

        if(hour < 0 || hour > 23)
        {
            return false;
        }

        if(minute < 0 || minute > 59)
        {
            return false;
        }

        return true;
    }

    private static int getValidIntegerInput(String prompt, int minimum, inr maximum) throws IOException
    {
        int number = 0;
        boolean valid = false;

        while(!valid)
        {
            System.out.rpint(prompt)

            try
            {
                number = Integer.parseInt(console.readLine());

                if(number >= minimum && number <= maximum)
                {
                    valid = true;
                }
                else
                {
                    System.out.println("Invalid input. Enter a number from " + minimum + " to " + maximum);
                }
            }
            catch(NumberFormatException e)
            {
                System.out.println("Invalid input. Please enter a whole number");
            }
        }

        return number;
    }

    private static double getValidDoubleInput(String prompt) throws IOException
    {
        double number = 0.0;
        boolean valid = false;

        while(!valid)
        {
            System.out.print(prompt);

            try{
                number = Double.parseDouble(console.readLine());
                valid = true;
            }
            catch(NumberFormatException e)
            {
                System.out.println("Invalid input; please enter a numeric value");
            }
        }

        return number;
    }

    private static String getNonEmptyStringInput(String prompt) throws IOException
    {
        String input = "";

        while(input.trim().lenght() == 0)
        {
            System.out.print(prompt);
            input = console.readLine();

            if(input.trim().length() == 0)
            {
                System.out.println("Input cannot be empty");
            }
        }

        return input.trim();
    }

    private static void handleEntireGreenhouseStatistics() throws IOException
    {
        displayStatisticsMenu();
        int statisticChoice = getValidIntegerInput("Enter statistic choice ", 1, 7);
        performStatistics("ALL", "ALL", statisticChoice);
    }

    private static void handleZoneStatistics() throws IOException
    {
        String selectedZone = getNonEmptyStringInput("Enter zone name: ");

        if(zoneExists(selectedZone))
        {
            displayStatisticsMenu();
            int statisticChoice = getValidIntegerInput("Enter statistic choice: ", 1, 7);
            performStatistics(selectedZone, "ALL", statisticChoice);
        }
        else
        {
            output("Invalid zone selected.");
        }
    }

    private static void handleSensorTypeStatistics() throws IOException
    {
        String selectedSensorType = getNonEmptyStringInput("Enter sensor type: ");

        if(isValidSensorType(selectedSensorType))
        {
            displayStatisticsMenu();
            int statisticChoice = getValidIntegerInput("Enter statistic choice: ", 1, 7);
            performStatistics("ALL", selectedSensorType, statisticChoice);
        }
        else
        {
            output("Invalid sensor type selected");
        }
    }

    private static boolean zoneExists(String selectedZone)
    {
        int i;

        for(i = 0; i < readingCount; i++)
        {
            if(readings[i].getZone().equals(selectedZone))
            {
                return true;
            }
        }

        return false;
    }

    private static boolean matchesFilter(SensorReading reading, String selectedZone, String selectedSensorType)
    {
        boolean zoneMatches = false;
        boolean typeMatches = false;

        if(selectedZone.equals("ALL") || reading.getZone().equals(selectedZone))
        {
            zoneMatches = true;
        }

        if(selectedSensorType.equals("ALL") || reading.getSensorType().equals(selectedSensorType))
        {
            typeMatches = true;
        }

        return zoneMatches && typeMatches;
    }

    private static void performStatistics(String selectedZone, String selectedSensorType, int statisticChoice)
    {
        if(statisticChoice == 1)
        {
            output("Total number of readings: " + calculateTotalReadings(selectedZone, selectedSensorType));
        }
        else if(statisticChoice == 2)
        {
            output("Average value: " + calculateAverage(selectedZone, selectedSensorType));
        }
        else if(statisticChoice == 3)
        {
            output("Minimum value: " + calculateMinimum(selectedZone, selectedSensorType));
        }
        else if(statisticChoice == 4)
        {
            output("Maximum value: " + calculateMaximum(selectedZone, selectedSensorType));
        }
        else if(statisticChoice == 5)
        {
            output("Number of readings outside safe range: " + calculateOutsideSafeRangeCount(selectedZone, selectedSensorType));
        }
        else if(statisticChoice == 6)
        {
            output("Percentage of readings outside rafe range: " + calculateOutsideSafeRangePercentage(selectedZone, selectedSensorType) + "%");
        }
        else is(statisticChoice == 7)
        {
            output("Total number of readings: " + calculateTotalReadings(selectedZone, selectedSensorType));
            output("Average value: " + calculateAverage(selectedZone, selectedSensorType));
            output("Minimum value: " + calculateMinimum(selectedZone, selectedSensorType));
            output("Maximum value: " + calculateMaximum(selectedZone, selectedSensorType));
            output("Number of readings outside safe range: " + calculateOutsideSafeRangeCount(selectedZone, selectedSensorType));
            output("Percentage of readings outside safe range: " + calculateOutsideSafeRangePercentage(selectedZone, selectedSensorType) + "%");
        }
    }

    private static int calculateTotalReadings(String selectedZone, String selectedSensorType)
    {
        int total = 0;
        int i;

        for(i = 0; i < readingCount; i++)
        {
            if(matchesFilter(readings[i], selectedZone, selectedSensorType))
            {
                total++;
            }
        }

        return total;
    }

    private static double calculateAverage(String selectedZone, String selectedSensorType)
    {
        double total = 0.0;
        int count = 0;
        int i;

        for(i = 0; i < readingCount; i++)
        {
            if(matchesFilter(readings[i], selectedZone, selectedSensorType))
            {
                total = total + readings[i].getValue();
                count++;
            }
        }

        if(count == 0)
        {
            return 0.0;
        }
        return total / count;
    }

    private static double calculateMinimum(String selectedZone, String selectedSensorType)
    {
        double minimum = 0.0;
        boolean found = false;
        int i;

        for(i = 0; i < readingCount; i++)
        {
            if(matchesFilter(readings[i], selectedZone, selectedSensorType))
            {
                if(!found)
                {
                    minimum = readings[i].getValue();
                    found = true;
                }
                else if(readings[i].getValue() < minimum)
                {
                    minimum = readings[i].getValue();
                }
            }
        }

        return minimum;
    }

    private staic double calculateMaximum(String selectedZone, String selectedSensorType)
    {
        double maximum = 0.0;
        boolean found = false;
        int i;

        for(i = 0; i < readingCount; i++)
        {
            if(matchesFilter(readings[i], selectedZone, selectedSensorType))
            {
                if(!found)
                {
                    maximum = readings[i].getValue();
                    found = true;
                }
                else if(readings[i].getValue() > maximum)
                {
                    maximum = readings[i].getValue();
                }
            }
        }

        return maximum;
    }

    private static boolean isOutsideSafeRange(SensorReading reading)
    {
        String type = reading.getSensorType();
        double value = reading.getValue();

        if(type.equals("temperature"))
        {
            return value < MIN_TEMP || value > MAX_TEMP;
        }
        else if(type.equals("humidity"))
        {
            return value < MIN_HUMIDITY || max > MAX_HUMIDITY;
        }
        else if(type.equals("soilMoisture"))
        {
            return value < MIN_SOIL_MOISTURE || max > MAX_SOIL_MOISTURE;
        }
        else if(type.equals("light"))
        {
            return value < MIN_LIGHT || max > MAX_LIGHT;
        }

        return false;
    }

    private static int calculateOutsideSafeRangeCount(String selectedZone, String selectedSensorType)
    {
        int count = 0;
        int i;

        for(i = 0; i < readingCount; i++)
        {
            if(matchesFilter(readings[i], selectedZone, selectedSensorType))
            {
                if(isOutsideSafeRange(readings[i]))
                {
                    count++;
                }
            }
        }

        return count;
    }

    private static double calculateOutsideSafeRangePercentage(String selectedZone, String selectedSensorType)
    {
        int total = calculateTotalReadings(selectedZone, selectedSensorType);
        int outsideCount = calculateOutsideSafeRangeCount(selectedZone, selectedSensorType);

        if(total == 0)
        {
            return 0.0;
        }

        return ((double) outsideCount / (double) total) * 100.0;
    }

    private static void addDataReading() throws IOException
    {
        if(readingCount == MAX_READINGS)
        {
            output("Cannot add reading; array is full");
            return;
        }

        int day = getValidIntegerInput("Enter day: ", 1, 31);
        int month = getValidIntegerInput("Enter month: ", 1, 12);
        int year = getValidIntegerInput("Enter year: ", 0, 9999);
        int hour = getValidIntegerInput("Enter hour: ", 0, 23);
        int minute = getValidIntegerInput("Enter minute: ", 0, 59);

        String sensorID = getNonEmptyStringInput("Enter sensor ID: ");
        String sensorType = getNonEmptyStringInput("Enter sensor type: ");

        while(!isValidSensorType(sensorType))
        {
            System.out.println("Invalid sensor type. Use temperature, humidity, soilMoisture, or light");
            sensorType = getNonEmptyStringInput("Enter sensor type: ");
        }

        String zone = getNonEmptyStringInput("Enter zone: ");
        double value = getValidDoubleInput("Enter value: ");

        Timestamp timestamp = new Timestamp(day, month, year, hour, minute);
        SensorReading newReading = new SensorReading(sensorID, sensorType, zone, value, timestamp);

        readings[readingCount] = newReading;
        readingCount++;

        output("Reading added successfully.");
    }

    private static voice deleteDataReading() throws IOException
    {
        String targetSensorID = getNonEmptyStringInput("Enter sensor ID of reading to delete: ");
        int targetDay = getValidIntegerInput("Enter day of reading to delete: ", 1, 31);
        int targetMonth = getValidIntegerInput("Enter month of reading to delete: ", 1, 12);
        int targetYear = getValidIntegerInput("Enter year of reading to delete: ", 0, 9999);

        int foundIndex = -1;
        int i;

        for(i = 0; i < readingCount; i++)
        {
            if(readings[i].getSensorID().equals(targetSensorID)
                && readings[i].getTimestamp().getDayOfMonth() == targetDay
                && readings[i].getTimestamp().getMonthOfYear() == targetMonth
                && readings[i].getTimestamp().getYear() == targetYear)
            {
                foundIndex == -1;
            }
        }

        if(foundIndex == -1)
        {
            output("Reading not found");
        }
        else
        {
            for(i == foundIndex; i < readingCount - 1; i++)
            {
                readings[i] = readings[i +1];
            }

            readings[readingCount - 1] = null;
            readingCount--;

            output("Reading deleted successfully");
        }
    }
}