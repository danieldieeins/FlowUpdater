package fr.flowarg.flowupdater.download.json;

import com.google.gson.JsonObject;
import fr.flowarg.flowupdater.utils.FlowUpdaterException;
import fr.flowarg.flowupdater.utils.IOUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class represent an MCP object.
 */
public class MCP
{
    private final String clientURL;
    private final String clientSha1;
    private final long clientSize;

    private String serverURL;
    private String serverSha1;
    private long serverSize;

    /**
     * Construct a new MCP object.
     * @param clientURL URL of client.jar
     * @param clientSha1 SHA1 of client.jar
     * @param clientSize Size (bytes) of client.jar
     * @param serverURL URL of server.jar. Can be ""
     * @param serverSha1 SHA1 of server.jar. Can be ""
     * @param serverSize Size (bytes) of server.jar. Can be -1
     * @deprecated Deprecated server download.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.6.0")
    public MCP(String clientURL, String clientSha1, long clientSize, String serverURL, String serverSha1, long serverSize)
    {
        this.clientURL = clientURL;
        this.clientSha1 = clientSha1;
        this.clientSize = clientSize;
        this.serverURL = serverURL;
        this.serverSha1 = serverSha1;
        this.serverSize = serverSize;
    }

    /**
     * Construct a new MCP object.
     * @param clientURL URL of client.jar
     * @param clientSha1 SHA1 of client.jar
     * @param clientSize Size (bytes) of client.jar
     */
    public MCP(String clientURL, String clientSha1, long clientSize)
    {
        this.clientURL = clientURL;
        this.clientSha1 = clientSha1;
        this.clientSize = clientSize;
    }
    
    /**
     * Provide an MCP instance from a JSON file.
     * Template of a JSON file :
     * {
     *     "clientURL": "https://url.com/launcher/client.jar",
     *     "clientSha1": "9b0a9d70320811e7af2e8741653f029151a6719a",
     *     "clientSize": 1234
     * }
     * @param jsonUrl the JSON file URL.
     * @return the MCP instance.
     */
    public static @NotNull MCP getMCPFromJson(URL jsonUrl)
    {
        final JsonObject object = IOUtils.readJson(jsonUrl).getAsJsonObject();
        return new MCP(object.get("clientURL").getAsString(), object.get("clientSha1").getAsString(), object.get("clientSize").getAsLong());
    }

    /**
     * Provide an MCP instance from a JSON file.
     * @param jsonUrl the JSON file URL.
     * @return the MCP instance.
     */
    public static @NotNull MCP getMCPFromJson(String jsonUrl)
    {
        try
        {
            return getMCPFromJson(new URL(jsonUrl));
        } catch (MalformedURLException e)
        {
            throw new FlowUpdaterException(e);
        }
    }

    /**
     * Return the client url.
     * @return the client url.
     */
    public String getClientURL()
    {
        return this.clientURL;
    }

    /**
     * Return the client sha1.
     * @return the client sha1.
     */
    public String getClientSha1()
    {
        return this.clientSha1;
    }

    /**
     * Return the client size.
     * @return the client size.
     */
    public long getClientSize()
    {
        return this.clientSize;
    }

    /**
     * Return the server url.
     * @return the server url.
     */
    public String getServerURL()
    {
        return this.serverURL;
    }

    /**
     * Return the server sha1.
     * @return the server sha1.
     */
    public String getServerSha1()
    {
        return this.serverSha1;
    }

    /**
     * Return the server size.
     * @return the server size.
     */
    public long getServerSize()
    {
        return this.serverSize;
    }
}