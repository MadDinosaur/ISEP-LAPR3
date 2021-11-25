package lapr.project.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetSize {

    /**
     * return the size of a Result Set and then set's it to the starting position
     * @param resultSet the set to be evaluated
     * @return returns the amount of rows in a set
     * @throws SQLException throws the exception if the set is invalid
     */
    public static int size(ResultSet resultSet) throws SQLException {
        int size =0;
        if (resultSet != null)
        {
            resultSet.last();
            size = resultSet.getRow();
            resultSet.beforeFirst();
        }
        return size;
    }

}
