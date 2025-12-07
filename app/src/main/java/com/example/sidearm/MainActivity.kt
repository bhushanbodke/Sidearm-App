package com.example.sidearm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //udp discovery
        receiveUdpBroadcast();
        switchFragment(HomeConnectionFrag(),R.id.container);

        is_connected.observe(this){
            if (is_connected.value == 1){
                switchFragment(DeviceHomeFrag(),R.id.container)}
            else if (is_connected.value == -1){
                switchFragment(HomeConnectionFrag(),R.id.container)
                Toast.makeText(this,"the connection is closed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun switchFragment(fragment:Fragment,id:Int){
        supportFragmentManager.beginTransaction().apply {
            replace(id, fragment)
            commit()
        }
    }
}

