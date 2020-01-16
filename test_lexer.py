#!/usr/bin/env python3
import os

class color:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'

def valid_lexer():
    counter = 0
    tmp = 0
    test_lexer = "src/test/script/launchers/test_lex"
    valid_lexer = "src/test/deca/lexicographie/valid"
    print("Details d'execution des tests valides [1/0] ?")
    x = int(input())
    print("======================================================")
    for file in os.listdir(valid_lexer):
        tmp += 1
        execute = test_lexer + " " + valid_lexer + "/" + str(file) + " " +"2> {}.log".format(str(file))
        if x == 1:
            os.system(execute)
        if x == 0:
            os.system(execute + "> lexer.txt")
            os.system("rm lexer.txt")
        if os.stat("{}.log".format(str(file))).st_size != 0:
            print(file+color.BOLD+color.WARNING+"  *** [Test FAILED UNEXPECTED] *** "+color.ENDC)

        else:
            print(file+color.BOLD+color.HEADER+"  *** [TEST PASSED EXPECTED] *** "+color.ENDC)
            counter += 1
        print("======================================================")
    return counter, tmp


def invalid_lexer():
    counter = 0
    tmp = 0
    test_lexer = "src/test/script/launchers/test_lex"
    invalid_lexer = "src/test/deca/lexicographie/invalid"
    print("~======================================================~")
    for file in os.listdir(invalid_lexer):
        tmp += 1
        execute = test_lexer + " " + invalid_lexer + "/" + str(file) + " " +"2> {}.log".format(str(file))
        os.system(execute + "> lexer.txt")
        os.system("rm lexer.txt")
        if os.stat("{}.log".format(str(file))).st_size != 0:
            counter += 1
            fichier = open("{}.log".format(str(file)), "r")
            read = fichier.readlines()[0]
            if read[0] == 's':
                new_read = read[len(invalid_lexer) + 1:]
            else:
                new_read = read
            print(new_read+color.BOLD+color.HEADER +"  *** [Test FAILED EXPECTED] ***"+color.ENDC)
        else:
            print(file+color.BOLD+color.WARNING+"  *** [TEST PASSED UNEXPECTED] ***"+color.ENDC)

        print("~======================================================~")
    return counter, tmp


print("Tester les valides ? [1/0]")
val = int(input())
if val == 1:
    y = valid_lexer()
    if (y[0] == y[1]):
        print(color.BOLD+ color.OKGREEN+"     .-~-.-~-.-~[{} TESTS VALID SUCCESS].-~-.-~-.-~".format(str(y[1]))+color.ENDC)
        print("~======================================================~")
    else:
        print(color.BOLD+ color.FAIL+"     .-~-.-~-.-~[{} TESTS VALID ERROR].-~-.-~-.-~".format(str(y[1] -y[0]))+color.ENDC)
        print("~========================================================================~")
print("Tester les invalides ? [1/0]")
inval = int(input())
if inval == 1:
    x = invalid_lexer()
    if (x[0] == x[1]):
        print(color.BOLD+ color.OKGREEN+"     .-~-.-~-.-~[{} TESTS INVALID SUCCESS].-~-.-~-.-~".format(str(x[1]))+color.ENDC)
        print("~======================================================~")
    else:
        print(color.BOLD+ color.FAIL+"     .-~-.-~-.-~[{} TESTS INVALID ERROR].-~-.-~-.-~".format(str(x[1] - x[0]))+color.ENDC)
        print("~========================================================================~")


os.system("rm *.log")
