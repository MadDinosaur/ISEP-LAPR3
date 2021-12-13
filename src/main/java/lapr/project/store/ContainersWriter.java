package lapr.project.store;


import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ContainersWriter {

    /**
     * Hides the implicit public controller
     */
    private ContainersWriter(){}

    /**
     * Writes in a file the list of containers
     * @param containers The list of containers to be written
     */
    public static void writeContainers(List<String> containers){

        StringBuilder sb = new StringBuilder();

        for(String string : containers){
            sb.append(string);
            sb.append("\n");
        }

        writeOutput(sb.toString(),"containers");

    }

    /**
     * Writes a string
     * @param output The string
     * @param filename The file's name
     */
    public static void writeOutput(String output, String filename){
        try (FileWriter myWriter = new FileWriter("output\\sprint2\\" + filename + ".txt")) {
            if(output!=null)
                myWriter.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
