package lightbouncers.world;

import lightbouncers.file.JSONUtil;
import org.json.simple.JSONObject;

import java.io.File;

public class LevelBuilder
{
    public static Level loadLevelFromFile(File levelFile)
    {
        if(levelFile.exists() && levelFile.getAbsolutePath().toLowerCase().endsWith(".json"))
        {
            JSONObject jsonObject = JSONUtil.getJsonObject(levelFile.getAbsolutePath());
        }
        return null;
    }

    public static void saveLevelToFile(String levelFilePath)
    {

    }
}
