inp = eval(input())
for i in inp:
    print(bin(int(i, 16)).lstrip('0b').zfill(32))
