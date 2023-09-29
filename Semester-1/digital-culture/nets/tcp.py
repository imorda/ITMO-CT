import socket
s = socket.socket()
s.connect(('10.8.0.1', 1234))
while True:
    data = s.recv(10000).decode("utf-8").strip()
    print('recv:', data)
    if len(data) < 1:
        break
    if len(data) == 1:
        continue
    try:
        res = eval(data)
        print('calculated:', res)
        s.send(str(res).encode("utf-8"))
    except SyntaxError:
        pass

