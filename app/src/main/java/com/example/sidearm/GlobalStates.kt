package com.example.sidearm
import android.R
import androidx.lifecycle.MutableLiveData
import okhttp3.WebSocket

data class Device(val name:String, val ip:String);
object AvailableDevice {
    private val _devices = MutableLiveData<MutableMap<String, Device>>(mutableMapOf())
    val devices: MutableLiveData<MutableMap<String, Device>> = _devices
    fun addDevice(device: Device){
        val current = _devices.value ?: mutableMapOf()
        current[device.ip] = device
        _devices.postValue(current)

    }
}



var ws: WebSocket? = null;
object Messages{
    var messages: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf());
    fun addMessage(msg:String){
        val current = messages.value ?: mutableListOf()
        current.add(0,msg)
        messages.postValue(current)
    }

}
var is_connected :MutableLiveData<Int> = MutableLiveData(0);
//clipboard
