package com.mycompany.plugins.example

import android.Manifest
import android.app.ActivityManager
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import com.getcapacitor.JSArray
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.annotation.Permission
import com.getcapacitor.annotation.PermissionCallback
import java.io.File
import java.io.RandomAccessFile
import java.text.SimpleDateFormat
import java.util.*

@CapacitorPlugin(
    name = "DeepDeviceAccess",
    permissions = [
        Permission(strings = [Manifest.permission.READ_PHONE_STATE], alias = "phoneState"),
        Permission(strings = [Manifest.permission.ACCESS_NETWORK_STATE], alias = "networkState"),
        Permission(strings = [Manifest.permission.BLUETOOTH], alias = "bluetooth"),
        Permission(strings = [Manifest.permission.BLUETOOTH_CONNECT], alias = "bluetoothConnect"),
        Permission(strings = [Manifest.permission.BLUETOOTH_SCAN], alias = "bluetoothScan"),
        Permission(strings = [Manifest.permission.ACCESS_WIFI_STATE], alias = "wifiState"),
        Permission(strings = [Manifest.permission.ACCESS_FINE_LOCATION], alias = "location"),
        Permission(strings = [Manifest.permission.ACCESS_COARSE_LOCATION], alias = "coarseLocation"),
        Permission(strings = [Manifest.permission.READ_EXTERNAL_STORAGE], alias = "storage"),
        Permission(strings = [Manifest.permission.READ_MEDIA_IMAGES], alias = "mediaImages"),
        Permission(strings = [Manifest.permission.READ_CONTACTS], alias = "contacts")
    ]
)
class DeepDeviceAccessPlugin : Plugin() {

    @PluginMethod
    fun requestAllPermissions(call: PluginCall) {
        try {
            val permissionsToRequest = mutableListOf<String>()
            
            // Always request these permissions
            permissionsToRequest.add("location")
            permissionsToRequest.add("contacts")
            permissionsToRequest.add("phoneState")
            
            // Add media permissions based on Android version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionsToRequest.add("mediaImages")
            } else {
                permissionsToRequest.add("storage")
            }
            
            // Add Bluetooth permissions for Android 12+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissionsToRequest.add("bluetoothConnect")
                permissionsToRequest.add("bluetoothScan")
            }
            
