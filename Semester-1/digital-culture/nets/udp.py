import socket

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.bind(("10.8.0.214", 666))

while True:
    data = s.recv(10000)
    print('recv:', data)
