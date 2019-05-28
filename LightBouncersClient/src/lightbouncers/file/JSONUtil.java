package lightbouncers.file;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JSONUtil
{
    public static JSONObject getJsonObject(String filePath)
    {
        JSONParser jsonParser = new JSONParser();
        Path path = Paths.get(filePath);

        try
        {
            return (JSONObject)jsonParser.parse(new FileReader(path.toFile()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
