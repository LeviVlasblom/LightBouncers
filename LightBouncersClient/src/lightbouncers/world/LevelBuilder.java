package lightbouncers.world;

import lightbouncers.file.JSONUtil;
import lightbouncers.math.Vector2D;
import lightbouncers.objects.environment.EnvironmentObject;
import lightbouncers.objects.environment.WallBox;
import lightbouncers.objects.environment.WallCircle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LevelBuilder
{
    public static Level loadLevelFromFile(File levelFile, World world)
    {
        Level level = null;

        if(levelFile.exists() && levelFile.getAbsolutePath().toLowerCase().endsWith(".json"))
        {
            JSONObject levelObject= JSONUtil.getJsonObject(levelFile.getAbsolutePath());
            String name = levelObject.get("name").toString();
            JSONArray environmentObjects = (JSONArray)levelObject.get("enviromentobjects");

            level = new Level(name);

            for(Object object : environmentObjects)
            {
                JSONObject environmentObjectJson = (JSONObject)object;
                String type = environmentObjectJson.get("type").toString();
                Vector2D position = new Vector2D(Double.parseDouble(environmentObjectJson.get("positionx").toString()), Double.parseDouble(environmentObjectJson.get("positiony").toString()));
                double rotation = Double.parseDouble(environmentObjectJson.get("rotation").toString());

                EnvironmentObject environmentObject = null;

                if(type.equals("box"))
                {
                    int width = Integer.parseInt(environmentObjectJson.get("width").toString());
                    int height = Integer.parseInt(environmentObjectJson.get("height").toString());

                    environmentObject = new WallBox(position, rotation, world, width, height);
                }
                else if(type.equals("circle"))
                {
                    int radius = Integer.parseInt(environmentObjectJson.get("radius").toString());

                    environmentObject = new WallCircle(position, rotation, world, radius);
                }

                level.addEnviromentObject(environmentObject);
            }
        }
        return level;
    }

    public static void saveLevelToFile(String levelFilePath, Level level, boolean overrideExcistingFile)
    {
        File levelFile = new File(levelFilePath);

        if(levelFile.exists() && !overrideExcistingFile)
        {
            System.out.println("File already excists!");
        }
        else
        {
            JSONObject levelObject = new JSONObject();
            JSONArray environmentObjects = new JSONArray();

            levelObject.put("name", level.getName());

            for(EnvironmentObject environmentObject : level.getEnvironmentObjects())
            {
                JSONObject environmentObjectJson = new JSONObject();
                environmentObjectJson.put("positionx", environmentObject.getWorldPosition().x);
                environmentObjectJson.put("positiony", environmentObject.getWorldPosition().y);
                environmentObjectJson.put("rotation", environmentObject.getRotation());

                if(environmentObject instanceof WallBox)
                {
                    environmentObjectJson.put("width", ((WallBox)environmentObject).getWidth());
                    environmentObjectJson.put("height", ((WallBox)environmentObject).getHeight());
                }
                else if(environmentObject instanceof  WallCircle)
                {
                    environmentObjectJson.put("radius", ((WallCircle)environmentObject).getRadius());
                }
                environmentObjects.add(environmentObjectJson);
            }

            levelObject.put("enviromentobjects", environmentObjects);

            try
            {
                FileWriter fileWriter = new FileWriter(levelFilePath);
                fileWriter.write(levelObject.toJSONString());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
