package lapr.project.store;

import lapr.project.mappers.dto.StorageDTO;
import lapr.project.model.Coordinate;
import lapr.project.model.Country;
import oracle.ucp.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeaDistFilesReader {


    /**
     * reads the country file and fills a list with all the countries in it
     * @param path the path to the file
     * @return a list with all the country information
     */
    public static List<Country> readCountries(String path){
        List<Country> countryList = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            reader.readLine();
            String line;
            List<String[]> dataSet = new ArrayList<>();

            while ((line = reader.readLine()) != null)
                dataSet.add(line.split(","));

            for(String[] values : dataSet){
                Country country = new Country(values[0],values[3],new Coordinate(Float.parseFloat(values[7]), Float.parseFloat(values[6])),values[1],values[2],Float.parseFloat(values[4]),values[5]);
                countryList.add(country);
            }

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return countryList;
    }

    /**
     * reads the border file and fills a list with all the borders in it
     * @param path the path to the file
     * @return a list with all the borders information
     */
    public static List<Pair<String, String>> readBorders(String path){
        List<Pair<String, String>> BorderList = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            reader.readLine();
            String line;
            List<String[]> dataSet = new ArrayList<>();

            while ((line = reader.readLine()) != null)
                dataSet.add(line.split(","));

            for(String[] values : dataSet){
                Pair<String, String> border = new Pair<>(values[0], values[1].substring(1));
                BorderList.add(border);
            }

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return BorderList;
    }

    /**
     * reads the seaDist file and fills a list with all the paths in it
     * @param path the path to the file
     * @return a list with all the port paths information
     */
    public static List<Pair<Pair<String, String>, String>> readSeaDist(String path){
        List<Pair<Pair<String, String>, String>> BorderList = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            reader.readLine();
            String line;
            List<String[]> dataSet = new ArrayList<>();

            while ((line = reader.readLine()) != null)
                dataSet.add(line.split(","));

            for(String[] values : dataSet){
                Pair<String, String> stPath = new Pair<>(values[1], values[4]);
                String dist = values[6];
                BorderList.add(new Pair<>(stPath, dist));
            }

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return BorderList;
    }
}
