/* Parser.java */
/* Generated By:JJTree&JavaCC: Do not edit this line. Parser.java */
import java.util.*;
import java.io.*;

import java.io.StringReader;

public class Parser/*@bgen(jjtree)*/implements ParserTreeConstants, ParserConstants {/*@bgen(jjtree)*/
  protected static JJTParserState jjtree = new JJTParserState();private int whileExpressionErrorsCounter;
    private int MAX_WHILE_EXPRESSION_ERRORS = 10;

    public Parser(String stringReader) throws ParseException, FileNotFoundException{
        this.whileExpressionErrorsCounter = 0;

        System.out.println("Parsing...");

        System.setIn(new FileInputStream(stringReader));

                Parser parser = new Parser(System.in);
                SimpleNode root = parser.Program();  // returns reference to root node

                root.dump(""); // prints the tree on the screen
    }

    /* Skips the while expression until if finds the token "{" */
    public void skipWhileExpression() {
        Token prev; // will store the previous token

        do{
            prev = token;
            token = getNextToken();
        } while(token.kind != LBRACKET && token.kind != EOF);

        if(token.kind == LBRACKET)
            token = prev;
    }

    /* Handles with while expression error */
    public void handleWhileExpressionError(ParseException exception) throws ParseException {

        whileExpressionErrorsCounter++;

        if(whileExpressionErrorsCounter > MAX_WHILE_EXPRESSION_ERRORS) {
            System.out.println("Maximum Number of While Expression Errors(" + MAX_WHILE_EXPRESSION_ERRORS + ") exceeded.");
            throw new ParseException();
        }
        else{
            System.out.println("Error Number "+whileExpressionErrorsCounter+" -> "+ exception);
        }




        skipWhileExpression();
    }

  final public SimpleNode Program() throws ParseException {/*@bgen(jjtree) Program */
  SimpleNode jjtn000 = new SimpleNode(JJTPROGRAM);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      ImportDeclaration();
      ClassDeclaration();
      jj_consume_token(0);
jjtree.closeNodeScope(jjtn000, true);
                                                   jjtc000 = false;
{if ("" != null) return jjtn000;}
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
    throw new Error("Missing return statement in function");
  }

