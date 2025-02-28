import serial
import bluetooth

# Configurar comunicação serial com WellWatch 670
ser = serial.Serial('/dev/ttyUSB0', baudrate=9600, timeout=1)

# Configurar servidor Bluetooth
server_sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
server_sock.bind(("", bluetooth.PORT_ANY))
server_sock.listen(1)
port = server_sock.getsockname()[1]

bluetooth.advertise_service(server_sock, "WellWatchBluetooth",
                            service_classes=[bluetooth.SERIAL_PORT_CLASS],
                            profiles=[bluetooth.SERIAL_PORT_PROFILE])

print(f"Aguardando conexão Bluetooth na porta {port}...")

client_sock, address = server_sock.accept()
print(f"Conectado a {address}")

try:
    while True:
        data = ser.readline().decode().strip()
        if data:
            print(f"Dado recebido do WellWatch: {data}")
            client_sock.send(data + "\n")  # Envia os dados via Bluetooth
except KeyboardInterrupt:
    print("Encerrando...")
finally:
    client_sock.close()
    server_sock.close()
    ser.close()