package com.example.sidearm
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread


class FtpFragment : Fragment(R.layout.ftp_frag_layout){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ftpManager = FtpManager();
        lifecycleScope.launch {
            val connected: Boolean = ftpManager.connect();
            val parent = view.findViewById<LinearLayout>(R.id.files)
            if (connected) {
                println("ftp client is connect to server")
                val files = ftpManager.listAllfiles();
                for (file in files) {
                    val view = layoutInflater.inflate(R.layout.files_name_layout, parent, false);
                    view.findViewById<TextView>(R.id.filename).text = file[1]
                    view.findViewById<TextView>(R.id.fileSize).text = file[2]
                    val file_type =  view.findViewById<ImageView>(R.id.filetype)
                    if(file[3] == "Image"){file_type.setImageResource(R.drawable.baseline_image_24)}
                    if(file[3] == "Video"){file_type.setImageResource(R.drawable.baseline_video_file_24)}
                    if(file[3] == "Zip"){file_type.setImageResource(R.drawable.baseline_folder_zip_24)}
                    if(file[3] == "Pdf"){file_type.setImageResource(R.drawable.baseline_picture_as_pdf_24)}
                    if(file[3] == "Text"){file_type.setImageResource(R.drawable.baseline_text_format_24)}
                    parent.addView(view);
                }
            }
        }
    }
}