import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.InputStream
import java.util.*

class BluetoothService(private val deviceAddress: String) {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var socket: BluetoothSocket? = null

    fun connect(): Boolean {
        val device: BluetoothDevice = bluetoothAdapter?.getRemoteDevice(deviceAddress) ?: return false
        val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID padrão para SPP
        socket = device.createRfcommSocketToServiceRecord(uuid)
        bluetoothAdapter?.cancelDiscovery()
        return try {
            socket?.connect()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun readData(): String? {
        val inputStream: InputStream? = socket?.inputStream
        return try {
            val buffer = ByteArray(1024)
            val bytesRead = inputStream?.read(buffer) ?: -1
            if (bytesRead > 0) String(buffer, 0, bytesRead) else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun close() {
        socket?.close()
    }
}