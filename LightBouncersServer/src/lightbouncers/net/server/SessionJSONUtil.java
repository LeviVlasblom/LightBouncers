package lightbouncers.net.server;

import lightbouncers.net.PlayerObject;
import lightbouncers.net.ProjectileObject;
import org.json.simple.JSONObject;

public class SessionJSONUtil
{
    public static JSONObject getPlayerObjectJson(PlayerObject playerObject)
    {
        JSONObject objectJson = new JSONObject();
        objectJson.put("username", playerObject.getUsername());
        objectJson.put("radius", playerObject.getRadius());
        objectJson.put("rotation", playerObject.getRotation());
        objectJson.put("positionx", playerObject.getPosition().x);
        objectJson.put("positiony", playerObject.getPosition().y);
        objectJson.put("velocityx", playerObject.getVelocity().x);
        objectJson.put("velocityy", playerObject.getVelocity().y);
        return objectJson;
    }

    public static JSONObject getProjectileObjectJson(ProjectileObject projectileObject)
    {
        JSONObject objectJson = new JSONObject();
        objectJson.put("username", projectileObject.getUsername());
        objectJson.put("radius", projectileObject.getRadius());
        objectJson.put("positionx", projectileObject.getPosition().x);
        objectJson.put("positiony", projectileObject.getPosition().y);
        objectJson.put("velocityx", projectileObject.getVelocity().x);
        objectJson.put("velocityy", projectileObject.getVelocity().y);
        return objectJson;
    }
}