            // Request all permissions using aliases
            requestPermissionForAliases(permissionsToRequest.toTypedArray(), call, "permissionsCallback")
        } catch (e: Exception) {
            call.reject("Error requesting permissions: ${e.message}")
        }
    }
    
    @PermissionCallback
    private fun permissionsCallback(call: PluginCall) {
        val result = JSObject()
        
        // Check which permissions were granted
        val locationGranted = getPermissionState("location") == com.getcapacitor.PermissionState.GRANTED
        val contactsGranted = getPermissionState("contacts") == com.getcapacitor.PermissionState.GRANTED
        val phoneStateGranted = getPermissionState("phoneState") == com.getcapacitor.PermissionState.GRANTED
        
        var mediaGranted = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mediaGranted = getPermissionState("mediaImages") == com.getcapacitor.PermissionState.GRANTED
        } else {
            mediaGranted = getPermissionState("storage") == com.getcapacitor.PermissionState.GRANTED
        }
        
        var bluetoothGranted = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bluetoothGranted = getPermissionState("bluetoothConnect") == com.getcapacitor.PermissionState.GRANTED
        }
        
        result.put("location", locationGranted)
        result.put("contacts", contactsGranted)
        result.put("phoneState", phoneStateGranted)
        result.put("media", mediaGranted)
        result.put("bluetooth", bluetoothGranted)
        result.put("allGranted", locationGranted && contactsGranted && phoneStateGranted && mediaGranted && bluetoothGranted)
        
        call.resolve(result)
    }

    @PluginMethod
    fun getDeviceIdentifiers(call: PluginCall) {
        try {
            val result = JSObject()
            
            result.put("androidId", Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
            
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    result.put("serial", Build.getSerial())
                } else {
                    @Suppress("DEPRECATION")
                    result.put("serial", Build.SERIAL)
                }
            } catch (e: Exception) {
                result.put("serial", "Restricted")
            }
            
            result.put("brand", Build.BRAND)
            result.put("manufacturer", Build.MANUFACTURER)
            result.put("model", Build.MODEL)
            result.put("device", Build.DEVICE)
            result.put("product", Build.PRODUCT)
            result.put("hardware", Build.HARDWARE)
            result.put("board", Build.BOARD)
            result.put("display", Build.DISPLAY)
            result.put("fingerprint", Build.FINGERPRINT)
            result.put("bootloader", Build.BOOTLOADER)
            result.put("tags", Build.TAGS)
            
            call.resolve(result)
        } catch (e: Exception) {
            call.reject("Error getting identifiers: ${e.message}")
        }
    }

    @PluginMethod
    fun getInstalledApps(call: PluginCall) {
        try {
            val pm = context.packageManager
            val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val userAppList = JSArray()
            val systemAppList = JSArray()
            var userCount = 0
            var systemCount = 0

            for (app in apps) {
                val isSystemApp = (app.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                
                val appInfo = JSObject()
                appInfo.put("packageName", app.packageName)
                appInfo.put("appName", pm.getApplicationLabel(app).toString())
                appInfo.put("isSystemApp", isSystemApp)
                
                try {
                    val packageInfo = pm.getPackageInfo(app.packageName, 0)
                    appInfo.put("versionName", packageInfo.versionName ?: "Unknown")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        appInfo.put("versionCode", packageInfo.longVersionCode.toInt())
                    } else {
                        @Suppress("DEPRECATION")
                        appInfo.put("versionCode", packageInfo.versionCode)
                    }
                } catch (e: Exception) {
                    appInfo.put("versionName", "Unknown")
                    appInfo.put("versionCode", 0)
                }
                
                if (isSystemApp) {
                    systemAppList.put(appInfo)
                    systemCount++
                } else {
                    userAppList.put(appInfo)
                    userCount++
                }
            }

            val result = JSObject()
            result.put("userApps", userAppList)
            result.put("systemApps", systemAppList)
            result.put("totalApps", apps.size)
            result.put("userAppsCount", userCount)
            result.put("systemAppsCount", systemCount)
            call.resolve(result)
        } catch (e: Exception) {
            call.reject("Error getting apps: ${e.message}")
        }
    }

    @PluginMethod
    fun getCPUInfo(call: PluginCall) {
        try {
            val result = JSObject()
            
            result.put("cores", Runtime.getRuntime().availableProcessors())
            
            try {
                val cpuFreq = readCPUFrequency()
                result.put("currentFrequency", cpuFreq.current)
                result.put("minFrequency", cpuFreq.min)
                result.put("maxFrequency", cpuFreq.max)
            } catch (e: Exception) {
                result.put("currentFrequency", 0)
                result.put("minFrequency", 0)
                result.put("maxFrequency", 0)
            }
            
            try {
                val cpuUsage = readCPUUsage()
                result.put("cpuUsage", cpuUsage)
            } catch (e: Exception) {
                result.put("cpuUsage", 0.0)
            }
            
            result.put("architecture", Build.SUPPORTED_ABIS[0])
            
            call.resolve(result)
        } catch (e: Exception) {
            call.reject("Error getting CPU info: ${e.message}")
        }
    }

    @PluginMethod
    fun getMemoryInfo(call: PluginCall) {
        try {
            val result = JSObject()
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memInfo)
            
            val totalRAMMB = memInfo.totalMem / (1024 * 1024)
            val availableRAMMB = memInfo.availMem / (1024 * 1024)
            val usedRAMMB = totalRAMMB - availableRAMMB
            
            result.put("totalRAM", totalRAMMB)
            result.put("availableRAM", availableRAMMB)
            result.put("usedRAM", usedRAMMB)
            result.put("ramPercentage", ((usedRAMMB.toDouble() / totalRAMMB * 100)).toInt())
            result.put("isLowMemory", memInfo.lowMemory)
            
            val stat = StatFs(Environment.getDataDirectory().path)
            val totalStorage = stat.blockCountLong * stat.blockSizeLong
            val availableStorage = stat.availableBlocksLong * stat.blockSizeLong
            val usedStorage = totalStorage - availableStorage
            
            result.put("totalStorage", (totalStorage / (1024 * 1024 * 1024)))
            result.put("availableStorage", (availableStorage / (1024 * 1024 * 1024)))
            result.put("usedStorage", (usedStorage / (1024 * 1024 * 1024)))
            result.put("storagePercentage", ((usedStorage.toDouble() / totalStorage * 100)).toInt())
            
            call.resolve(result)
        } catch (e: Exception) {
            call.reject("Error getting memory info: ${e.message}")
        }
    }

    @PluginMethod
    fun getDetailedBatteryInfo(call: PluginCall) {
        try {
            val result = JSObject()
            val batteryIntent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            
            if (batteryIntent != null) {
                val level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                val percentage = (level.toFloat() / scale.toFloat() * 100).toInt()
                
                result.put("level", percentage)
                result.put("health", getBatteryHealth(batteryIntent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)))
                result.put("temperature", batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10.0)
                result.put("voltage", batteryIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1))
                result.put("technology", batteryIntent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Unknown")
                result.put("plugType", getPlugType(batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)))
                result.put("isCharging", batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1) == BatteryManager.BATTERY_STATUS_CHARGING)
                
                try {
                    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
                    val capacity = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)
                    result.put("capacity", capacity / 1000)
                } catch (e: Exception) {
                    result.put("capacity", 0)
                }
            }
            
            call.resolve(result)
        } catch (e: Exception) {
            call.reject("Error getting battery info: ${e.message}")
        }
    }

    @PluginMethod
    fun getAllSensors(call: PluginCall) {
        try {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)
            val sensorList = JSArray()
            
            for (sensor in sensors) {
                val sensorInfo = JSObject()
                sensorInfo.put("name", sensor.name)
                sensorInfo.put("type", getSensorType(sensor.type))
                sensorInfo.put("vendor", sensor.vendor)
                sensorInfo.put("power", sensor.power)
                sensorInfo.put("maxRange", sensor.maximumRange)
                sensorInfo.put("resolution", sensor.resolution)
                sensorList.put(sensorInfo)
            }
            
            val result = JSObject()
            result.put("sensors", sensorList)
            result.put("totalSensors", sensors.size)
            call.resolve(result)
        } catch (e: Exception) {
            call.reject("Error getting sensors: ${e.message}")
        }
    }

    @PluginMethod
    fun getNetworkDetails(call: PluginCall) {
        try {
            val result = JSObject()
            
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            
            // WiFi SSID requires location permission on Android 12+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    result.put("wifiSSID", wifiInfo.ssid.replace("\"", ""))
                } else {
                    result.put("wifiSSID", "Location permission required")
                }
            } else {
                result.put("wifiSSID", wifiInfo.ssid.replace("\"", ""))
            }
            
            result.put("wifiLinkSpeed", wifiInfo.linkSpeed)
            result.put("wifiRSSI", wifiInfo.rssi)
            result.put("wifiIPAddress", intToIp(wifiInfo.ipAddress))
            
            // Telephony info - requires READ_PHONE_STATE
            try {
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    result.put("networkOperator", telephonyManager.networkOperatorName ?: "Unknown")
                    result.put("simOperator", telephonyManager.simOperatorName ?: "Unknown")
                    result.put("networkType", getNetworkType(telephonyManager.networkType))
                    result.put("phoneType", getPhoneType(telephonyManager.phoneType))
                    result.put("isNetworkRoaming", telephonyManager.isNetworkRoaming)
                    
                    val simState = when (telephonyManager.simState) {
                        TelephonyManager.SIM_STATE_READY -> "Ready"
                        TelephonyManager.SIM_STATE_ABSENT -> "No SIM"
                        TelephonyManager.SIM_STATE_PIN_REQUIRED -> "PIN Required"
                        TelephonyManager.SIM_STATE_PUK_REQUIRED -> "PUK Required"
                        TelephonyManager.SIM_STATE_NETWORK_LOCKED -> "Network Locked"
                        else -> "Unknown"
                    }
                    result.put("simState", simState)
                } else {
                    result.put("networkOperator", "Permission required")
                    result.put("simOperator", "Permission required")
                }
            } catch (e: Exception) {
                result.put("telephonyError", e.message)
            }
            
            call.resolve(result)
        } catch (e: Exception) {
            call.reject("Error getting network details: ${e.message}")
        }
    }
    
    private fun getPhoneType(type: Int): String {
        return when (type) {
            TelephonyManager.PHONE_TYPE_GSM -> "GSM"
            TelephonyManager.PHONE_TYPE_CDMA -> "CDMA"
            TelephonyManager.PHONE_TYPE_SIP -> "SIP"
            TelephonyManager.PHONE_TYPE_NONE -> "None"
            else -> "Unknown"
        }
    }

    @PluginMethod
    fun getBluetoothDevices(call: PluginCall) {
        try {
            val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val bluetoothAdapter = bluetoothManager.adapter
            
            val result = JSObject()
            
            if (bluetoothAdapter == null) {
                result.put("supported", false)
                call.resolve(result)
                return
            }
            
            result.put("supported", true)
            result.put("enabled", bluetoothAdapter.isEnabled)
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    result.put("name", bluetoothAdapter.name ?: "Unknown")
                    val pairedDevices = bluetoothAdapter.bondedDevices
                    val devicesList = JSArray()
                    
                    for (device in pairedDevices) {
                        val deviceInfo = JSObject()
                        deviceInfo.put("name", device.name ?: "Unknown")
                        deviceInfo.put("address", device.address)
                        devicesList.put(deviceInfo)
                    }
                    
                    result.put("pairedDevices", devicesList)
                    result.put("pairedCount", pairedDevices.size)
                } else {
                    result.put("name", "Permission required")
                    result.put("pairedCount", 0)
                    result.put("pairedDevices", JSArray())
                }
            } else {
                @Suppress("DEPRECATION")
                val pairedDevices = bluetoothAdapter.bondedDevices
                result.put("name", bluetoothAdapter.name ?: "Unknown")
                val devicesList = JSArray()
                
                for (device in pairedDevices) {
                    val deviceInfo = JSObject()
                    @Suppress("DEPRECATION")
                    deviceInfo.put("name", device.name ?: "Unknown")
                    deviceInfo.put("address", device.address)
                    devicesList.put(deviceInfo)
                }
                
                result.put("pairedDevices", devicesList)
                result.put("pairedCount", pairedDevices.size)
            }
            
            call.resolve(result)
        } catch (e: Exception) {
            call.reject("Error getting Bluetooth info: ${e.message}")
        }
    }

    @PluginMethod
    fun enableLocationServices(call: PluginCall) {
        try {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            
            val result = JSObject()
            result.put("opened", true)
            call.resolve(result)
        } catch (e: Exception) {
            call.reject("Error opening location settings: ${e.message}")
        }
    }

    @PluginMethod
    fun getLocationInfo(call: PluginCall) {
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val result = JSObject()
            
            result.put("gpsEnabled", locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            result.put("networkEnabled", locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                try {
                    var lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    
                    if (lastLocation == null || System.currentTimeMillis() - lastLocation.time > 300000) {
                        val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (networkLocation != null) {
                            lastLocation = networkLocation
                        }
                    }
                    
                    if (lastLocation != null) {
                        result.put("latitude", lastLocation.latitude)
                        result.put("longitude", lastLocation.longitude)
                        result.put("accuracy", lastLocation.accuracy.toDouble())
                        result.put("altitude", lastLocation.altitude)
                        result.put("timestamp", lastLocation.time)
                        result.put("provider", lastLocation.provider ?: "unknown")
                        
                        val ageSeconds = (System.currentTimeMillis() - lastLocation.time) / 1000
                        result.put("ageSeconds", ageSeconds)
                    } else {
                        result.put("noLocation", true)
                        result.put("message", "No cached location available. Please wait for GPS to acquire position.")
                    }
                } catch (e: Exception) {
                    result.put("error", e.message)
                }
            } else {
                result.put("permissionDenied", true)
            }
            
            call.resolve(result)
        } catch (e: Exception) {
            call.reject("Error getting location: ${e.message}")
        }
    }

    @PluginMethod
    fun getContacts(call: PluginCall) {
        try {
            val result = JSObject()
            val contactsList = JSArray()
            
            if (context.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                val cursor = context.contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC"
                )
                
                var count = 0
                cursor?.use {
                    val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
                    val hasPhoneIndex = it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    
                    while (it.moveToNext()) {
                        val contactInfo = JSObject()
                        val contactId = it.getString(idIndex)
                        val name = it.getString(nameIndex)
                        val hasPhone = it.getInt(hasPhoneIndex) > 0
                        
                        contactInfo.put("id", contactId)
                        contactInfo.put("name", name)
                        
                        if (hasPhone) {
                            val phones = getContactPhones(contactId)
                            contactInfo.put("phones", phones)
                        } else {
                            contactInfo.put("phones", JSArray())
                        }
                        
                        contactsList.put(contactInfo)
                        count++
                    }
                }
                
                result.put("contacts", contactsList)
                result.put("totalContacts", count)
            } else {
                result.put("permissionDenied", true)
                result.put("contacts", JSArray())
                result.put("totalContacts", 0)
            }
            
            call.resolve(result)
        } catch (e: Exception) {
            call.reject("Error getting contacts: ${e.message}")
        }
    }
    
    private fun getContactPhones(contactId: String): JSArray {
        val phones = JSArray()
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            arrayOf(contactId),
            null
        )
        
        cursor?.use {
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val typeIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)
            
            while (it.moveToNext()) {
                val phoneInfo = JSObject()
                phoneInfo.put("number", it.getString(numberIndex))
                phoneInfo.put("type", getPhoneTypeLabel(it.getInt(typeIndex)))
                phones.put(phoneInfo)
            }
        }
        
        return phones
    }
    
    private fun getPhoneTypeLabel(type: Int): String {
        return when (type) {
            ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> "Home"
            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> "Mobile"
            ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> "Work"
            ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK -> "Work Fax"
            ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME -> "Home Fax"
            ContactsContract.CommonDataKinds.Phone.TYPE_PAGER -> "Pager"
            ContactsContract.CommonDataKinds.Phone.TYPE_OTHER -> "Other"
            else -> "Unknown"
        }
    }

    @PluginMethod
    fun getRecentFiles(call: PluginCall) {
        try {
            val result = JSObject()
            val recentFiles = JSArray()
            val recentPhotos = JSArray()
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (context.checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    getRecentPhotosFromMediaStore(recentPhotos)
                } else {
                    result.put("photosPermissionDenied", true)
                }
            } else {
                if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    getRecentPhotosFromMediaStore(recentPhotos)
                } else {
                    result.put("photosPermissionDenied", true)
                }
            }
            
            result.put("recentFiles", recentFiles)
            result.put("recentPhotos", recentPhotos)
            call.resolve(result)
        } catch (e: Exception) {
            call.reject("Error getting recent files: ${e.message}")
        }
    }

    private fun getRecentPhotosFromMediaStore(photosList: JSArray) {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATA
        )
        
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        
        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            
            var count = 0
            while (cursor.moveToNext() && count < 50) {
                val photoInfo = JSObject()
                photoInfo.put("id", cursor.getLong(idColumn))
                photoInfo.put("name", cursor.getString(nameColumn))
                photoInfo.put("size", cursor.getLong(sizeColumn))
                photoInfo.put("dateAdded", cursor.getLong(dateColumn))
                photoInfo.put("path", cursor.getString(pathColumn))
                
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                photoInfo.put("dateAddedFormatted", dateFormat.format(Date(cursor.getLong(dateColumn) * 1000)))
                
                photosList.put(photoInfo)
                count++
            }
        }
    } 

    // Helper functions
    private data class CPUFrequency(val current: Long, val min: Long, val max: Long)

    private fun readCPUFrequency(): CPUFrequency {
        val current = File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq").readText().trim().toLong() / 1000
        val min = File("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq").readText().trim().toLong() / 1000
        val max = File("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq").readText().trim().toLong() / 1000
        return CPUFrequency(current, min, max)
    }

    private fun readCPUUsage(): Double {
        val reader = RandomAccessFile("/proc/stat", "r")
        val load = reader.readLine()
        reader.close()
        
        val toks = load.split(" +".toRegex())
        val idle = toks[4].toLong()
        val cpu = toks.slice(1..4).sumOf { it.toLong() }
        
        return ((cpu - idle).toDouble() / cpu * 100)
    }

    private fun getBatteryHealth(health: Int): String {
        return when (health) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
            BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
            else -> "Unknown"
        }
    }

    private fun getPlugType(plugged: Int): String {
        return when (plugged) {
            BatteryManager.BATTERY_PLUGGED_AC -> "AC"
            BatteryManager.BATTERY_PLUGGED_USB -> "USB"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless"
            else -> "Not Plugged"
        }
    }

    private fun getSensorType(type: Int): String {
        return when (type) {
            Sensor.TYPE_ACCELEROMETER -> "Accelerometer"
            Sensor.TYPE_GYROSCOPE -> "Gyroscope"
            Sensor.TYPE_LIGHT -> "Light"
            Sensor.TYPE_MAGNETIC_FIELD -> "Magnetic Field"
            Sensor.TYPE_PRESSURE -> "Pressure"
            Sensor.TYPE_PROXIMITY -> "Proximity"
            Sensor.TYPE_TEMPERATURE -> "Temperature"
            Sensor.TYPE_GRAVITY -> "Gravity"
            Sensor.TYPE_STEP_COUNTER -> "Step Counter"
            Sensor.TYPE_HEART_RATE -> "Heart Rate"
            else -> "Type $type"
        }
    }

    private fun intToIp(ip: Int): String {
        return "${ip and 0xFF}.${ip shr 8 and 0xFF}.${ip shr 16 and 0xFF}.${ip shr 24 and 0xFF}"
    }

    private fun getNetworkType(type: Int): String {
        return when (type) {
            TelephonyManager.NETWORK_TYPE_LTE -> "4G LTE"
            TelephonyManager.NETWORK_TYPE_NR -> "5G"
            TelephonyManager.NETWORK_TYPE_HSDPA -> "3G HSDPA"
            TelephonyManager.NETWORK_TYPE_EDGE -> "2G EDGE"
            else -> "Type $type"
        }
    }
}