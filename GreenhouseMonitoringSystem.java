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
        
    }
}