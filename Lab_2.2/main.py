from __future__ import annotations

import abc
import enum
import parser_edsl as pe
import sys
import re
import typing
from dataclasses import dataclass
from pprint import pprint

@dataclass
class ArrType:
  name: str
  
@dataclass
class Type:
  content: list[str | ArrType]
  name: str
  
@dataclass
class Token:
  name: str

@dataclass
class Method:
  type: str
  name: str
  args: list[str | ArrType]

@dataclass
class AlterElement:
  is_rep: bool
  elem: RuleBody | str

@dataclass 
class Alter:
  content: list[AlterElement]
  method: str | None

@dataclass
class RuleBody:
  alts: list[Alter]

@dataclass 
class Rule:
  nonterm: str
  body: RuleBody

@dataclass
class Specification:
  class_name : str
  tokens: list[Token]
  types: list[Type]
  methods: list[Method]
  rules: list[Rule]
  axiom: str

IDENT = pe.Terminal('IDENT', '[a-zA-Z][a-zA-Z0-9_]*', str)

def make_keyword(image):
  return pe.Terminal(image, image, lambda name: None,
                      re_flags=re.IGNORECASE, priority=10)

KW_CLASS, KW_TOKENS, KW_TYPES, KW_METHODS, KW_GRAMMAR, KW_AXIOM, KW_END, KW_REP = \
  map(make_keyword, '%class %tokens %types %methods %grammar %axiom %end %rep'.split())

NSpecification = pe.NonTerminal('Specification')

NTokens, NToken = \
  map(pe.NonTerminal, 'Tokens Token'.split())
  
NTypeDefs, NTypeDef, NTypeContent, NType = \
  map(pe.NonTerminal, 'TypeDefs TypeDef TypeContent Type'.split())
  
NMethodDefs, NMethodDef, NMethodArgs, NMethodArgsList = \
  map(pe.NonTerminal, 'MethodDefs MethodDef MethodArgs MethodArgsList'.split())
  
NRuleDefs, NRuleDef, NRuleBody, NAlter, NAlterContent, NAlterMethod, NAlterElement, NElemRep = \
  map(pe.NonTerminal, 'RuleDefs RuleDef RuleBody Alter AlterContent AlterMethod AlterElement ElemRep'.split())

NSpecification |= KW_CLASS, IDENT, KW_TOKENS, NTokens, KW_TYPES, NTypeDefs, KW_METHODS, NMethodDefs, KW_GRAMMAR, NRuleDefs, KW_AXIOM, IDENT, KW_END, Specification

NTokens |= lambda: []
NTokens |= NTokens, NToken, lambda ts, t: ts + [t]
NToken |= IDENT, Token

NTypeDefs |= lambda: []
NTypeDefs |= NTypeDefs, NTypeDef, lambda tds, td: tds + [td]
NTypeDef |= NTypeContent, ':', NType, ";", Type
NTypeContent |= IDENT, lambda i: [i]
NTypeContent |= NTypeContent, ",", IDENT, lambda tc, i: tc + [i]
NType |= IDENT, str
NType |= IDENT, "[]", ArrType

NMethodDefs |= lambda: []
NMethodDefs |= NMethodDefs, NMethodDef, lambda mds, md: mds + [md]
NMethodDef |= NType, IDENT, "(", NMethodArgs, ")", ";", Method
NMethodArgs |= lambda: []
NMethodArgs |= NMethodArgsList
NMethodArgsList |= NType, lambda t: [t]
NMethodArgsList |= NMethodArgsList, ",", NType, lambda ms, t: ms + [t] 

NRuleDefs |= lambda: []
NRuleDefs |= NRuleDefs, NRuleDef, lambda rds, rd: rds + [rd]
NRuleDef |= IDENT, "=", NRuleBody, ";", Rule

NRuleBody |= NAlter, lambda alter: RuleBody([alter])
NRuleBody |= NRuleBody, "|", NAlter, lambda body, alter: RuleBody(body.alts + [alter])

NAlter |= NAlterContent, NAlterMethod, Alter
NAlterContent |= lambda: []
NAlterContent |= NAlterContent, NAlterElement, lambda content, elem: content + [elem]

NAlterElement |= NElemRep, IDENT, AlterElement
NAlterElement |= NElemRep, "(", NRuleBody, ")", AlterElement

NAlterMethod |= lambda: ""
NAlterMethod |= "/", IDENT, str

NElemRep |= KW_REP, lambda: True
NElemRep |= lambda: False




p = pe.Parser(NSpecification)
try:
  assert p.is_lalr_one()
except:
  p.print_table()
  exit()

p.add_skipped_domain('\\s')
p.add_skipped_domain('\\$[^\\n]*')

for filename in sys.argv[1:]:
  try:
    with open(filename) as f:
      tree = p.parse(f.read())
      pprint(tree)
  except pe.Error as e:
    print(f'Ошибка {e.pos}: {e.message}')
  except Exception as e:
    print(e)