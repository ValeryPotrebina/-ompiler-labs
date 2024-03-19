import java.util.ArrayList;
public class Automata {

//    index of array is current condition
    final static DomainTag[] states = {
            DomainTag.START,
            DomainTag.IDENT,
            DomainTag.NUMBER,
            DomainTag.IDENT,
            DomainTag.AX,
            DomainTag.IDENT,
            DomainTag.IDENT,
            DomainTag.EAX,
            DomainTag.IDENT,
            DomainTag.IDENT,
            DomainTag.RAX,
            DomainTag.LPAREN,
            DomainTag.RPAREN,
            DomainTag.COMMENT,
            DomainTag.SPACE

    };
    final static boolean[] isFinal = {
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
    };

    // space - [ \t]
    // line - [\n\r]
    // eaxr
    //Comment
    // Ident - [bcdfghijklmnopqstuvwyz] and capital letters
    // digit - [0123456789]
    // Other - any other symbols

//    Первый индекс - состояние в котором находимся
//    Второй индекс - индекс фактор группы

    /*
    0 [label="start" ];
    1 [label="ident" shape="doublecircle"]
    2 [label="numbers" shape="doublecircle"]
    3 [label="a" shape="doublecircle"]
    4 [label="ax" shape="doublecircle"]
    5 [label="e" shape="doublecircle"]
    6 [label="ea" shape="doublecircle"]
    7 [label="eax" shape="doublecircle"]
    8 [label="r" shape="doublecircle"]
    9 [label="ra" shape="doublecircle"]
    10 [label="rax" shape="doublecircle"]
    11 [label="lparen" shape="doublecircle"]
    12 [label="rparen", shape="doublecircle"]
    13 [label="comment" shape="doublecircle"]
    14 [label="space" shape="doublecircle"];
    */
    final static int[][] transitionTable = {
            //Id Num Com Spa Lin  A   E  X   R   LP  RP  OTHER
            { 1,  2, 13, 14, 14,  3,  5,  1,  8, 11, 12, -1}, // start
            { 1,  1,  0,  0,  0,  1,  1,  1,  1,  0,  0, -1}, // ident
            { 0,  2,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1}, // numbers
            { 1,  1,  0,  0,  0,  1,  1,  4,  1,  0,  0, -1}, // a
            { 1,  1,  0,  0,  0,  1,  1,  1,  1,  0,  0, -1}, // ax
            { 1,  1,  0,  0,  0,  6,  1,  1,  1,  0,  0, -1}, // e
            { 1,  1,  0,  0,  0,  1,  1,  7,  1,  0,  0, -1}, // ea
            { 1,  1,  0,  0,  0,  1,  1,  1,  1,  0,  0, -1}, // eax
            { 1,  1,  0,  0,  0,  9,  1,  1,  1,  0,  0, -1}, // r
            { 1,  1,  0,  0,  0,  1,  1, 10,  1,  0,  0, -1}, // ra
            { 1,  1,  0,  0,  0,  1,  1,  1,  1,  0,  0, -1}, // rax
            { 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1}, // lparen
            { 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1}, // lparen
            {13, 13, 13, 13,  0, 13, 13, 13, 13, 13, 13, 13}, // comment
            { 0,  0,  0, 14, 14,  0,  0,  0,  0,  0,  0, -1}  // space
};


    public static int getFactorId(char symbol) {
        if (Character.isDigit(symbol))
            return FactorGroup.NUMBER.ordinal();
        if (Character.isLetter(symbol)){
            if (Character.toLowerCase(symbol) == 'a')
                return FactorGroup.A.ordinal();
            if (Character.toLowerCase(symbol) == 'e')
                return FactorGroup.E.ordinal();
            if (Character.toLowerCase(symbol) == 'x')
                return FactorGroup.X.ordinal();
            if (Character.toLowerCase(symbol) == 'r')
                return FactorGroup.R.ordinal();

            return FactorGroup.IDENT.ordinal();
        }
        if (Character.isWhitespace(symbol)){
            if (symbol == '\n' || symbol == '\r')
                return FactorGroup.LINE.ordinal();
            return FactorGroup.SPACE.ordinal();
        }

        if (symbol == '[')
            return FactorGroup.LPAREN.ordinal();

        if (symbol == ']')
            return FactorGroup.RPAREN.ordinal();

        if (symbol == ';' || symbol == '#')
            return FactorGroup.COMMENTS.ordinal();

        return FactorGroup.OTHER.ordinal();
    }

    public static int nextState(int currentState, char symbol){
        int factorId = getFactorId(symbol);
        return transitionTable[currentState][factorId];
    }

}
