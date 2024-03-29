# Лабораторная работа № 1.3 «Объектно-ориентированный лексический анализатор»
18 марта 2024 г.

# Цель работы
Целью данной работы является приобретение навыка реализации лексического анализатора
на объектно-ориентированном языке без применения каких-либо средств автоматизации
решения задачи лексического анализа.

# Индивидуальный вариант
* Целые числа: последовательности цифр определенной системы счисления, 
предваренные соответствующим индикатором, определяющим систему счисления
(для десятичных чисел — пустой индикатор, для двоичных чисел — «0b», 
для восьмеричных чисел — «0t», для шестнадцатеричных чисел — «0x», 
шестнадцатеричные цифры могут быть записаны в любом регистре).
* Ключевые слова: «and»,«or».
* Знаки операций: «(»,«)».
* Идентификаторы: последовательности латинских букв.

# Тестирование

Входные данные

```
102 fs
0b1101010 fj
0t756 fs
0xf5a8 fs
and or
0bR
0xR
(ierjg and oijerg)
0b _  _
```

Вывод на `stdout`

```
----------------PROGRAM----------------
102 fs
0b1101010 fj
0t756 fs
0xf5a8 fs
and or
0bR
0xR
(ierjg and oijerg)
0b _  _
----------------TOKENS-----------------
NUMBER  (1, 1) - (1, 4)   : 102
 IDENT  (1, 5) - (1, 7)   : fs
NUMBER  (2, 1) - (2, 10)  : 106
 IDENT  (2, 11) - (2, 13) : fj
NUMBER  (3, 1) - (3, 6)   : 494
 IDENT  (3, 7) - (3, 9)   : fs
NUMBER  (4, 1) - (4, 7)   : 62888
 IDENT  (4, 8) - (4, 10)  : fs
   AND  (5, 1) - (5, 4)   
    OR  (5, 5) - (5, 7)   
NUMBER  (6, 1) - (6, 3)   : 0
 IDENT  (6, 3) - (6, 4)   : R
NUMBER  (7, 1) - (7, 3)   : 0
 IDENT  (7, 3) - (7, 4)   : R
LPAREN  (8, 1) - (8, 2)   
 IDENT  (8, 2) - (8, 7)   : ierjg
   AND  (8, 8) - (8, 11)  
 IDENT  (8, 12) - (8, 18) : oijerg
RPAREN  (8, 18) - (8, 19) 
NUMBER  (9, 1) - (9, 3)   : 0
   END  (9, 8) - (9, 8)   
----------------MESSAGES-----------------
Error (6, 3) Expected binary number, but found R
Error (7, 3) Expected hex number, but found R
Error (9, 3) Expected binary number, but found  
Error (9, 4) unexpected character
Error (9, 7) unexpected character
----------------IDENTS-----------------
0 fs
1 fj
2 R
3 ierjg
4 oijerg
```
