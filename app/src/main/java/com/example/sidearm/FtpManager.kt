package com.example.sidearm
import kotlinx.coroutines.CoroutineScope
import okhttp3.Dispatcher
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FtpManager() {
    private val ftp_client = FTPClient()
    suspend fun connect(): Boolean = withContext(Dispatchers.IO) {
                ftp_client.enterLocalPassiveMode()
                val port = connected_device["port"]!!.toInt();
                ftp_client.connect(connected_device["ip"], port)
                val sucess =
                    ftp_client.login(connected_device["username"], connected_device["pass"])
                ftp_client.setFileType(FTP.BINARY_FILE_TYPE)
                sucess;
    }
    fun formatFileSize(sizeInBytes: Long): String {
        if (sizeInBytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(sizeInBytes.toDouble()) / Math.log10(1024.0)).toInt()
        return String.format("%.2f %s", sizeInBytes / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
    }

    fun get_file_type(name:String):String {
        val extension = name.substringAfterLast('.', "").lowercase()
        when (extension) {
            "png", "jpg", "jpeg", "gif" -> return "Image"
            "mp4", "mkv", "avi" -> return "Video"
            "txt", "log" -> return "Text"
            "zip" -> return "Zip"
            "pdf" -> return "Pdf"
            else -> return "Other"
        }
    }
    suspend fun listAllfiles(): MutableList<MutableList<String>> =withContext(Dispatchers.IO){
        val result = mutableListOf<MutableList<String>>();
        if(!ftp_client.isConnected)  result;

        val files = ftp_client.listFiles();
        for (file in files){
            if(file.isFile){
                val row = mutableListOf<String>()
                row.add("F")                         // type
                row.add(file.name)                    // name
                row.add(formatFileSize(file.size))   // size
                row.add(get_file_type(file.name))    // file type (image/video/etc.)
                result.add(row)                       // add to main list
            }
        }
        result;
    }
}