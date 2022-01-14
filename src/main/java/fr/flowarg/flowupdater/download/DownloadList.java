package fr.flowarg.flowupdater.download;

import fr.flowarg.flowupdater.download.json.AssetDownloadable;
import fr.flowarg.flowupdater.download.json.Downloadable;
import fr.flowarg.flowupdater.download.json.ExternalFile;
import fr.flowarg.flowupdater.download.json.Mod;
import fr.flowarg.flowupdater.integrations.curseforgeintegration.CurseMod;
import fr.flowarg.flowupdater.integrations.optifineintegration.OptiFine;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represent information about download status. Used with {@link IProgressCallback} progress system.
 *
 * @author FlowArg
 */
public class DownloadList
{
    private final List<Downloadable> downloadableFiles = new ArrayList<>();
    private final List<AssetDownloadable> downloadableAssets = new ArrayList<>();
    private final List<ExternalFile> extFiles = new ArrayList<>();
    private final List<Mod> mods = new ArrayList<>();
    private final List<CurseMod> curseMods = new ArrayList<>();
    private final Lock updateInfoLock = new ReentrantLock();
    private OptiFine optiFine = null;
    private DownloadInfo downloadInfo;
    private boolean init = false;

    /**
     * This method initialize fields.
     */
    public void init()
    {
        if (this.init) return;

        this.downloadInfo = new DownloadInfo();
        this.downloadableFiles.forEach(downloadable -> this.downloadInfo.totalToDownloadBytes.set(this.downloadInfo.totalToDownloadBytes.get() + downloadable.getSize()));
        this.downloadableAssets.forEach(downloadable -> this.downloadInfo.totalToDownloadBytes.set(this.downloadInfo.totalToDownloadBytes.get() + downloadable.getSize()));
        this.extFiles.forEach(externalFile -> this.downloadInfo.totalToDownloadBytes.set(this.downloadInfo.totalToDownloadBytes.get() + externalFile.getSize()));
        this.mods.forEach(mod -> this.downloadInfo.totalToDownloadBytes.set(this.downloadInfo.totalToDownloadBytes.get() + mod.getSize()));
        this.curseMods.forEach(obj -> this.downloadInfo.totalToDownloadBytes.set(this.downloadInfo.totalToDownloadBytes.get() + (long)(obj.getLength())));

        this.downloadInfo.totalToDownloadFiles.set(this.downloadInfo.totalToDownloadFiles.get() + this.downloadableFiles.size() + this.downloadableAssets.size() + this.extFiles.size() + this.mods.size() + this.curseMods.size());

        if (this.optiFine != null)
        {
            this.downloadInfo.totalToDownloadBytes.set(this.downloadInfo.totalToDownloadBytes.get() + (long)(this.optiFine.getSize()));
            this.downloadInfo.totalToDownloadFiles.incrementAndGet();
        }
        this.init = true;
    }

    /**
     * This method increments the number of bytes downloaded by the number of bytes passed in parameter.
     * @param bytes number of bytes to add to downloaded bytes.
     */
    public void incrementDownloaded(long bytes)
    {
        this.updateInfoLock.lock();
        this.downloadInfo.downloadedFiles.incrementAndGet();
        this.downloadInfo.downloadedBytes.set(this.downloadInfo.downloadedBytes.get() + bytes);
        this.updateInfoLock.unlock();
    }

    /**
     * Get the total of bytes to download.
     * @return bytes to download.
     * @deprecated use {@link DownloadInfo#getTotalToDownloadBytes()} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.6.0")
    public long getTotalToDownloadBytes()
    {
        return this.downloadInfo.totalToDownloadBytes.get();
    }

    /**
     * Get the downloaded bytes.
     * @return the downloaded bytes.
     * @deprecated use {@link DownloadInfo#getDownloadedBytes()} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.6.0")
    public long getDownloadedBytes()
    {
        return this.downloadInfo.downloadedBytes.get();
    }

    /**
     * Get the number of files to download.
     * @return number of files to download.
     * @deprecated use {@link DownloadInfo#getTotalToDownloadFiles} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.6.0")
    public int getTotalToDownloadFiles()
    {
        return this.downloadInfo.totalToDownloadFiles.get();
    }

    /**
     * Get the number of downloaded files.
     * @return the number of downloaded files.
     * @deprecated use {@link DownloadInfo#getDownloadedFiles} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.6.0")
    public int getDownloadedFiles()
    {
        return this.downloadInfo.downloadedFiles.get();
    }

    /**
     * Get the new API to get information about the progress of the download.
     * @return the instance of {@link DownloadInfo}.
     */
    public DownloadInfo getDownloadInfo()
    {
        return this.downloadInfo;
    }

    /**
     * Get the queue that contains all assets to download.
     * @return the queue that contains all assets to download.
     */
    public List<AssetDownloadable> getDownloadableAssets()
    {
        return this.downloadableAssets;
    }

    /**
     * Get the list that contains all downloadable files.
     * @return the list that contains all downloadable files.
     */
    public List<Downloadable> getDownloadableFiles()
    {
        return this.downloadableFiles;
    }

    /**
     * Get the list that contains all external files.
     * @return the list that contains all external files.
     */
    public List<ExternalFile> getExtFiles()
    {
        return this.extFiles;
    }

    /**
     * Get the list that contains all mods.
     * @return the list that contains all mods.
     */
    public List<Mod> getMods()
    {
        return this.mods;
    }

    /**
     * Get the list that contains all curse mods.
     * @return the list that contains all curse mods.
     */
    public List<CurseMod> getCurseMods()
    {
        return this.curseMods;
    }

    /**
     * Get the OptiFine object.
     * @return the OptiFine object.
     */
    public OptiFine getOptiFine()
    {
        return this.optiFine;
    }

    /**
     * Define the OptiFine object.
     * @param optiFine the OptiFine object to define.
     */
    public void setOptiFine(OptiFine optiFine)
    {
        this.optiFine = optiFine;
    }

    /**
     * Clear and reset this download list object.
     */
    public void clear()
    {
        this.downloadableFiles.clear();
        this.extFiles.clear();
        this.downloadableAssets.clear();
        this.mods.clear();
        this.curseMods.clear();
        this.optiFine = null;
        this.downloadInfo.reset();
        this.init = false;
    }

    public static class DownloadInfo
    {
        private final AtomicLong totalToDownloadBytes = new AtomicLong(0);
        private final AtomicLong downloadedBytes = new AtomicLong(0);
        private final AtomicInteger totalToDownloadFiles = new AtomicInteger(0);
        private final AtomicInteger downloadedFiles = new AtomicInteger(0);

        /**
         * Reset this download info object.
         */
        public void reset()
        {
            this.totalToDownloadBytes.set(0);
            this.downloadedBytes.set(0);
            this.totalToDownloadFiles.set(0);
            this.downloadedFiles.set(0);
        }

        /**
         * Get the total of bytes to download.
         * @return bytes to download.
         */
        public long getTotalToDownloadBytes()
        {
            return this.totalToDownloadBytes.get();
        }

        /**
         * Get the downloaded bytes.
         * @return the downloaded bytes.
         */
        public long getDownloadedBytes()
        {
            return this.downloadedBytes.get();
        }

        /**
         * Get the number of files to download.
         * @return number of files to download.
         */
        public int getTotalToDownloadFiles()
        {
            return this.totalToDownloadFiles.get();
        }

        /**
         * Get the number of downloaded files.
         * @return the number of downloaded files.
         */
        public int getDownloadedFiles()
        {
            return this.downloadedFiles.get();
        }
    }
}
