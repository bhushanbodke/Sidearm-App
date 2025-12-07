package com.example.sidearm

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import okhttp3.OkHttpClient
import okhttp3.Request
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import androidx.activity.OnBackPressedCallback
import android.widget.Toast


class HomeConnectionFrag: Fragment(R.layout.home_connection){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AvailableDevice.devices.observe(viewLifecycleOwner) { map ->
            val parent = view.findViewById<LinearLayout>(R.id.new_devices)
            parent.removeAllViews();
            for ((ip, device) in map) {
                val view = layoutInflater.inflate(R.layout.devices_name_header, parent, false)

                val deviceName = view.findViewById<TextView>(R.id.Devices_name);
                deviceName.text = "${device.name}'s PC "
                view.setOnClickListener {
                    connectToserver(device.ip)
                }
                parent.addView(view)
            }
        }
    }
    fun connectToserver(ip:String){
        val client = OkHttpClient();
        val request = Request.Builder().url(ip).build()
        val listener = MyWebSocketListener()
        ws = client.newWebSocket(request, listener)
    }


}

class DeviceHomeFrag: Fragment(R.layout.home_device_layout){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sendText = view.findViewById<LinearLayout>(R.id.Sendtext)
        sendText.setOnClickListener {
            (requireActivity() as MainActivity).switchFragment(SendTextFrag(),
                R.id.container)
        }
    }
}
class SendTextFrag: Fragment(R.layout.send_text_layout){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sendButton = view.findViewById<LinearLayout>(R.id.sendButton)
        sendButton.setOnClickListener {
            view.findViewById<TextView>(R.id.sucessMsg).visibility = View.VISIBLE
            lifecycleScope.launch {
                delay(2000L)
                view.findViewById<TextView>(R.id.sucessMsg).visibility = View.GONE
            }
        }
        //Display Messages
        displayMsg(view)
        
        backButton();
    }

    fun backButton(){
        var  confirmationpending = false;
        val handler = Handler(Looper.getMainLooper())
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if(confirmationpending) {
                        (requireActivity() as MainActivity).switchFragment(HomeConnectionFrag(),
                            R.id.container)
                        ws?.close(1000,"Closed the connection");
                    }
                    else{
                        confirmationpending = true;
                        Toast.makeText(requireContext(), "Tap the Back Button again to Exit", Toast.LENGTH_SHORT).show()
                        handler.postDelayed({
                            if(confirmationpending){
                                confirmationpending =false;

                            }},3000)
                    }
                }
            })
        }

    fun displayMsg(view:View){
        Messages.messages.observe(viewLifecycleOwner){ msgs ->
            val parent = view.findViewById<LinearLayout>(R.id.ReceivedMsg);
            parent.removeAllViews();
            for (msg in msgs) {
                val msgView = layoutInflater.inflate(R.layout.display_messages, parent, false)
                msgView.findViewById<TextView>(R.id.msg).text = msg
                parent.addView(msgView)
                }
            }
    }
}