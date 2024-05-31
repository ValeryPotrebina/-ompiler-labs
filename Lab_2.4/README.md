# Лабораторная работа № 2.4.    
29.05.2024

# Цель работы


# Индивидуальный вариант


# Грамматика

Терминал - не раскрывается
Нетерминал = раскрывается в псоледовательность нетерм и термим

# Абстрактный синтаксис

Specification => 
    '%class' ClassName '%tokens' Tokens '%types' TypesDefs '%methods' MethodDefs '%grammar' RulesList '%axiom' Axiom '%end'

IDENT => [a-zA-Z][a-zA-Z0-9]*
Type=> IDENT | IDENT[]


ClassName => IDENT

Tokens => Token*
Token => IDENT

TypesDefs => TypeDef*
TypeDef => TypeContent ':' Type ';'
TypeContent => IDENT ',' ... ',' IDENT


MethodDefs => MethodDef*
MethodDef => MethodType MethodName '(' MethodArgs ')' ';'
MethodType => Type
MethodName => IDENT
MethodArgs => Type ',' ... ',' Type

RulesList => Rule*
Rule => RuleHead '=' RuleBody ';'
RuleHead => IDENT 
RuleBody => Alter '|' ... '|' Alter
Alter => AlterContent AlterMethod?
AlterContent => AlterElement*
AlterElement => ElemRep? ElemContent
ElemContent => IDENT | '(' RuleBody ')'
ElemRep => '%rep'
AlterMethod => '/' IDENT

Axiom => IDENT

# Грамматика


IDENT => [a-zA-Z][a-zA-Z0-9]*
ARR_TYPE =? '[]'
LPAR => '('
RPAR => ')'
COLON => ':'
SEMICOLON => ';'
COMMA => ','
SLASH => '/'
OR => '|'
EQUAL => '='
KW_CLASS -> '%class'
KW_TOKENS -> '%tokens'
KW_TYPES -> '%types'
KW_METHODS -> '%methods'
KW_GRAMMAR -> '%grammar'
KW_AXIOM -> '%axiom'
KW_REP -> '%rep'
KW_END -> '%end'

Specification =>
KW_CLASS IDENT KW_TOKENS Tokens KW_TYPES TypesDefs KW_METHODS MethodDefs KW_GRAMMAR RulesList KW_AXIOM Axiom KW_END

ClassName   => IDENT

Type        => IDENT ARR_TYPE?
Tokens      => IDENT*


TypesDefs   => TypeDef*
TypeDef     => TypeContent COLON Type SEMICOLON
TypeContent => IDENT TypeContent2*
TypeContent2=> COMMA IDENT

MethodDefs => MethodDef*
MethodDef => Type IDENT LPAR MethodArgs? RPAR SEMICOLON
MethodArgs =>  Type (COMMA Type)*

RulesList => Rule*
Rule => IDENT EQUAL RuleBody SEMICOLON
RuleBody => Alter (OR Alter)*
Alter => AlterContent AlterMethod? 
AlterContent => AlterElement*
AlterElement => KW_REP? ElemContent
AlterMethod => SLASH IDENT
ElemContent => IDENT | LPAR RuleBody RPAR

Axiom => IDENT