  final public void ImportDeclaration() throws ParseException {/*@bgen(jjtree) Imports */
    SimpleNode jjtn000 = new SimpleNode(JJTIMPORTS);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token base, comp;
    String importText = "";
    try {
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case IMPORT:{
          ;
          break;
          }
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
        jj_consume_token(IMPORT);
        base = jj_consume_token(IDENTIFIER);
        label_2:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case DOT:{
            ;
            break;
            }
          default:
            jj_la1[1] = jj_gen;
            break label_2;
          }
          jj_consume_token(DOT);
          comp = jj_consume_token(IDENTIFIER);
importText += "." + comp.image;
        }
        jj_consume_token(PVIRG);
SimpleNode jjtn001 = new SimpleNode(JJTIMPORT);
                                                                                                          boolean jjtc001 = true;
                                                                                                          jjtree.openNodeScope(jjtn001);
        try {
jjtree.closeNodeScope(jjtn001, true);
                                                                                                          jjtc001 = false;
jjtn001.val = base.image + importText;
        } finally {
if (jjtc001) {
                                                                                                            jjtree.closeNodeScope(jjtn001, true);
                                                                                                          }
        }
      }
    } finally {
if (jjtc000) {
       jjtree.closeNodeScope(jjtn000, true);
     }
    }
  }

  final public void ClassDeclaration() throws ParseException {/*@bgen(jjtree) Class */
    SimpleNode jjtn000 = new SimpleNode(JJTCLASS);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token name, comp;
    try {
      jj_consume_token(CLASS);
      name = jj_consume_token(IDENTIFIER);
jjtn000.val = name.image;
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case EXTENDS:{
        jj_consume_token(EXTENDS);
        comp = jj_consume_token(IDENTIFIER);
jjtn000.val += " " + comp.image;
        break;
        }
      default:
        jj_la1[2] = jj_gen;
        ;
      }
      jj_consume_token(LBRACKET);
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case INT:
        case BOOLEAN:
        case IDENTIFIER:{
          ;
          break;
          }
        default:
          jj_la1[3] = jj_gen;
          break label_3;
        }
        VarDeclaration();
      }
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case PUBLIC:{
          ;
          break;
          }
        default:
          jj_la1[4] = jj_gen;
          break label_4;
        }
        MethodDeclaration();
      }
      jj_consume_token(RBRACKET);
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  final public void VarDeclaration() throws ParseException {/*@bgen(jjtree) VarDeclaration */
  SimpleNode jjtn000 = new SimpleNode(JJTVARDECLARATION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      Type();
      VarDeclaration1();
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  final public void VarDeclaration1() throws ParseException {/*@bgen(jjtree) VarId */
    SimpleNode jjtn000 = new SimpleNode(JJTVARID);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token id;
    try {
      id = jj_consume_token(IDENTIFIER);
jjtn000.val = id.image;
      jj_consume_token(PVIRG);
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  final public void MethodDeclaration() throws ParseException {/*@bgen(jjtree) MethodDeclaration */
  SimpleNode jjtn000 = new SimpleNode(JJTMETHODDECLARATION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(PUBLIC);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case INT:
      case BOOLEAN:
      case IDENTIFIER:{
        RegularMethod();
        break;
        }
      case STATIC:{
        Main();
        break;
        }
      default:
        jj_la1[5] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  final public void RegularMethod() throws ParseException {/*@bgen(jjtree) RegularMethod */
    SimpleNode jjtn000 = new SimpleNode(JJTREGULARMETHOD);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token name;
    try {
SimpleNode jjtn001 = new SimpleNode(JJTRETURNTYPE);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      try {
        Type();
      } catch (Throwable jjte001) {
if (jjtc001) {
        jjtree.clearNodeScope(jjtn001);
        jjtc001 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte001 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte001;}
      }
      if (jjte001 instanceof ParseException) {
        {if (true) throw (ParseException)jjte001;}
      }
      {if (true) throw (Error)jjte001;}
      } finally {
if (jjtc001) {
        jjtree.closeNodeScope(jjtn001, true);
      }
      }
      name = jj_consume_token(IDENTIFIER);
jjtn000.val = name.image;
      jj_consume_token(LPAR);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case INT:
      case BOOLEAN:
      case IDENTIFIER:{
        MethodParams();
        break;
        }
      default:
        jj_la1[6] = jj_gen;
        ;
      }
      jj_consume_token(RPAR);
      jj_consume_token(LBRACKET);
SimpleNode jjtn002 = new SimpleNode(JJTMETHODBODY);
          boolean jjtc002 = true;
          jjtree.openNodeScope(jjtn002);
      try {
        MethodBody();
      } catch (Throwable jjte002) {
if (jjtc002) {
            jjtree.clearNodeScope(jjtn002);
            jjtc002 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte002 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte002;}
          }
          if (jjte002 instanceof ParseException) {
            {if (true) throw (ParseException)jjte002;}
          }
          {if (true) throw (Error)jjte002;}
      } finally {
if (jjtc002) {
            jjtree.closeNodeScope(jjtn002, true);
          }
      }
      jj_consume_token(RETURN);
SimpleNode jjtn003 = new SimpleNode(JJTRETURNSTATEMENT);
                   boolean jjtc003 = true;
                   jjtree.openNodeScope(jjtn003);
      try {
        Expression();
      } catch (Throwable jjte003) {
if (jjtc003) {
                     jjtree.clearNodeScope(jjtn003);
                     jjtc003 = false;
                   } else {
                     jjtree.popNode();
                   }
                   if (jjte003 instanceof RuntimeException) {
                     {if (true) throw (RuntimeException)jjte003;}
                   }
                   if (jjte003 instanceof ParseException) {
                     {if (true) throw (ParseException)jjte003;}
                   }
                   {if (true) throw (Error)jjte003;}
      } finally {
if (jjtc003) {
                     jjtree.closeNodeScope(jjtn003, true);
                   }
      }
      jj_consume_token(PVIRG);
      jj_consume_token(RBRACKET);
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  final public void Main() throws ParseException {/*@bgen(jjtree) Main */
  SimpleNode jjtn000 = new SimpleNode(JJTMAIN);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(STATIC);
      jj_consume_token(VOID);
      jj_consume_token(MAIN);
      jj_consume_token(LPAR);
      jj_consume_token(STRING);
      jj_consume_token(LRPAR);
      jj_consume_token(RRPAR);
      jj_consume_token(IDENTIFIER);
      jj_consume_token(RPAR);
      jj_consume_token(LBRACKET);
SimpleNode jjtn001 = new SimpleNode(JJTMETHODBODY);
          boolean jjtc001 = true;
          jjtree.openNodeScope(jjtn001);
      try {
        MethodBody();
      } catch (Throwable jjte001) {
if (jjtc001) {
            jjtree.clearNodeScope(jjtn001);
            jjtc001 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte001 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte001;}
          }
          if (jjte001 instanceof ParseException) {
            {if (true) throw (ParseException)jjte001;}
          }
          {if (true) throw (Error)jjte001;}
      } finally {
if (jjtc001) {
            jjtree.closeNodeScope(jjtn001, true);
          }
      }
      jj_consume_token(RBRACKET);
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  final public void MethodParams() throws ParseException {/*@bgen(jjtree) MethodParams */
  SimpleNode jjtn000 = new SimpleNode(JJTMETHODPARAMS);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      MethodParam();
      label_5:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case VIRG:{
          ;
          break;
          }
        default:
          jj_la1[7] = jj_gen;
          break label_5;
        }
        jj_consume_token(VIRG);
        MethodParam();
      }
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  final public void MethodParam() throws ParseException {/*@bgen(jjtree) MethodParam */
    SimpleNode jjtn000 = new SimpleNode(JJTMETHODPARAM);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token name;
    try {
      Type();
      name = jj_consume_token(IDENTIFIER);
SimpleNode jjtn001 = new SimpleNode(JJTVARID);
                                 boolean jjtc001 = true;
                                 jjtree.openNodeScope(jjtn001);
      try {
jjtree.closeNodeScope(jjtn001,  0);
                                 jjtc001 = false;
jjtn001.val = name.image;
      } finally {
if (jjtc001) {
                                   jjtree.closeNodeScope(jjtn001,  0);
                                 }
      }
    } catch (Throwable jjte000) {
if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  final public void MethodBody() throws ParseException {Token type;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case IF:
    case INT:
    case NEW:
    case TRUE:
    case THIS:
    case WHILE:
    case FALSE:
    case BOOLEAN:
    case LBRACKET:
    case LPAR:
    case NOT:
    case IDENTIFIER:
    case INTEGERLITERAL:{
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFIER:{
        type = jj_consume_token(IDENTIFIER);
SimpleNode jjtn001 = new SimpleNode(JJTTYPE);
                               boolean jjtc001 = true;
                               jjtree.openNodeScope(jjtn001);
        try {
jjtree.closeNodeScope(jjtn001,  0);
                               jjtc001 = false;
jjtn001.val = type.image;
        } finally {
if (jjtc001) {
                                 jjtree.closeNodeScope(jjtn001,  0);
                               }
        }
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case IDENTIFIER:{
SimpleNode jjtn002 = new SimpleNode(JJTVARDECLARARTION);
                                                                      boolean jjtc002 = true;
                                                                      jjtree.openNodeScope(jjtn002);
          try {
            VarDeclaration1();
          } catch (Throwable jjte002) {
if (jjtc002) {
                                                                        jjtree.clearNodeScope(jjtn002);
                                                                        jjtc002 = false;
                                                                      } else {
                                                                        jjtree.popNode();
                                                                      }
                                                                      if (jjte002 instanceof RuntimeException) {
                                                                        {if (true) throw (RuntimeException)jjte002;}
                                                                      }
                                                                      if (jjte002 instanceof ParseException) {
                                                                        {if (true) throw (ParseException)jjte002;}
                                                                      }
                                                                      {if (true) throw (Error)jjte002;}
          } finally {
if (jjtc002) {
                                                                        jjtree.closeNodeScope(jjtn002,  2);
                                                                      }
          }
          MethodBody();
          break;
          }
        case LRPAR:
        case DOT:
        case EQUAL:
        case AND:
        case LESS:
        case PLUS:
        case MINUS:
        case MULT:
        case DIV:{
          Statement2();
          label_6:
          while (true) {
            switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
            case IF:
            case NEW:
            case TRUE:
            case THIS:
            case WHILE:
            case FALSE:
            case LBRACKET:
            case LPAR:
            case NOT:
            case IDENTIFIER:
            case INTEGERLITERAL:{
              ;
              break;
              }
            default:
              jj_la1[8] = jj_gen;
              break label_6;
            }
            Statement();
          }
          break;
          }
        default:
          jj_la1[9] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
        }
      case INT:
      case BOOLEAN:{
        Type1();
SimpleNode jjtn003 = new SimpleNode(JJTVARDECLARARTION);
                     boolean jjtc003 = true;
                     jjtree.openNodeScope(jjtn003);
        try {
          VarDeclaration1();
        } catch (Throwable jjte003) {
if (jjtc003) {
                       jjtree.clearNodeScope(jjtn003);
                       jjtc003 = false;
                     } else {
                       jjtree.popNode();
                     }
                     if (jjte003 instanceof RuntimeException) {
                       {if (true) throw (RuntimeException)jjte003;}
                     }
                     if (jjte003 instanceof ParseException) {
                       {if (true) throw (ParseException)jjte003;}
                     }
                     {if (true) throw (Error)jjte003;}
        } finally {
if (jjtc003) {
                       jjtree.closeNodeScope(jjtn003,  2);
                     }
        }
        MethodBody();
        break;
        }
      case IF:
      case NEW:
      case TRUE:
      case THIS:
      case WHILE:
      case FALSE:
      case LBRACKET:
      case LPAR:
      case NOT:
      case INTEGERLITERAL:{
        Statement1();
        label_7:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case IF:
          case NEW:
          case TRUE:
          case THIS:
          case WHILE:
          case FALSE:
          case LBRACKET:
          case LPAR:
          case NOT:
          case IDENTIFIER:
          case INTEGERLITERAL:{
            ;
            break;
            }
          default:
            jj_la1[10] = jj_gen;
            break label_7;
          }
          Statement();
        }
        break;
        }
      default:
        jj_la1[11] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
      }
    default:
      jj_la1[12] = jj_gen;
      ;
    }
  }

  final public void Type() throws ParseException {Token type;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case INT:
    case BOOLEAN:{
      Type1();
      break;
      }
    case IDENTIFIER:{
      type = jj_consume_token(IDENTIFIER);
SimpleNode jjtn001 = new SimpleNode(JJTTYPE);
                                    boolean jjtc001 = true;
                                    jjtree.openNodeScope(jjtn001);
      try {
jjtree.closeNodeScope(jjtn001,  0);
                                    jjtc001 = false;
jjtn001.val = type.image;
      } finally {
if (jjtc001) {
                                      jjtree.closeNodeScope(jjtn001,  0);
                                    }
      }
      break;
      }
    default:
      jj_la1[13] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void Type1() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case INT:{
SimpleNode jjtn001 = new SimpleNode(JJTTYPE);
      boolean jjtc001 = true;
      jjtree.openNodeScope(jjtn001);
      try {
        jj_consume_token(INT);
jjtn001.val = "int";
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case LRPAR:{
          jj_consume_token(LRPAR);
          jj_consume_token(RRPAR);
jjtn001.val = "[]";
          break;
          }
        default:
          jj_la1[14] = jj_gen;
          ;
        }
      } finally {
if (jjtc001) {
        jjtree.closeNodeScope(jjtn001,  0);
      }
      }
      break;
      }
    case BOOLEAN:{
      jj_consume_token(BOOLEAN);
SimpleNode jjtn002 = new SimpleNode(JJTTYPE);
                                                                                                   boolean jjtc002 = true;
                                                                                                   jjtree.openNodeScope(jjtn002);
      try {
jjtree.closeNodeScope(jjtn002,  0);
                                                                                                   jjtc002 = false;
jjtn002.val = "boolean";
      } finally {
if (jjtc002) {
                                                                                                     jjtree.closeNodeScope(jjtn002,  0);
                                                                                                   }
      }
      break;
      }
    default:
      jj_la1[15] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void Statement() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case IF:
    case NEW:
    case TRUE:
    case THIS:
    case WHILE:
    case FALSE:
    case LBRACKET:
    case LPAR:
    case NOT:
    case INTEGERLITERAL:{
      Statement1();
      break;
      }
    case IDENTIFIER:{
      jj_consume_token(IDENTIFIER);
      Statement2();
      break;
      }
    default:
      jj_la1[16] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void Statement1() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case LBRACKET:{
      jj_consume_token(LBRACKET);
      label_8:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case IF:
        case NEW:
        case TRUE:
        case THIS:
        case WHILE:
        case FALSE:
        case LBRACKET:
        case LPAR:
        case NOT:
        case IDENTIFIER:
        case INTEGERLITERAL:{
          ;
          break;
          }
        default:
          jj_la1[17] = jj_gen;
          break label_8;
        }
        Statement();
      }
      jj_consume_token(RBRACKET);
      break;
      }
    case IF:{
      jj_consume_token(IF);
      jj_consume_token(LPAR);
      Expression();
      jj_consume_token(RPAR);
      Statement();
      jj_consume_token(ELSE);
      Statement();
      break;
      }
    case WHILE:{
      While();
      break;
      }
    case NEW:
    case TRUE:
    case THIS:
    case FALSE:
    case LPAR:
    case NOT:
    case INTEGERLITERAL:{
      Expression3();
      Expression1();
      jj_consume_token(PVIRG);
      break;
      }
    default:
      jj_la1[18] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void While() throws ParseException {
    try {
      jj_consume_token(WHILE);
      WhileExpression();
    } catch (ParseException e) {
handleWhileExpressionError(e);
    }
    Statement();
  }

  final public void WhileExpression() throws ParseException {
    jj_consume_token(LPAR);
    Expression();
    jj_consume_token(RPAR);
  }

  final public void Statement2() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case EQUAL:{
      jj_consume_token(EQUAL);
      Expression();
      jj_consume_token(PVIRG);
      break;
      }
    case LRPAR:{
      jj_consume_token(LRPAR);
      Expression();
      jj_consume_token(RRPAR);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case EQUAL:{
        jj_consume_token(EQUAL);
        Expression();
        jj_consume_token(PVIRG);
        break;
        }
      default:
        jj_la1[19] = jj_gen;
        ;
      }
      break;
      }
    case DOT:
    case AND:
    case LESS:
    case PLUS:
    case MINUS:
    case MULT:
    case DIV:{
      Expression4();
      jj_consume_token(PVIRG);
      break;
      }
    default:
      jj_la1[20] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void Expression() throws ParseException {
    Expression2();
    Expression1();
  }

  final public void Expression1() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case DOT:
    case AND:
    case LESS:
    case PLUS:
    case MINUS:
    case MULT:
    case DIV:{
      Expression4();
      break;
      }
    case LRPAR:{
      jj_consume_token(LRPAR);
      Expression2();
      Expression1();
      jj_consume_token(RRPAR);
      break;
      }
    default:
      jj_la1[21] = jj_gen;

    }
  }

  final public void Expression2() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case NEW:
    case TRUE:
    case THIS:
    case FALSE:
    case LPAR:
    case NOT:
    case INTEGERLITERAL:{
      Expression3();
      break;
      }
    case IDENTIFIER:{
      jj_consume_token(IDENTIFIER);
      break;
      }
    default:
      jj_la1[22] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void Expression3() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case INTEGERLITERAL:{
      jj_consume_token(INTEGERLITERAL);
      break;
      }
    case TRUE:{
      jj_consume_token(TRUE);
      break;
      }
    case FALSE:{
      jj_consume_token(FALSE);
      break;
      }
    case THIS:{
      jj_consume_token(THIS);
      break;
      }
    case NEW:{
      jj_consume_token(NEW);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IDENTIFIER:{
        jj_consume_token(IDENTIFIER);
        jj_consume_token(LPAR);
        jj_consume_token(RPAR);
        break;
        }
      case INT:{
        jj_consume_token(INT);
        jj_consume_token(LRPAR);
        Expression2();
        Expression1();
        jj_consume_token(RRPAR);
        break;
        }
      default:
        jj_la1[23] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
      }
    case NOT:{
      jj_consume_token(NOT);
      Expression2();
      Expression1();
      break;
      }
    case LPAR:{
      jj_consume_token(LPAR);
      Expression2();
      Expression1();
      jj_consume_token(RPAR);
      break;
      }
    default:
      jj_la1[24] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void Expression4() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case AND:
    case LESS:
    case PLUS:
    case MINUS:
    case MULT:
    case DIV:{
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case AND:{
        jj_consume_token(AND);
        break;
        }
      case LESS:{
        jj_consume_token(LESS);
        break;
        }
      case PLUS:{
        jj_consume_token(PLUS);
        break;
        }
      case MINUS:{
        jj_consume_token(MINUS);
        break;
        }
      case MULT:{
        jj_consume_token(MULT);
        break;
        }
      case DIV:{
        jj_consume_token(DIV);
        break;
        }
      default:
        jj_la1[25] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      Expression2();
      Expression1();
      break;
      }
    case DOT:{
      jj_consume_token(DOT);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case LENGTH:{
        jj_consume_token(LENGTH);
        break;
        }
      case IDENTIFIER:{
        jj_consume_token(IDENTIFIER);
        jj_consume_token(LPAR);
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case NEW:
        case TRUE:
        case THIS:
        case FALSE:
        case LPAR:
        case NOT:
        case IDENTIFIER:
        case INTEGERLITERAL:{
          Expression2();
          Expression1();
          label_9:
          while (true) {
            switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
            case VIRG:{
              ;
              break;
              }
            default:
              jj_la1[26] = jj_gen;
              break label_9;
            }
            jj_consume_token(VIRG);
            Expression2();
            Expression1();
          }
          break;
          }
        default:
          jj_la1[27] = jj_gen;
          ;
        }
        jj_consume_token(RPAR);
        break;
        }
      default:
        jj_la1[28] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
      }
    default:
      jj_la1[29] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  /** Generated Token Manager. */
  public ParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[30];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x2000000,0x0,0x8000000,0x4000400,0x100000,0x4200400,0x4000400,0x0,0x50074a00,0x0,0x50074a00,0x54074e00,0x54074e00,0x4000400,0x0,0x4000400,0x50074a00,0x50074a00,0x50074a00,0x0,0x0,0x0,0x40054800,0x400,0x40054800,0x0,0x0,0x40054800,0x800000,0x0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x8,0x0,0x2000,0x0,0x2000,0x2000,0x20,0x6004,0x3f99,0x6004,0x6004,0x6004,0x2000,0x1,0x0,0x6004,0x6004,0x4004,0x10,0x1f99,0x1f89,0x6004,0x2000,0x4004,0x1f80,0x20,0x6004,0x2000,0x1f88,};
   }

  /** Constructor with InputStream. */
  public Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Parser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public Parser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
	if (jj_input_stream == null) {
      jj_input_stream = new SimpleCharStream(stream, 1, 1);
   } else {
      jj_input_stream.ReInit(stream, 1, 1);
   }
   if (token_source == null) {
      token_source = new ParserTokenManager(jj_input_stream);
   }

    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public Parser(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[47];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 30; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 47; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
